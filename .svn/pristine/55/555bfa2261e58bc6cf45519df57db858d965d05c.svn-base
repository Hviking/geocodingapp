/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.thread;

import com.beyondb.datasource.BydDataSource;
import com.beyondb.datasource.BydOperator;
import com.beyondb.datasource.DataSourceUtils;

/**
 *数据入库
 * @author lbs
 */
public  class ThreadImportIntoDB extends NewThread {

    protected BydOperator m_bydOperator;

    protected boolean m_FirstImport;

    public void setBydDataSource(BydDataSource dataSource) {
        m_bydOperator = new BydOperator(new DataSourceUtils(dataSource));
    }

    public void setFirstImport(boolean firstImport) {
        m_FirstImport = firstImport;
    }

 

    
}
