/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.spacialization;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 要素类
 * @author 倪永
 */
public class Feature implements  Cloneable{
   
    
   public  ArrayList<BydField>   Fields;
   
    /**
     *区分大小 写
     * @param name
     * @return
     */
    public  BydField getFieldbyName(String name)
   {
       BydField bydField = null;
       
       if (Fields.size()>0) {
           Iterator<BydField> iterator = Fields.iterator();
           while (iterator.hasNext()) {
               BydField tempbydField = iterator.next();
               if (tempbydField.Name.equals(name)) {
                   bydField = tempbydField;
                   break;
               }
           }
       }
       
       return bydField;
   }
   
   public  BydField getGeoField()
   {
       BydField bydField = null;
       
       if (Fields.size()>0) {
           Iterator<BydField> iterator = Fields.iterator();
           while (iterator.hasNext()) {
               BydField tempbydField = iterator.next();
               if (tempbydField.rov == BydField.RasterOrVector.vector) {
                   bydField = tempbydField;
                   break;
               }
           }
       }
       
       return bydField;
   }
        
   @Override
   public Feature clone() throws CloneNotSupportedException 
     {   
         Feature f=null;   
        try   
         {   
             f=(Feature)super.clone();   
         
               f.Fields=new ArrayList<>();
               for (BydField bydField : this.Fields) {
                 f.Fields.add(bydField.clone());
             }
         }   
        catch(CloneNotSupportedException e)   
         {   
               Logger.getLogger(Feature.class.getName()).log(Level.SEVERE, 
                       "不支持Feature深复制", e);
         }   
        return f;   
     }    
}
