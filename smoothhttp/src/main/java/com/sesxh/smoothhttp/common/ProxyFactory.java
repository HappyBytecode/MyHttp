package com.sesxh.smoothhttp.common;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author LYH
 * @date 2021/5/8
 * @time 17:41
 * @desc
 **/
public class ProxyFactory {

    public static Proxy getDefaultProxy(String host,int port){
        return getProxy(Proxy.Type.HTTP,host, port);
    }

    public static Proxy getProxy(Proxy.Type type,String host,int port){
        return new Proxy(type, new InetSocketAddress(host, port));
    }
}
