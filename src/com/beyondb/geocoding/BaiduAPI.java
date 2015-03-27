/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.geocoding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 *
 * @author ZhangShuo
 */
public class BaiduAPI implements  I_GeoCodeAPI{

    private static BaiduAPI baiduAPI;
    private static final String ak = "d9l3TIC7cWs7vtd88A07dro7";
    private static final String baiduURL4GeoCodingAPI ="http://api.map.baidu.com/geocoder/v2/?ak=%s&output=json&address=%s";
    private static final String baiduURL4PlaceAPI ="http://api.map.baidu.com/place/v2/search?ak=%s&scope=1&output=json&query=%s";
    
    public static Map<String, String> testPost(String x, String y) throws IOException {
        URL url = new URL("http://api.map.baidu.com/geocoder?" + ak + "=您的密钥"
                + "&callback=renderReverse&location=" + x
                + "," + y + "&output=json");
        URLConnection connection = url.openConnection();
        /**
         * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
         * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
         */
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection
                .getOutputStream(), "utf-8");
//        remember to clean up
        out.flush();
        out.close();
//        一旦发送成功，用以下方法就可以得到服务器的回应：
        String res;
        InputStream l_urlStream;
        l_urlStream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                l_urlStream, "UTF-8"));
        StringBuilder sb = new StringBuilder("");
        while ((res = in.readLine()) != null) {
            sb.append(res.trim());
        }
        String str = sb.toString();
        System.out.println(str);
        Map<String, String> map = null;
        if (StringUtils.isNotEmpty(str)) {
            int addStart = str.indexOf("formatted_address\":");
            int addEnd = str.indexOf("\",\"business");
            if (addStart > 0 && addEnd > 0) {
                String address = str.substring(addStart + 20, addEnd);
                map = new HashMap<String, String>();
                map.put("address", address);
                return map;
            }
        }

        return null;

    }

    public static String getPointByAddress(String address, String city) throws IOException {
        String resultPoint = "";
        try {
            URL url = new URL("http://api.map.baidu.com/geocoder/v2/?ak=" + ak
                    + "&output=xml&address=" + address
                    + "&" + city);

            URLConnection connection = url.openConnection();
            /**
             * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
             * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
             */
            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection
                    .getOutputStream(), "utf-8");
//        remember to clean up
            out.flush();
            out.close();
//        一旦发送成功，用以下方法就可以得到服务器的回应：

            String res;
            InputStream l_urlStream;
            l_urlStream = connection.getInputStream();
            if (l_urlStream != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        l_urlStream, "UTF-8"));
                StringBuilder sb = new StringBuilder("");
                while ((res = in.readLine()) != null) {
                    sb.append(res.trim());
                }
                String str = sb.toString();
                System.out.println(str);
                Document doc = DocumentHelper.parseText(str); // 将字符串转为XML
                Element rootElt = doc.getRootElement(); // 获取根节点
