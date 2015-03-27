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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 *天地图地名地址服务  
 * @author  倪永
 */
public class SkyMapAPI  implements  I_GeoCodeAPI{

    private String httpRequest="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
"<wfs:GetFeature maxFeatures=\"5\" service=\"WFS\" version=\"1.0.0\" xsi:schemaLocation=\"http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/wfs.xsd\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> \n" +
"	<wfs:Query typeName=\"iso19112:SI_Gazetteer\" srsName=\"EPSG:4326\">\n" +
"		<ogc:Filter>\n" +
//"			<ogc:And>\n" +
//"				<ogc:PropertyIsLike wildCard=\"*\" singleChar=\".\" escape=\"!\"> \n" +
//"					<ogc:PropertyName>name</ogc:PropertyName>\n" +
//"					<ogc:Literal/>\n" + //<ogc:Literal>***北京 超市**</ogc:Literal> "  请求的时候仅需要替换 超市 这个关键词就好,如果指定城市搜索，搜索关键词为指定城市的名称  加上空格要搜索的关键字就可以 
//"				</ogc:PropertyIsLike> \n" +
"                               <ogc:PropertyIsEqualTo>\n"+
"					<ogc:PropertyName>name</ogc:PropertyName>\n" +
"					<ogc:Literal/>\n" + //<ogc:Literal>***北京 超市**</ogc:Literal> "
"                               </ogc:PropertyIsEqualTo>\n"+
//"			</ogc:And>\n" +
"		</ogc:Filter>\n" +
"	</wfs:Query>\n" +
" </wfs:GetFeature>";
    

    private static SkyMapAPI api;

    private static String requestURL ="http://ogc.tianditu.com/wfssearch.shtml";
 
   
    public static void main(String[] args) throws IOException {

        SkyMapAPI api = new SkyMapAPI();

        Coordinate coord = api.getCoordinate("国家地震局", "");
        System.out.println("lon" + coord.Longitude);
        System.out.println("lat" + coord.Latitude);
    }

    public static synchronized SkyMapAPI getInstance() {      
        if (api == null) {
            api = new SkyMapAPI();
        }
        return api;
    }
    
