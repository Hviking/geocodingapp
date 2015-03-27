/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.raster;

    /**
 * 栅格模式的枚举
 * @author beyondb
 */
public enum  RasterMode
{
    /**
     *栅格属性表 + 栅格分块表
     */
    inline,
    /**
     *栅格属性表 + 栅格分块树
     */
    outline,
    /**
     *栅格属性表 + 外部栅格数据源。
     */
    gateway
}
