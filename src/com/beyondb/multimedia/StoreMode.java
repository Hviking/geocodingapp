/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.multimedia;

/**
 *多媒体数据的存储模式
 * @author lbs
 */
public class StoreMode {
   
    public MediaMode mediamode;
        /**
     *指示是否分块,默认值 是true
     */
    public boolean blocking=true;

    /**
     *参数值格式为n[k | m | g]，
     * 其中，n表示分块大小数值部分
     * 、k,m,g表示分块大小单位，为可选项，
     * 没有k,m,g时单位为byte，默认值为5M。
     */
    public String blocksize="5M";
    /**
     *创建时使用XML方案的序号，方案必须已经导入到数据库中。默认是1
     */
    public int metaschemaId=1;
    
    /**
     *是否有外部数据源，只对gateway有效
     * 该参数仅在mediamode为gateway时支持，并且必须指定。默认值为true。
     */
    public boolean Linkexist=true;
    
      /**
     *inline模式是B表
     * gateway模式是文件路径
     */
    public String tableBName="";

    public MediaFormat fileformat;
    /**
     * 比特率
     */
    public int bitRate=-1;
    /**
     * 对象帧速率
     */
    public int frameRate=-1;
    /**
     * 对象帧分辨率
     */
    public int frameResolution=-1;
}

