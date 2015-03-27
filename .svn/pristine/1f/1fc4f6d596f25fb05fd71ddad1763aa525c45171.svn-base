package com.beyondb.gdal.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;

/**
 * 对空间数据库表进行预处理，默认有三种模式：覆盖，追加，更新。
 *
 * @author guanml <guanminglin@gmail.com>
 */
public class TableReset {

    public final int OVERWRITE = 0;
    public final int UPDATE = 1;
    public final int APPEND = 2;
    private NutDao dao;

    public TableReset(NutDao dao) {
        this.dao = dao;
    }

    /**
     * 设置指定名称的表为覆盖模式
     *
     * @param owner
     * @param tableName
     */
    public void setTableOverWrite(String owner, String tableName) {
        if (!existTable(owner, tableName)) {
            return;
        }
        Sql sql = Sqls.create("drop table $owner.$tabename");
        sql.vars().set("owner", owner);
        sql.vars().set("tablename", tableName);
        dao.execute(sql);
    }

    /**
     * 设置指定名称的表为更新模式
     *
     * @param owner
     * @param tableName
     */
    public void setTableUpdate(String owner, String tableName) {
        if (!existTable(owner, tableName)) {
            return;
        }
        Sql sql = Sqls.create("delete * from $owner.$tabename ");
        sql.vars().set("owner", owner);
        sql.vars().set("tablename", tableName);
        dao.execute(sql);
    }

    /**
     * 判断是否存在指定名称的表
     *
     * @param owner 表的拥有者
     * @param tableName 表名称
     * @return 是否存在
     */
    private boolean existTable(String owner, String tableName) {
        Sql sql = Sqls.create("select table_name from iitables where table_owner='$owner' and table_name='$tablename'");
        sql.vars().set("tablename", tableName);
        sql.vars().set("owner", owner);
        sql.setCallback(new SqlCallback() {
            @Override
            public Object invoke(Connection conn, ResultSet rs, Sql sql)
                    throws SQLException {
                return rs.next();
            }
        });
        dao.execute(sql);
        boolean exist = sql.getObject(Boolean.class);
        return exist;
    }
}
