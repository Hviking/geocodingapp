package org.nutz.dao.impl.jdbc.beyondb;

import java.util.List;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Entity;
import org.nutz.dao.entity.MappingField;
import org.nutz.dao.entity.PkType;
import org.nutz.dao.impl.jdbc.AbstractJdbcExpert;
import org.nutz.dao.jdbc.JdbcExpertConfigFile;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Pojo;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.Pojos;
import org.nutz.lang.Lang;

/**
 * 实现Nutz 接口，实现Nutz对BeyonDB1.1/BeyonDB 2.0 数据库的支持
 *
 * @author guanml
 */
public class BeyondbJdbcExpert extends AbstractJdbcExpert {

    public BeyondbJdbcExpert(JdbcExpertConfigFile conf) {
        super(conf);
    }

    public String getDatabaseType() {
        return "BeyonDB";
    }

    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        if (offset > 0) {
            sql.append(" OFFSET " + offset);
            if (limit > 0 && limit < Integer.MAX_VALUE) {
                sql.append(" FETCH NEXT " + limit + " ROWS ONLY ");
            }
        } else if (limit > 0 && limit < Integer.MAX_VALUE) {
            sql.append(" FETCH FIRST " + limit + " ROWS ONLY ");
        }
    }

    @Override
    public void formatQuery(Pojo pojo) {
        Pager pager = pojo.getContext().getPager();
        // 需要进行分页
        int offset = pager.getOffset();
        if(null != pager && pager.getPageNumber() > 0 &&offset ==0){
             pojo.append(Pojos.Items.wrapf(" FETCH FIRST %d ROWS ONLY",
                    pager.getPageSize()));
        }else if (null != pager && pager.getPageNumber() > 0 &&  offset> 0) {
            pojo.append(Pojos.Items.wrapf(" OFFSET %d FETCH NEXT %d ROWS ONLY",
                    offset,
                    pager.getPageSize()));
        }
    }

    @Override
    public void formatQuery(Sql sql) {
        Pager pager = sql.getContext().getPager();

          int offset = pager.getOffset();
        if(null != pager && pager.getPageNumber() > 0 &&offset ==0){
              sql.setSourceSql(sql.getSourceSql() + String.format(" FETCH FIRST %d ROWS ONLY",
                    pager.getPageSize()));
        }else if (null != pager && pager.getPageNumber() > 0 &&  offset> 0) {
           sql.setSourceSql(sql.getSourceSql() + String.format(" OFFSET %d FETCH NEXT %d ROWS ONLY",
                    offset,
                    pager.getPageSize()));
        }
    }

    //创建实体
    @Override
    public boolean createEntity(Dao dao, Entity<?> en) {
        boolean isAutoIncrement = false;
        StringBuilder sb = new StringBuilder("CREATE TABLE " + en.getTableName() + "(");
        // 创建字段
        for (MappingField mf : en.getMappingFields()) {
            sb.append('\n').append(mf.getColumnName());
            if (mf.isId() && mf.isAutoIncreasement()) {
                isAutoIncrement = true;
                sb.append(" default ").append(" seq_").append(en.getTableName()).append(".nextval not null,");
            } else {
                sb.append(' ').append(evalFieldType(mf));
                // 非主键的 @Name，应该加入唯一性约束
                if (mf.isName() && en.getPkType() != PkType.NAME) {
                    sb.append(" UNIQUE NOT NULL");
                } // 普通字段
                else {
                    if (mf.isUnsigned()) {
                        sb.append(" UNSIGNED");
                    }
                    if (mf.isNotNull()) {
                        sb.append(" NOT NULL");
                    }
                    if (mf.isAutoIncreasement()) {
                        throw Lang.noImplement();
                    }
                    if (mf.hasDefaultValue()) {
                        sb.append(" DEFAULT '").append(getDefaultValue(mf)).append('\'');
                    }
                }
            }
            sb.append(',');
        }
        // 创建主键
        List<MappingField> pks = en.getPks();
        if (!pks.isEmpty()) {
            sb.append('\n');
            sb.append(String.format("CONSTRAINT %s_pkey PRIMARY KEY (", en.getTableName()));
            for (MappingField pk : pks) {
                sb.append(pk.getColumnName()).append(',');
            }
            sb.setCharAt(sb.length() - 1, ')');
            sb.append("\n ");
        }

        // 结束表字段设置
        sb.setCharAt(sb.length() - 1, ')');

        //创建sequence ，如果需要字段自增长
        if (isAutoIncrement) {
            dao.execute(Sqls.create(encodeSequnce(en.getTableName())));
        }

        // 执行创建语句
        dao.execute(Sqls.create(sb.toString()));

        // 创建索引
        dao.execute(createIndexs(en).toArray(new Sql[0]));

        // 创建关联表
        createRelation(dao, en);

        // 添加注释(表注释与字段注释)
        addComment(dao, en);

        return true;
    }

    /**
     * 生成创建序列的语句
     *
     * @param typeName 表名称
     * @return 创建序列的语句
     */
    public String encodeSequnce(String typeName) {
        StringBuilder createSeq = new StringBuilder();
        return createSeq.append("create sequence seq_").append(typeName).append(" minvalue 0 start with 0 nocache").toString();
    }

    @Override
    protected String evalFieldType(MappingField mf) {
        switch (mf.getColumnType()) {
            case BOOLEAN:
                return "SMALLINT";
            case INT:
            // 用户自定义了宽度
                // if (mf.getWidth() > 0)
                // return "decimal(" + mf.getWidth() + ")";
                // 用数据库的默认宽度
                return "INTEGER";

            case FLOAT:
                // 用户自定义了精度
                if (mf.getWidth() > 0 && mf.getPrecision() > 0) {
                    return "decimal(" + mf.getWidth() + "," + mf.getPrecision() + ")";
                }
                // 用默认精度
                if (mf.getTypeMirror().isDouble()) {
                    return "FLOAT8";
                }
                return "FLOAT";
            case TEXT:
                return "CLOB";
            case BINARY:
                return "LONG BYTE";
            default:
                break;
        }
        return super.evalFieldType(mf);
    }

    @Override
    protected String createResultSetMetaSql(Entity<?> en) {
        return "SELECT * FROM " + en.getViewName() + " FETCH FIRST 1 ROWS ONLY ";
    }

}
