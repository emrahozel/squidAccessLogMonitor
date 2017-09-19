package com.service;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.util.config;


/**
 * Created by emrahozel on 14.09.2017.
 */
public class clientProvider {
    private static clientProvider instance = null;
    private static Object lock      = new Object();

    private TransportClient client;

    public static clientProvider instance(){

        if(instance == null) {
            synchronized (lock) {
                if(null == instance){
                    instance = new clientProvider();
                }
            }
        }
        return instance;
    }

    public void prepareClient(){
        config conf = new config();
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(conf.elkHost), conf.elkPort))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(conf.elkHost), conf.elkPort));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void closeClient(){
        client.close();
    }

    public TransportClient getClient() {
        return client;
    }

    public void printThis() {
        System.out.println(this);
    }
}
