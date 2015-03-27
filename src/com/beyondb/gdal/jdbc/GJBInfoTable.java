package com.beyondb.gdal.jdbc;

import com.beyondb.bean.GJBInfo;
import com.beyondb.utils.ZipCompressor;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;

/**
 * GJB文件元数据处理类。 处理流程：<p>
 * 1、 创建 md_tf_info 与md_file 表，并创建空间索引和id的索引。
 * 2、预先插入解析的gjb元数据信息（图幅id，图幅名称等信息） 3、创建图层表，然后将图层的所有字段信息导入到数据库中。
 * 注：每张表都需要添加一个tf_id 字段做为外键 4、
 *
 * @author guanml
 */
public class GJBInfoTable {

    private NutDao dao;
    private int scale;
    private int tf_id;
    private boolean overwrite = false;
    private String envelope;
    private double xld;  //x左下脚点
    private double xlu; //x左上脚点
    private double yld;  //y左下脚点
    private double ylu; //y左上脚点
    private double xrd; //x右下脚点
    private double xru; //x右上脚点
    private double yrd;  //y右下脚点
    private double yru;  //y右上脚点
    private double Xmax;
    private double Ymax;
    private double Xmin;
    private double Ymin;

    private static final  Logger m_Logger= Logger.getLogger(GJBInfoTable.class.getName());
    public GJBInfoTable(NutDao dao, boolean overwrite) {
        this.dao = dao;
        this.overwrite = overwrite;
    }
    public final static Map<String, Integer> TYPENAME_TO_CODE_MAP = new HashMap<String, Integer>() {
        {
            put("A", 1);
            put("B", 2);
            put("C", 3);
            put("D", 4);
            put("E", 5);
            put("F", 6);
            put("G", 7);
            put("H", 8);
            put("I", 9);
            put("J", 10);
            put("K", 11);
            put("L", 12);
            put("M", 13);
            put("N", 14);
            put("O", 15);
            put("P", 16);
            put("Q", 17);
            put("R", 18);
            put("S", 19);
            put("T", 20);
            put("U", 21);
            put("V", 22);
            put("W", 23);
            put("X", 24);
            put("Y", 25);
            put("Z", 26);

        }
    };

    /**
     * get geo typecode
     *
     * @param typeName
     * @return
     */
    public static int getTypeCode(String typeName) {
        Integer code = TYPENAME_TO_CODE_MAP.get(typeName.toUpperCase());
        return code != null ? code : -1;
    }

    /**
     * 字符窜经度转换
     *
     * @param lonlat
     */
    private void changeLon(String lonlat) {
        String[] lon = lonlat.split("-");
//        String x1end = lon[0].substring(lon[0].length()-4, lon[0].length());

//        lon[0]="1122354";
        String x1pre = lon[0].substring(0, lon[0].length() - 4);

        String miao1 = lon[0].substring(lon[0].length() - 2, lon[0].length());
        String fen1 = lon[0].substring(lon[0].length() - 4, lon[0].length() - 2);


//        String x2end = lon[1].substring(lon[1].length()-4, lon[1].length());
        String x2pre = lon[1].substring(0, lon[1].length() - 4);

        String miao2 = lon[1].substring(lon[1].length() - 2, lon[1].length());
        String fen2 = lon[1].substring(lon[1].length() - 4, lon[1].length() - 2);

        double x1 = (Double.parseDouble(miao1) / 60.0 + Double.parseDouble(fen1)) / 60.0 + Double.parseDouble(x1pre);
        double x2 = (Double.parseDouble(miao2) / 60.0 + Double.parseDouble(fen2)) / 60.0 + Double.parseDouble(x2pre);


        setXmax(x2);
        setXmin(x1);

    }

    /**
     * 字符窜纬度转换
     *
     * @param lonlat
     */
    private void changeLat(String lonlat) {
        String[] lat = lonlat.split("-");
//        lat[0]="402300";
//        String y1end = lat[0].substring(lat[0].length()-4, lat[0].length());
        String y1pre = lat[0].substring(0, lat[0].length() - 4);

        String miao1 = lat[0].substring(lat[0].length() - 2, lat[0].length());
        String fen1 = lat[0].substring(lat[0].length() - 4, lat[0].length() - 2);



//        String y2end = lat[1].substring(lat[1].length()-4, lat[1].length());
        String y2pre = lat[1].substring(0, lat[1].length() - 4);

        String miao2 = lat[1].substring(lat[1].length() - 2, lat[1].length());
        String fen2 = lat[1].substring(lat[1].length() - 4, lat[1].length() - 2);

        double y1 = (Double.parseDouble(miao1) / 60.0 + Double.parseDouble(fen1)) / 60.0 + Double.parseDouble(y1pre);
        double y2 = (Double.parseDouble(miao2) / 60.0 + Double.parseDouble(fen2)) / 60.0 + Double.parseDouble(y2pre);



        setYmax(y2);
        setYmin(y1);

    }

