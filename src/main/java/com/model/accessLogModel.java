package com.model;

import java.util.Date;

/**
 * Created by emrahozel on 16.09.2017.
 */
public class accessLogModel {

    private String timestamp;
    private String user;
    private String src_ip;
    private String dst_ip;
    private String dst_host;
    private String request_url;
    private String status_code;
    private String method;
    private String content;
    private String location;
    private double bytes;
    private long hits;
    private String byteFormat;

    public accessLogModel(){}

    public accessLogModel(String dst_ip, String byteFormat, long hits, String location) {
        this.dst_ip = dst_ip;
        this.byteFormat = byteFormat;
        this.hits = hits;
        this.location = location;
    }

    public accessLogModel(String src_ip, String byteFormat, long hits){
        this.src_ip = src_ip;
        this.byteFormat = byteFormat;
        this.hits = hits;
    }
    public accessLogModel(String content, long hits){
        this.content = content;
        this.hits = hits;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSrc_ip() {
        return src_ip;
    }

    public void setSrc_ip(String src_ip) {
        this.src_ip = src_ip;
    }

    public String getDst_ip() {
        return dst_ip;
    }

    public void setDst_ip(String dst_ip) {
        this.dst_ip = dst_ip;
    }

    public String getDst_host() {
        return dst_host;
    }

    public void setDst_host(String dst_host) {
        this.dst_host = dst_host;
    }

    public String getRequest_url() {
        return request_url;
    }

    public void setRequest_url(String request_url) {
        this.request_url = request_url;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getBytes() {
        return bytes;
    }

    public void setBytes(double bytes) {
        this.bytes = bytes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public String getByteFormat() {
        return byteFormat;
    }

    public void setByteFormat(String byteFormat) {
        this.byteFormat = byteFormat;
    }
}
