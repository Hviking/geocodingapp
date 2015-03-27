/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.raster;

/**
 * 栅格模式选项包含三个可选值，分别为：inline 、outline 和gateway
 *
 * @author beyondb
 */
public class StoreMode {
    
    /**
     *导入栅格数据的SRID。
     */
    public String SRID;

    public  StoreMode(){
      this.SRID = "";
      pyd_level =0;
      rastersize="(0,0,0)";
      blocking = true;
      blocksize ="(256,256,1)";
      ulccoord="(0,0,0)";
      brpcoord = "(0,0,0)";
      metaschemaId=1;
      cellType = Celltype.BB1;
      comp= Compression.JPEG;
      quality=75;
      formatt = RasterFormat.GTiff;
      inter =  Interleaving.BSQ;
      mode = RasterMode.outline;
      
    }
    

    /**
     * 金字塔层级，默认0是不创建金字塔
     */
    public  int pyd_level=0;
    /**
     *inline模式是B表
     * outline模式是B树
     * gateway模式是文件路径
     */
    public String tableBName;

    /**
     *压缩质量
     */
    public int quality;

    /**
     *栅格的宽、高、波段,默认(0,0,0)
     */
    public String rastersize;
   
    /**
     * 读取栅格宽
     * @return 
     */
    public  int getRasterXSize()
    {
        String value =rastersize.substring(1,rastersize.indexOf(","));
        return  Integer.parseInt(value);
    }
    
    /**
     * 读取栅格长
     * @return 
     */
    public  int getRasterYSize()
    {
        String value =rastersize.split(",")[1];
        return  Integer.parseInt(value);
    }
    
    /**
     * 读取栅格波段数
     * @return 
     */
    public  int getRasterBand()
    {
         String tmpSize=rastersize.replace("(", "").replace(")", "");
        String value =tmpSize.split(",")[2];
        return  Integer.parseInt(value);
    }
    
       /**
     * 读取分块宽
     * @return 
     */
    public  int getBlockXSize()
    {
        String value =blocksize.substring(1,blocksize.indexOf(","));
        return  Integer.parseInt(value);
    }
    
    /**
     * 读取分块长
     * @return 
     */
    public  int getBlockYSize()
    {
        String value =blocksize.split(",")[1];
        return  Integer.parseInt(value);
    }
    
    /**
     * 读取分块波段
     * @return 
     */
    public  int getBlockBand()
    {
        String tmpBlockSize=blocksize.replace("(", "").replace(")", "");
        String value =tmpBlockSize.split(",")[2];
        return  Integer.parseInt(value);
    }
    
    /**
     * 是否开启变换
     */
    public  boolean  transform;
    
    
    /**
     *指示是否分块
     */
    public boolean blocking;

    /**
     *分块的宽、高、波段(bh,bw,bb)  默认(256,256,1)
     */
    public String blocksize;

    /**
     *ULC坐标值，默认(0,0,0)
     */
    public String ulccoord;

         /**
     * 读取ULC宽
     * @return 
     */
    public  int getULCRow()
    {
        String value =ulccoord.substring(1,ulccoord.indexOf(","));
        return  Integer.parseInt(value);
    }
    
    /**
     * 读取ULC列
     * @return 
     */
    public  int getULCColumn()
    {
        String value =ulccoord.split(",")[1];
        return  Integer.parseInt(value);
    }
    
    /**
     * 读取ULC波段
     * @return 
     */
    public  int getULCBand()
    {
          String tmpSize=ulccoord.replace("(", "").replace(")", "");
        String value =tmpSize.split(",")[2];
        return  Integer.parseInt(value);
    }
    
  
    /**
     * BRP坐标值 默认(0,0,0)
     */
    public String brpcoord;
    
    
          /**
     * 读取BRP行
     * @return 
     */
    public  int getBRPRow()
    {
        String value =brpcoord.substring(1,brpcoord.indexOf(","));
        return  Integer.parseInt(value);
    }
    
    /**
     * 读取BRP列
     * @return 
     */
    public  int getBRPColumn()
    {
        String value =brpcoord.split(",")[1];
        return  Integer.parseInt(value);
    }
    