//            System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称
                Element resultElem = rootElt.element("result");
                if (resultElem.hasContent()) {
                    Element locationElem = resultElem.element("location");
                    Element latElem = locationElem.element("lat");
//            System.out.print("lat:"+latElem.getTextTrim()+",");
                    Element lngElem = locationElem.element("lng");
//            System.out.println("lng:"+lngElem.getTextTrim());
                    resultPoint = lngElem.getTextTrim() + "," + latElem.getTextTrim();
                } else {
                    System.out.println("can't compute the coor");
                    resultPoint = " , ";
                }
            } else {
                resultPoint = " , ";
            }

        } catch (DocumentException ex) {
            Logger.getLogger(BaiduAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultPoint;
    }

    /**
     * 获取经纬度坐标
     * 返回输入地址的经纬度坐标 key lng(经度),lat(纬度)
     * @param address
     * @param city
     * @return 
     */
    public static Map<String, String> getLongLatCoordinate(String address,String city) {
        Map<String, String> map = new HashMap<>();
        try {
//   将地址转换成utf-8的16进制 
            address = URLEncoder.encode(address, "UTF-8");
//   如果有代理，要设置代理，没代理可注释 
//  System.setProperty("http.proxyHost","192.168.172.23"); 
//  System.setProperty("http.proxyPort","3209"); 
            URL resjson = new URL("http://api.map.baidu.com/geocoder/v2/?ak=" + ak
                    + "&output=json&address=" + address
                    + "&"+city);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(resjson.openStream()));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            in.close();
            String str = sb.toString();
            System.out.println("return json:" + str);           
            if (str.contains("lng")) {
                int lngStart = str.indexOf("lng\":");
                int lngEnd = str.indexOf(",\"lat");
                int latEnd = str.indexOf("},\"precise");
                if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
                    String lng = str.substring(lngStart + 5, lngEnd);
                    String lat = str.substring(lngEnd + 7, latEnd);                    
                    map.put("lng", lng);
                    map.put("lat", lat);                  
                }
            }  
            else{                
                map.put("lng", " ");
                map.put("lat", " ");
                 Logger.getLogger(BaiduAPI.class.getName()).log(Level.WARNING,"Parse coordinate from ‘"+str+"’ is empty!");
                System.out.println("lng,lat is empty!"+str+"--"+map.get("lng")+","+map.get("lat"));
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(BaiduAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BaiduAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }

    public static void main(String[] args) throws IOException {
//    	Map<String, String> json = BaiduAPI.testPost("29.542938", "114.064022");
//        System.out.println("address :" + json.get("address"));
//        String str = BaiduAPI.getPointByAddress("宝山区祁连山路2400号上海巨邦物流有限公司", "上海市");
//        System.out.println(str);
//        Map<String, String> map = BaiduAPI.getLongLatCoordinate("北京市丰台区郑常庄310号北京天达汽车修理有限公司","北京市");
//        if (null != map) {
//            System.out.println(map.get("lng"));
//            System.out.println(map.get("lat"));
//        }
       
        //Coordinate coord =BaiduAPI.getInstance().getCoordinate("北京市丰台区郑常庄310号北京天达汽车修理有限公司","");
          Coordinate coord =BaiduAPI.getInstance().getCoordinate("北京市丰台区郑常庄310号北京天达汽车修理有限公司","上海市");
         if (null != coord) {
            System.out.println("lat:" + coord.Latitude);
            System.out.println("lng:" + coord.Longitude);
        }
        Map<String, Coordinate> places = BaiduAPI.getInstance().getPlaces("光明路停车场", "北京市");
        if (places != null) {
            for (Map.Entry<String, Coordinate> entry : places.entrySet()) {
                String placeName = entry.getKey();
                Coordinate coordinate = entry.getValue();
                System.out.println("placeName:" + placeName);
                System.out.println("lat:" + coordinate.Latitude);
                System.out.println("lng:" + coordinate.Longitude);
            }
        }
    }

    public static synchronized BaiduAPI getInstance() {      
        if (baiduAPI == null) {
            baiduAPI = new BaiduAPI();
        }
        return baiduAPI;
    }
    
    @Override
    public Coordinate getCoordinate(String address, String city) {
        Coordinate coord = null;
        
        try {
            if (address == null || address.trim().length() == 0) {

                Logger.getLogger(BaiduAPI.class.getName())
                        .log(Level.SEVERE, "没有具体地址，无法解析 !");
                return null;
            }
                        
            //   对于address字段可能会出现中文或其它一些特殊字符（如：空格），
            //对于类似的字符要进行编码处理，编码成 UTF-8 字符的二字符十六进制值
            String encodeAddress = URLEncoder.encode(address, "UTF-8");
           
             //city = URLEncoder.encode(city, "UTF-8");

        
            String queryUrl="";
            if (city.isEmpty()) {
                queryUrl = String.format(baiduURL4GeoCodingAPI, ak, encodeAddress);
            } else {
                String citybaiduURL4GeoCodingAPI =baiduURL4GeoCodingAPI+ "&city=%s";
                queryUrl = String.format(citybaiduURL4GeoCodingAPI, ak, encodeAddress, city);
            }
            URL  query =  new URL(queryUrl);
            
   

            HttpURLConnection connection = (HttpURLConnection) query.openConnection();
            //取得返回包体
            InputStream in = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(in));//取得返回内容
            ;
            String line;
            StringBuilder sb = new StringBuilder("");
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line.trim());
            }
            in.close();
            
          
            String output = sb.toString();
//            System.out.println("return json:" + output);     
            if (output==null ||output.isEmpty()) {
                 Logger.getLogger(BaiduAPI.class.getName())
                        .log(Level.WARNING, "对“"+address+"”解析结果内容为空，请求失败");
                return null;
            }
            //解析json
            //格式如：
            //不带回调函数的返回值
            //{
            //	status: 0,
            //	result: 
            //	{
            //	location: 
                //{
                //lng: 116.30814954222,
                //lat: 40.056885091681
                //},
                //precise: 1,
                //confidence: 80,
                //level: "商务大厦"
                //}
               //}
            //}
            JSONObject jsonObj = JSONObject.fromObject(output);

            int status = jsonObj.getInt("status");
            if (status == 0) {
                //解析成功
                
                JSONObject result = jsonObj.getJSONObject("result");
  
                if (result.isEmpty()) {
                   String msg = jsonObj.getString("msg");
                    Logger.getLogger(BaiduAPI.class.getName())
                        .log(Level.WARNING, "\u67e5\u8be2{0} {1}", new Object[]{address, msg});
                    return null;
                }
                double lng = result.getJSONObject("location").getDouble("lng");
                double lat = result.getJSONObject("location").getDouble("lat");

                coord = new Coordinate();
                coord.Latitude = lat;
                coord.Longitude = lng;
            }
            else {
                 String msg = jsonObj.getString("msg");
                    Logger.getLogger(BaiduAPI.class.getName())
                        .log(Level.WARNING, "\u67e5\u8be2{0} {1}", new Object[]{address, msg});
                Logger.getLogger(BaiduAPI.class.getName())
                        .log(Level.WARNING, "解析结果内容为空，原因分析及可尝试方法：\n"
                                + "A:百度地址库里无此数据，本次结果为空\n"
                                + "B:加入city字段重新解析\n"
                                + "C:将过于详细或简单的地址更改至省市区县街道重新解析");
                return null;
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(BaiduAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BaiduAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return coord;
        
    }
    
    /**
     * 对于没有明确解析出地理坐标的地名
     *获取推荐的的地名和相应的坐标
     * @param address
     * @param city
     * @return
     */
    public Map<String,Coordinate> getPlaces(String address,String city)
    {
           Map<String,Coordinate> map=null;
        try {
            if (address == null || address.trim().length() == 0) {

                Logger.getLogger(BaiduAPI.class.getName())
                        .log(Level.SEVERE, "没有具体地址，无法解析 !");
                return null;
            }
                        
            // 对于address字段可能会出现中文或其它一些特殊字符（如：空格），
            //对于类似的字符要进行编码处理，编码成 UTF-8 字符的二字符十六进制值
            String encodeAddress = URLEncoder.encode(address, "UTF-8");
            String encodeCity = URLEncoder.encode(city==null?"":city, "UTF-8");

        
            String queryUrl="";
            if (city==null||city.isEmpty()) {
                queryUrl = String.format(baiduURL4PlaceAPI, ak, encodeAddress);
            } else {
               String region_baiduURL4PlaceAPI =baiduURL4PlaceAPI+ "&region=%s";
                queryUrl = String.format(region_baiduURL4PlaceAPI, ak, encodeAddress, encodeCity);
            }
            URL  query =  new URL(queryUrl);
 
            HttpURLConnection connection = (HttpURLConnection) query.openConnection();
            //取得返回包体
            InputStream in = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(in));//取得返回内容
            
            String line;
            StringBuilder sb = new StringBuilder("");
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line.trim());
            }
            in.close();
            
          
            String output = sb.toString();
            //System.out.println("return json:" + output);   
            String info ="对“"+address+"”解析结果内容为空，请求失败";
            if (output==null ||output.isEmpty()) {
                 Logger.getLogger(BaiduAPI.class.getName())
                        .log(Level.WARNING, info);
                return null;
            }
//        状态字段
//名称 	类型 	说明
//status 	Int 	本次API访问状态，如果成功返回0，如果失败返回其他数字。
//message 	string 	对API访问状态值的英文说明，如果成功返回"ok"，并返回结果字段，如果失败返回错误说明。
//total 	int 	检索总数，用户请求中设置了page_num字段才会出现total字段
            JSONObject jsonObj = JSONObject.fromObject(output);

            int status = jsonObj.getInt("status");
         
            if (status == 0) {
                //解析成功
      
               
                JSONArray results = jsonObj.getJSONArray("results");
                
                for (int i = 0; i < results.size(); i++) {
                    if (map==null) {
                         map = new HashMap<>();
                    }
                    JSONObject obj = results.getJSONObject(i);
                    String name = obj.getString("name");
                    String add = obj.getString("address");
                    double lng = obj.getJSONObject("location").getDouble("lng");
                    double lat = obj.getJSONObject("location").getDouble("lat");
                    Coordinate coord = new Coordinate();
                    coord.Latitude = lat;
                    coord.Longitude = lng;

                    map.put(name+" "+ add, coord);
                }
                //结果字段示例
//  {
//			name : "燕京书画社",
//			location : 
//			{
//				lat : 39.901442,
//				lng : 116.392169
//			},
//			address : "琉璃厂东街114号",
//			street_id : "881a28e7ba81f35ef942b248",
//			telephone : "(010)63033091",
//			uid : "881a28e7ba81f35ef942b248"
//		},
//           

            }
            else {
                 String msg = jsonObj.getString("message");
                    Logger.getLogger(BaiduAPI.class.getName())
                        .log(Level.WARNING, "\u67e5\u8be2{0} {1}", new Object[]{address, msg});
               
                return null;
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(BaiduAPI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BaiduAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return map;
    }
    @Override
    public String getApiType()
    {
        return this.getClass().getName();
    }
}
