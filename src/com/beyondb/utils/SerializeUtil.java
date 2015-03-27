/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.beyondb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author lbs
 */
public class SerializeUtil {
        
     /**
      * 序列化对象到文件
     * @param serializableObj
     * @param file
      */
    public static void serializeObject( Serializable serializableObj,File file) {
     
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
      
            FileOutputStream fos = new FileOutputStream(file);
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(serializableObj);
            }

        } catch (IOException ex) {
            System.err.println("序列化对象失败" + ex.getLocalizedMessage());
        }

        }

    /**
     *反序列化文件到对象
     * @param file
     * @return
     */
    public static Serializable reSerializeObject(File file) {
        Serializable obj = null;
        try {

            FileInputStream fis = new FileInputStream(file);

            try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                obj = (Serializable) ois.readObject();
            }

        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("反序列化对象失败"+ex.getLocalizedMessage());

        }
        return obj;

    }
}