    /**
     * 读取BRP波段
     * @return 
     */
    public  int getBRPBand()
    {
        String tmpSize = rastersize.replace("(", "").replace(")", "");
        String value = tmpSize.split(",")[2];
        return Integer.parseInt(value);
    }
    
    private String storage_opt;

    /**
     * 栅格模式
     */
    public RasterMode mode = RasterMode.outline;

    /**
     *创建时使用XML方案的序号，默认是1
     */
    public int metaschemaId;

    /**
     *设置像元类型
     */
    public Celltype cellType;
    /**
     *是否有外部数据源，只对gateway有效
     */
    public boolean linexistB;
    /**
     *压缩格式
     */
    public  Compression comp;
    

    /**
     *设置像元交错类型
     */
    public Interleaving inter;
 
    /**
     *   
     仅在
     rastermode 为
     gateway、
     linkexist 为
     false 时支持。
     为创建外部的
     数据源格式
     */
    public RasterFormat formatt;  

  

    /**
     * 创建Raster对象
     *
     * @param mode
     * @return  ST_Create()对象的字符串表达式
     */
    public String  creatRasterStorageOpt() {
        switch (mode) {
            case inline:
                createRasterObj4Inline();
                break;
            case outline:
                createRasterObj4Outline();
                break;
            case gateway:
                createRasterObj4Gateway();
                break;
        }
        
        return storage_opt ;

    }

    public  static String getCellType(Celltype cellType)
    {
        String type ="";
        if (cellType == Celltype.BB1) {
            type += "1BB";
        } else if (cellType == Celltype.BUI2) {
            type += "2BUI";
        } else if (cellType == Celltype.BUI4) {
            type += "4BUI";
        } else if (cellType == Celltype.BUI8) {
            type += "8BUI";
        } else if (cellType == Celltype.BSI8) {
            type += "8BSI";
        } else if (cellType == Celltype.BSI16) {
            type += "16BSI";
        } else if (cellType == Celltype.BSI32) {
            type += "32BSI";
        } else if (cellType == Celltype.BUI16) {
            type += "16BUI";
        } else if (cellType == Celltype.BUI32) {
            type += "32BUI";
        } else if (cellType == Celltype.BF32) {
            type += "32BF";
        } else if (cellType == Celltype.BF64) {
            type += "64BF";
        }
        return  type;
    }
    /*
     * 创建inline对象
     */
    private void createRasterObj4Inline() {

        storage_opt = "rastermode=inline;rastersize=" + rastersize + ";blocking=" + blocking + ";blocksize=" + blocksize
                + ";ulccoord=" + ulccoord + ";brpcoord=" + brpcoord
                + ";compression=" + comp.name() + ";quality="+quality+";"
                + "interleaving=" + inter.name() + ";metaschemaid=" + metaschemaId ;
        if(cellType !=null)
            storage_opt += ";celltype=" + getCellType(cellType);

        
    }

    private void createRasterObj4Outline() {
        storage_opt = "rastermode=outline;rastersize=" + rastersize + ";blocking=" + blocking + ";blocksize=" + blocksize
                + ";ulccoord=" + ulccoord + ";brpcoord=" + brpcoord
                + ";compression=" + comp.name() + ";quality="+quality+";"
                + "interleaving=" + inter.name() + ";metaschemaid=" + metaschemaId ;
 if(cellType !=null)
            storage_opt += ";celltype=" + getCellType(cellType);
    }

    private void createRasterObj4Gateway() {
        storage_opt = "rastermode=gateway;rastersize=" + rastersize + ";blocking=" + blocking + ";blocksize=" + blocksize
                + ";ulccoord=" + ulccoord + ";brpcoord=" + brpcoord
                + ";compression=" + comp.name() + ";quality="+quality+";"
                + "interleaving=" + inter.name() + ";metaschemaid=" + metaschemaId;
         if(cellType !=null)
           storage_opt += ";celltype=" + getCellType(cellType);
         
              storage_opt += ";linkexist=" + linexistB;
        if (!linexistB) {
            storage_opt += ";format=" + formatt.name();
        }
    }






    

}
