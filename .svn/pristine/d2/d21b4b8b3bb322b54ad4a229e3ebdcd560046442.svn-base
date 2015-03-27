package com.beyondb.utils;

/**
 * 计时工具
 * @author beyondb
 */
public class TimeUtil {
    
        /*
     * 获取格式化后的总时间
     * @param times 总时间 单位：毫秒
     * @return 格式化后的总时间字符串
     */
    public static String getCalFormatTime(long times) {

        long ms = times % 1000;
        long ss = (times / 1000) % 60;
        long mm = (times / (1000 * 60)) % 60;
        long hh = times / (1000 * 60 * 60) % 60;
        long dd = times / (24 * 60 * 60 * 60 * 1000);

        return dd + " 天 " + hh + " 小时 " + mm + " 分 " + ss + " 秒 " + ms + " 毫秒";

    }

    /**
     * 获取两个时间差，单位采用微秒
     * @param start  开始时间
     * @param end  结束时间
     * @return 结果时间
     */
    public static long getResultTime(long start,long end){
        
        long result = end - start;
        return result;
        
    }
    
}