    /**
     * 创建GJB文件元数据表
     *
     * @return
     */
    public boolean createGJBInfoTable(String smallORbig) {
        String dropTalbe = "DROP TABLE IF EXISTS md_tf_info,md_file";
        String cseq = "create sequence seq_tf_id minvalue 0 start with 1 nocache";

        String md_tf_info = "create table MD_TF_INFO"
                + "(TF_ID int not null primary key,"
                + "TF_SMS long byte not null,"
                + "TF_NAME varchar(256) not null,"
                + "TF_SCALE int not null,"
                + "TF_SCALE_NAME varchar(30),"
                + "TF_ENVELOPE st_Geometry not null,"
                + "TF_UPDATETIME datetime"
                + ")";

        String md_file = "create table md_file(file_id int not null auto_increment primary key,"
                + "file_tf_id int not null,"
                + "file_layer_id int not null,"
                + "file_name varchar(256) not null,"
                + "file long byte not null,"
                + " foreign key(file_tf_id) references md_tf_info(tf_id) ON DELETE CASCADE)";

        //  String index = "create index gjb_index on md_file(file_id)";
        String view = "create view gjbfile as select * from md_tf_info a,md_file b where a.tf_id=b.file_tf_id";
        
        String sync ="create table sync(\n" +
            "    id int primary key auto_increment,\n" +
            "    status varchar(50),\n" +
            "    starttime datetime,\n" +
            "    endtime datetime\n" +")";

        
        String regGeoColumn = "EXECUTE PROCEDURE ST_REG_GEOM_COLUMN(schema=NULL,tablename='md_tf_info', geoColumn='tf_envelope', gtype=3, dimension=2,dtype=1, srid=4214)";
        try {
            Sql drop = Sqls.create(dropTalbe);
            Sql createMdInfo = Sqls.create(md_tf_info);
            Sql createFIle = Sqls.create(md_file);
//            Sql createIndex = Sqls.create(index);
            Sql createView = Sqls.create(view);
            Sql createSync  = Sqls.create(sync);
            Sql createSeq = Sqls.create(cseq);
            Sql reg = Sqls.create(regGeoColumn);
            if (overwrite) {
                //采用覆盖模式，就会删除原有的表
                dao.execute(drop, createSeq, createMdInfo, createFIle, createView,createSync, reg);
            } else {
                if (checkTableName("md_tf_info")) {
                    return true;
                }
                //采用append模式，就会保留原来的表
                dao.execute(createSeq, createMdInfo, createFIle, createView,createSync, reg);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 测试是否存在指定的表名称
     *
     * @param dao
     * @param name
     * @return
     */
    public boolean checkTableName(String name) {

        Record record = dao.fetch("iitables", Cnd.where("table_name", "=", name.toLowerCase()));
        if (record != null && !record.isEmpty()) {

            return true;
        }

        return false;
    }

    /**
     * 插入GJB数据的元数据信息
     *
     * @param info
     */
    public void insertMD_TF_INFO(GJBInfo info) {

        String ins = "insert into md_tf_info(tf_id,tf_name,tf_scale,tf_scale_name,tf_envelope,tf_sms,tf_updatetime) values (@tf_id,@tf_name,@tf_scale,@tf_scale_name,st_geomfromtext(@tf_envelope,4214),@tf_sms,@tf_updatetime)";
        Sql insSql = Sqls.create(ins);
//        Sql setlevel = Sqls.create("set lockmode session where level=TABLE");
//        dao.execute(setlevel);
        try {
            FileInputStream fis = new FileInputStream(info.getTf_sms());
            insSql.params().set("tf_id", info.getTf_id());
            insSql.params().set("tf_name", info.getTf_name());
            insSql.params().set("tf_scale", info.getTf_scale());
            insSql.params().set("tf_scale_name", info.getTf_scale_name());
            insSql.params().set("tf_envelope", info.getTf_envelope());
            insSql.params().set("tf_sms", fis);
            insSql.params().set("tf_updatetime", new Timestamp(new Date().getTime()));
            dao.execute(insSql);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GJBInfoTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * 插入GJB数据的元数据信息
     *
     * @param info
     */
    public void insertMD_TF_INFOWithCompress(GJBInfo info) {

        String ins = "insert into md_tf_info(tf_id,tf_name,tf_scale,tf_scale_name,tf_envelope,tf_sms,tf_updatetime) values (@tf_id,@tf_name,@tf_scale,@tf_scale_name,st_geomfromtext(@tf_envelope,4214),@tf_sms,@tf_updatetime)";
        Sql insSql = Sqls.create(ins);
//        Sql setlevel = Sqls.create("set lockmode session where level=TABLE");
//        dao.execute(setlevel);
        try {
//            FileInputStream fis = new FileInputStream(info.getTf_sms());
            insSql.params().set("tf_id", info.getTf_id());
            insSql.params().set("tf_name", info.getTf_name());
            insSql.params().set("tf_scale", info.getTf_scale());
            insSql.params().set("tf_scale_name", info.getTf_scale_name());
            insSql.params().set("tf_envelope", info.getTf_envelope());
            insSql.params().set("tf_sms", ZipCompressor.compress(info.getTf_sms(), 1));
            insSql.params().set("tf_updatetime", new Timestamp(new Date().getTime()));
            dao.execute(insSql);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(GJBInfoTable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GJBInfoTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 导出sms文件
     *
     * @param tf_id 图幅ID
     * @param name 图幅名称
     * @param path 文件保存路径
     * @throws FileNotFoundException 文件未找到
     * @throws SQLException sql执行异常。
     */
    public void exportSMSFile(int tf_id, String name, String path) throws FileNotFoundException, SQLException {
        String select = "select tf_sms from md_tf_info where tf_id=" + tf_id;
        Sql sql = Sqls.create(select);
        sql.setCallback(new SqlCallback() {
            @Override
            public Object invoke(Connection cnctn, ResultSet rs, Sql sql) throws SQLException {
                byte[] sms = null;
                if (rs.next()) {
                    sms = rs.getBytes("tf_sms");
                }
                return sms;  //返回二进制结果
            }
        });
        dao.execute(sql);
        File file = new File(path + File.separator + name); //创建输出文件
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        byte[] smsbytes = (byte[]) sql.getResult();
        //写入文件
        try {
            out.write(smsbytes);
            out.close();

        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,"导出sms文件", ex);
        }
    }

    /**
     * 解析 sms 文件，读取比例尺，图幅号等信息
     *
     * @param file
     * @throws IOException
     */
    public GJBInfo parseSMS(File file) throws IOException {
        GJBInfo info = new GJBInfo();

        try {

            FileInputStream finput = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(finput, "GBK");
            BufferedReader in = new BufferedReader(reader);
            String line = null;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.contains(" 地图比例尺分母")) {
                    String[] ss = line.split(" ");
                    String res = ss[ss.length - 1];
                    setScale(Integer.parseInt(res));  //设置比例尺
                } else if (line.contains("图号")) {
//                    String tf_id = line.replaceAll("图号", "").replaceAll(" ", "");
//                    String res_id = tf_id.substring(1, tf_id.length());
                    String[] ss = line.split(" ");
                    String res_id = ss[ss.length - 1];
                    if (!res_id.contains("原图") && !res_id.contains("NULL")) {
                        String tf_id = res_id;
                      //  setTf_id(Integer.parseInt(tf_id)); //设置图号
                    }
                } else if (line.contains("图廓角点经度范围")) {
                    String[] ss = line.split(" ");
                    String resp = ss[ss.length - 1];
           
                    changeLon(resp);

                } else if (line.contains("图廓角点纬度范围")) {
                    String[] ss = line.split(" ");
                    String resp = ss[ss.length - 1];
             
                    changeLat(resp);
                } else if (line.contains("图幅左下角横坐标")) {
                    String[] ss = line.split(" ");
                    String resp = ss[ss.length - 1];
                    setXld(Double.parseDouble(resp));
                } else if (line.contains("图幅左下角纵坐标")) {
                    String[] ss = line.split(" ");
                    String resp = ss[ss.length - 1];
                    setYld(Double.parseDouble(resp));
                } else if (line.contains("图幅右下角横坐标")) {
                    String[] ss = line.split(" ");
                    String resp = ss[ss.length - 1];
                    setXrd(Double.parseDouble(resp));
                } else if (line.contains("图幅右下角纵坐标")) {
                    String[] ss = line.split(" ");
                    String resp = ss[ss.length - 1];
                    setYrd(Double.parseDouble(resp));
                } else if (line.contains("图幅右上角横坐标")) {
                    String[] ss = line.split(" ");
                    String resp = ss[ss.length - 1];
                    setXru(Double.parseDouble(resp));
                } else if (line.contains("图幅右上角纵坐标")) {
                    String[] ss = line.split(" ");
                    String resp = ss[ss.length - 1];
                    setYru(Double.parseDouble(resp));
                } else if (line.contains("图幅左上角横坐标")) {
                    String[] ss = line.split(" ");
                    String resp = ss[ss.length - 1];
                    setXlu(Double.parseDouble(resp));
                } else if (line.contains("图幅左上角纵坐标")) {
                    String[] ss = line.split(" ");
                    String resp = ss[ss.length - 1];
                    setYlu(Double.parseDouble(resp));
                }
            }
            //大于十万比例尺的数据用经纬度，其他的用脚点坐标
//            if(getScale()>=100000){
//                String polygon  = "polygon(("+Xmin+" "+Ymin+"," + Xmax+" "+Ymin+","+Xmax+" "+Ymax+","+Xmin+" "+Ymax+","+Xmin+" "+Ymin+"))";
//                setEnvelope(polygon);
//            }else{
//                 String env = "polygon((" + getXld() + " " + getYld() + "," + getXrd() + " " + getYrd() + "," + getXlu() + " " + getYlu() + "," + getXru() + " " + getYru() +","+getXld() + " " + getYld() + "))";
//                 setEnvelope(env);  //设置范围
//            }

            //统一采用经纬度坐标
            String polygon = "polygon((" + Xmin + " " + Ymin + "," + Xmax + " " + Ymin + "," + Xmax + " " + Ymax + "," + Xmin + " " + Ymax + "," + Xmin + " " + Ymin + "))";
            setEnvelope(polygon);

            info.setTf_sms(file);
            info.setTf_envelope(getEnvelope());
            info.setTf_scale(getScale());
            info.setTf_name(file.getParentFile().getName());



        } catch (FileNotFoundException ex) {
            m_Logger.log(Level.SEVERE, "解析 sms 文件", ex);
        }
        return info;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getTf_id() {
        return tf_id;
    }

    public void setTf_id(int tf_id) {
        this.tf_id = tf_id;
    }

    public String getEnvelope() {
        return envelope;
    }

    public void setEnvelope(String envelope) {
        this.envelope = envelope;
    }

    public NutDao getDao() {
        return dao;
    }

    public void setDao(NutDao dao) {
        this.dao = dao;
    }

    public double getXmax() {
        return Xmax;
    }

    public void setXmax(double Xmax) {
        this.Xmax = Xmax;
    }

    public double getYmax() {
        return Ymax;
    }

    public void setYmax(double Ymax) {
        this.Ymax = Ymax;
    }

    public double getXmin() {
        return Xmin;
    }

    public void setXmin(double Xmin) {
        this.Xmin = Xmin;
    }

    public double getYmin() {
        return Ymin;
    }

    public void setYmin(double Ymin) {
        this.Ymin = Ymin;
    }

    public double getXld() {
        return xld;
    }

    public void setXld(double xld) {
        this.xld = xld;
    }

    public double getXlu() {
        return xlu;
    }

    public void setXlu(double xlu) {
        this.xlu = xlu;
    }

    public double getYld() {
        return yld;
    }

    public void setYld(double yld) {
        this.yld = yld;
    }

    public double getYlu() {
        return ylu;
    }

    public void setYlu(double ylu) {
        this.ylu = ylu;
    }

    public double getXrd() {
        return xrd;
    }

    public void setXrd(double xrd) {
        this.xrd = xrd;
    }

    public double getXru() {
        return xru;
    }

    public void setXru(double xru) {
        this.xru = xru;
    }

    public double getYrd() {
        return yrd;
    }

    public void setYrd(double yrd) {
        this.yrd = yrd;
    }

    public double getYru() {
        return yru;
    }

    public void setYru(double yru) {
        this.yru = yru;
    }


    /**
     * 在数据库中存储 gjb原始文件
     *
     * @param tf_id 图幅ID
     * @param tf_name 图幅名称（即目录名称）
     * @param path 图层所在的路径
     */
    public void insertMapFiles(int tf_id, String tf_name, String path) {
        File file = new File(path);
        File[] files = new File[1];
        if (file != null && file.isDirectory()) {
            
            //设置行级锁
            Sql setlevel = Sqls.create("set lockmode session where level=ROW");
            dao.execute(setlevel);
            files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                FileInputStream fis = null;
                try {
                    String name = files[i].getName();
                    String typeName = name.substring(name.lastIndexOf(".") + 1, name.lastIndexOf(".") + 2);
//                    System.out.println(typeName);
                    //TODO 如何通过文件来判断 file_layer_id ?
                    int file_layer_id = getTypeCode(typeName);
                    fis = new FileInputStream(files[i]);

                    String ins = "insert into md_file(file_tf_id,file_layer_id,file_name,file) values (@tf_id,@file_layer_id,@file_name,@file)";
                    Sql insSql = Sqls.create(ins);
                    insSql.params().set("tf_id", tf_id);
                    insSql.params().set("file_layer_id", file_layer_id);
                    insSql.params().set("file_name", name);
                    insSql.params().set("file", fis);
                    dao.execute(insSql);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GJBInfoTable.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        fis.close();
                    } catch (IOException ex) {
                        Logger.getLogger(GJBInfoTable.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
//          Sql setlevel = Sqls.create("set lockmode session where level=MVCC");
//          dao.execute(setlevel);

    }
    /**
     * 在数据库中存储 gjb原始文件
     *
     * @param tf_id 图幅ID
     * @param tf_name 图幅名称（即目录名称）
     * @param path 图层所在的路径
     */
    public void insertMapFilesWithCompress(int tf_id, String tf_name, String path) {
        File file = new File(path);
        File[] files = new File[1];
        if (file != null && file.isDirectory()) {
            
            //设置行级锁
            Sql setlevel = Sqls.create("set lockmode session where level=ROW");
            dao.execute(setlevel);
            files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
//                FileInputStream fis = null;
                try {
                    String name = files[i].getName();
                    String typeName = name.substring(name.lastIndexOf(".") + 1, name.lastIndexOf(".") + 2);
//                    System.out.println(typeName);
                    //TODO 如何通过文件来判断 file_layer_id ?
                    int file_layer_id = getTypeCode(typeName);
//                    fis = new FileInputStream(files[i]);

                    String ins = "insert into md_file(file_tf_id,file_layer_id,file_name,file) values (@tf_id,@file_layer_id,@file_name,@file)";
                    Sql insSql = Sqls.create(ins);
                    insSql.params().set("tf_id", tf_id);
                    insSql.params().set("file_layer_id", file_layer_id);
                    insSql.params().set("file_name", name);
                    //压缩后入库，压缩等级 1
                    insSql.params().set("file", ZipCompressor.compress(files[i], 1));
//                    insSql.params().set("file", fis);
                    dao.execute(insSql);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GJBInfoTable.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GJBInfoTable.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }

        }

    }

    /**
     * 导出指定sql条件的所有文件到指定的目录中。
     *
     * @param path 保存文件的目录路径
     * @param sql SQL 条件语句
     */
    public void exportMapFiles(final String path, String sql) {
        File dist = new File(path);
        if (dist.exists() && dist.isDirectory()) {
            String ins = "select * from md_file where " + sql;
            Sql selectSql = Sqls.create(ins);
            selectSql.setCallback(new SqlCallback() {
                @Override
                public Object invoke(Connection cnctn, ResultSet rs, Sql sql) throws SQLException {
                    while (rs.next()) {
                        String name = rs.getString("file_name");
                        byte[] fileObj = rs.getBytes("file");
                        try {
                            File file = new File(path + File.separator + name); //创建输出文件
                            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));

                            //写入文件
                            out.write(fileObj);
                            out.close();
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(GJBInfoTable.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(GJBInfoTable.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    return "";
                }
            });

            dao.execute(selectSql);
        }
    }

}