    private String getRequestXML(String queryString) {
        return httpRequest.replace("<ogc:Literal/>", "<ogc:Literal>" + queryString + "</ogc:Literal>");
    }
    @Override
    public Coordinate getCoordinate(String address, String city) {
        Coordinate coord = null;
        Map<String, Coordinate> map = getPlaces(address, city);

        if (map != null) {
            Set<Map.Entry<String, Coordinate>> set = map.entrySet();
            Iterator<Map.Entry<String, Coordinate>> it = set.iterator();
            Map.Entry<String, Coordinate> entry;
            if (it.hasNext()) {
                entry = it.next();
                System.out.println("name:"+entry.getKey() );
                coord = entry.getValue();
            }
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
    public Map<String, Coordinate> getPlaces(String address, String city) {
        Map<String, Coordinate> map = null;
        try {

            URL url = new URL(requestURL);//webService服务是以XML格式通信的
            String soapRequest = getRequestXML(city == null || city.isEmpty() ? ("" + address) : (city + " " + address));

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");
            con.setRequestProperty("Content-Length", String.valueOf(soapRequest.length()));
            con.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
            // con.setRequestProperty("SOAPAction",_soap_action_str);

            con.setDoOutput(true);
            con.setConnectTimeout(3000);//3秒连接不上，自动阻塞,有效地避免了由于ip不通导致连接不上的问题。并且，这个时间只是指建立socket的时间，而并不是指发送数据以及数据传输的时间。

            con.setRequestMethod("POST");
            con.setUseCaches(false);//post请求不能使用缓存

            OutputStream out = con.getOutputStream();

            out.write(soapRequest.getBytes());
            out.flush();
            out.close();
            int code = 0;

            StringBuilder content = new StringBuilder();
            code = con.getResponseCode();    //用来获取服务器响应状态
            BufferedReader br;
            String line = "";
            if (code == HttpURLConnection.HTTP_OK) {

                br = new BufferedReader(new InputStreamReader(con.getInputStream()));

                while ((line = br.readLine()) != null) {
                    content.append(line);

                }
           //成功返回 XML格式数据
                //            <?xml version="1.0" encoding="UTF-8"?><wfs:FeatureCollection xmlns:topp="http://www.openplans.org/topp" xmlns:wfs="http://www.opengis.net/wfs" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd" service="WFS" outputFormat="gml" version="1.0">
//<gml:featureMember xmlns:iso19112="SI_Gazetteer"><iso19112:SI_Gazetteer><Geometry><gml:Point srsName="-1"><gml:coordinates>116.394272,39.900472</gml:coordinates></gml:Point></Geometry><ogc:id>1003716709</ogc:id><ogc:name>北京市政府天安门地区管委会</ogc:name><ogc:tel>010-65243322</ogc:tel><ogc:addnamel>东交民巷44号</ogc:addnamel><ogc:admin>156110101</ogc:admin><ogc:cls>100102 100100 100000</ogc:cls><ogc:level/><ogc:zip/></iso19112:SI_Gazetteer></gml:featureMember>
//<gml:featureMember xmlns:iso19112="SI_Gazetteer"><iso19112:SI_Gazetteer><Geometry><gml:Point srsName="-1"><gml:coordinates>116.394504,39.9013</gml:coordinates></gml:Point></Geometry><ogc:id>1003546134</ogc:id><ogc:name>北京市公安局天安门地区分局</ogc:name><ogc:tel>010-65241304</ogc:tel><ogc:addnamel>东交民巷37号</ogc:addnamel><ogc:admin>156110101</ogc:admin><ogc:cls>100501 100500 100000 100105 100100 100000</ogc:cls><ogc:level/><ogc:zip/></iso19112:SI_Gazetteer></gml:featureMember>
//<gml:featureMember xmlns:iso19112="SI_Gazetteer"><iso19112:SI_Gazetteer><Geometry><gml:Point srsName="-1"><gml:coordinates>116.393824,39.900068</gml:coordinates></gml:Point></Geometry><ogc:id>1025053239</ogc:id><ogc:name>北京全聚德天安门店</ogc:name><ogc:tel>010-65122265</ogc:tel><ogc:addnamel>东交民巷44号</ogc:addnamel><ogc:admin>156110101</ogc:admin><ogc:cls>010100 010000</ogc:cls><ogc:level/><ogc:zip/></iso19112:SI_Gazetteer></gml:featureMember>
//<gml:featureMember xmlns:iso19112="SI_Gazetteer"><iso19112:SI_Gazetteer><Geometry><gml:Point srsName="-1"><gml:coordinates>116.3938,39.899528</gml:coordinates></gml:Point></Geometry><ogc:id>1003552834</ogc:id><ogc:name>东来顺北京天安门店</ogc:name><ogc:tel>010-65241042</ogc:tel><ogc:addnamel>东交民巷44号院</ogc:addnamel><ogc:admin>156110101</ogc:admin><ogc:cls>010116 010100 010000</ogc:cls><ogc:level/><ogc:zip/></iso19112:SI_Gazetteer></gml:featureMember>
//	<gml:featureMember xmlns:iso19112="SI_Gazetteer">
//				<iso19112:SI_Gazetteer>
//					<Geometry>
//						<gml:Point srsName="-1">
//							<gml:coordinates>116.3938,39.899532</gml:coordinates>
//						</gml:Point>
//					</Geometry>
//					<ogc:id>1021636362</ogc:id>
//					<ogc:name>东来顺饭庄北京天安门店</ogc:name>
//					<ogc:tel>010-65241042</ogc:tel>
//					<ogc:addnamel>东交民巷44号院</ogc:addnamel>
//					<ogc:admin>156110101</ogc:admin>
//					<ogc:cls>010116 010100 010000</ogc:cls>
//					<ogc:level/>
//					<ogc:zip/>
//				</iso19112:SI_Gazetteer>
//	</gml:featureMember>
//</wfs:FeatureCollection>
                Document doc = DocumentHelper.parseText(content.toString()); // 将字符串转为XML
                Element rootElt = doc.getRootElement(); // 获取根节点FeatureCollection
               
                for (Iterator it = rootElt.elementIterator("featureMember"); it.hasNext();) {
                    if (map==null) {
                         map = new HashMap<>();
                    }
                    Element featureMemberElement = (Element) it.next();
                    Element gazetterEle = featureMemberElement.element("SI_Gazetteer");
                    Element geoEle = gazetterEle.element("Geometry");
                    Element pointEle = geoEle.element("Point");
                    Element coordEle = pointEle.element("coordinates");
                    String[] coordinate = coordEle.getTextTrim().split(",");
                    Coordinate tmpCoordinate = new Coordinate();
                    tmpCoordinate.Longitude = Double.parseDouble(coordinate[0]);
                    tmpCoordinate.Latitude = Double.parseDouble(coordinate[1]);
                    Element nameEle = gazetterEle.element("name");
                    map.put(nameEle.getTextTrim(), tmpCoordinate);
                }

            } else {
                //如果服务器返回的HTTP状态不是HTTP_OK，则表示发生了错误，此时可以通过如下方法了解错误原因。
                InputStream is = con.getErrorStream();    //通过getErrorStream了解错误的详情，因为错误详情也以XML格式返回，因此也可以用JDOM来获取。
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                br = new BufferedReader(isr);
                while ((line = br.readLine()) != null) {
                   content.append(line);
                }
                 Logger.getLogger(SkyMapAPI.class.getName()).log(Level.SEVERE, "查询天地图地址服务出错" );
            }

        } catch (MalformedURLException e) {
            Logger.getLogger(SkyMapAPI.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException | DocumentException e) {
            Logger.getLogger(SkyMapAPI.class.getName()).log(Level.SEVERE, null, e);
        }
        return map;
    }
   
    @Override
    public String getApiType()
    {
        return this.getClass().getName();
    }
    
    
}
  