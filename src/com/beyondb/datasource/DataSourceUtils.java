/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.datasource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *数据源操作类
 * @author 倪永 
 */
public final class DataSourceUtils {

    public Connection con;
    private static final Logger m_Logger= Logger.getLogger( DataSourceUtils.class.getName());
    private  DataSource bydsource;

    public DataSourceUtils()
    {
        
    }
    public  DataSourceUtils(DataSource source)
    {
        init(source);
    }
    
    public DataSource getDataSource()
    {
        return  (bydsource==null?null:bydsource);
    }
    public void init(DataSource source) {
        try {
            //加载驱动类   
            Class.forName(source.getDriver());
        } catch (ClassNotFoundException e) {
            m_Logger.log(Level.SEVERE, "加载驱动程序出错",e);
        }
        this.bydsource = source;
    }
    
    public boolean openConnection() throws  SQLException
    {
        boolean isOpen = false;
        try {

            con = DriverManager.getConnection(bydsource.getUrl(), bydsource.getName(), bydsource.getPassword());
            if (con.isClosed()) {
               m_Logger.log(Level.SEVERE, bydsource.getUrl() + "数据库连接打开失败！");
            } else {
                isOpen = true;
            }
        } catch (SQLException se) {
            m_Logger.log(Level.SEVERE, bydsource.getUrl() + "数据库连接失败！", se);
            throw se;
        }
        return isOpen;

    }
    public void closeConnection() { // 关闭连接对象
        try {
        
                if (con != null&&
                        con.isClosed() != true) {
                    con.close();
                }
            
        } catch (SQLException e) {
             m_Logger.log(Level.SEVERE, bydsource.getUrl()+ "数据库关闭连接失败！",e);
        }
    }

    /**
     * 执行静态SQL语句。通常通过Statement实例实现。
     *
     * @return
     */
    public Statement createStatement() {
        Statement stmt = null;
        try {
            if (con != null && con.isClosed() != true) {
                stmt = con.createStatement();
            } else {
                 m_Logger.log(Level.SEVERE, bydsource.getUrl() + "数据库连接失败");
            }
        } catch (SQLException ex) {
             m_Logger.log(Level.SEVERE, bydsource.getUrl() + "创建Statement失败！", ex);
        }
        return stmt;
    }

    public void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                m_Logger.log(Level.SEVERE, bydsource.getUrl()+"数据库关闭Statement失败！",e);
            }
        }
    }

    /**
     * 执行动态SQL语句。通常通过PreparedStatement实例实现。
     *
     * @param sql
     * @return
     */
    public PreparedStatement createPreparedStatement(String sql) {
        PreparedStatement pstmt = null;
        try {
            if (con!=null&&con.isClosed() != true) {
                pstmt = con.prepareStatement(sql);
            }
            else
            {
                 m_Logger.log(Level.SEVERE,  bydsource.getUrl()+"数据库连接失败");
            }
        } catch (SQLException ex) {
             m_Logger.log(Level.SEVERE,  bydsource.getUrl()+"创建preparestatment", ex);
        }

        return pstmt;
    }

    public void closePrepareStatement(PreparedStatement preparedStmt) {
        if (preparedStmt != null) {
            try {
                preparedStmt.close();
            } catch (SQLException e) {
                 m_Logger.log(Level.SEVERE, bydsource.getUrl()+ "数据库关闭PrepareStatement失败！",e);
            }
        }
    }

    /**
     * 执行数据库存储过程。通常通过CallableStatement实例实现。
     *
     * @param prepareCall 例如{CALL demoSp(? , ?)}
     * @return
     */
    public CallableStatement createCallableStatement(String prepareCall) {
        CallableStatement cstmt = null;
        try {
            if (con != null && con.isClosed() != true) {
                cstmt = con.prepareCall(prepareCall);
            } else {
                 m_Logger.log(Level.SEVERE, bydsource.getUrl() + "数据库连接失败");
            }
        } catch (SQLException ex) {
             m_Logger.log(Level.SEVERE, bydsource.getUrl() + "创建CallableStatement失败！", ex);
        }
        return cstmt;

    }

    public void closeCallableStatement(CallableStatement callableStmt) {
        if (callableStmt != null) {
            try {
                callableStmt.close();
            } catch (SQLException e) {
                 m_Logger.log(Level.SEVERE, bydsource.getUrl()+"数据库关闭CallableStatement失败！",e);
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            String username = "sry";
            String password = "sry";
            String jdbcURL = "jdbc:beyondb://192.168.1.127:II7/beijingpoidb;auto=multi;ENCODE=gbk";
            
            
            
            DataSource db = new DataSource(jdbcURL, username, password);
            
            String instance = db.getInstance();
            String ip = db.getIp();
            DataSourceUtils DsUtils = new DataSourceUtils(db);
            boolean openConnection = DsUtils.openConnection();
            if (openConnection) {
                System.out.println("连接成功");
            }
            DsUtils.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DataSourceUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        

    }
}

