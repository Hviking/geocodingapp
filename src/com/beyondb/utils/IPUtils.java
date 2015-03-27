/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.beyondb.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 *
 * @author beyondb
 */
public abstract class IPUtils {

    public static Collection<InetAddress> getAllHostAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            Collection<InetAddress> addresses = new ArrayList<InetAddress>();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    addresses.add(inetAddress);
                }
            }

            return addresses;
        } catch (SocketException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Collection<String> getAllNoLoopbackAddresses() {
        Collection<String> noLoopbackAddresses = new ArrayList<String>();
        Collection<InetAddress> allInetAddresses = getAllHostAddress();

        for (InetAddress address : allInetAddresses) {
            if (!address.isLoopbackAddress()) {
                noLoopbackAddresses.add(address.getHostAddress());
            }
        }

        return noLoopbackAddresses;
    }

    public  static String getFirstIpv4Address()
    {
        String ip = "";
        Collection<InetAddress> allInetAddresses = getAllHostAddress();
        for (InetAddress address : allInetAddresses) {
            if (address.isLoopbackAddress()) {
                continue;
            }
            if (address.isLinkLocalAddress()) {
                continue;
            }
             ip =address.getHostAddress();
             break;
            
        }
        if (ip.isEmpty()) {
            ip = getFirstNoLoopbackAddress();
        }
       return  ip;
    }
    public static String getFirstNoLoopbackAddress() {
        Collection<String> allNoLoopbackAddresses = getAllNoLoopbackAddresses();

        String ip = "";
        if (!allNoLoopbackAddresses.isEmpty()) {
            ip = allNoLoopbackAddresses.iterator().next();
        } else {
            return (" Sorry, seems you don't have a network card :( ");
        }
        return ip;


    }
    
}
