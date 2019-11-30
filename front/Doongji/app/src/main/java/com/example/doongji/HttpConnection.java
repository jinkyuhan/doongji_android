package com.example.doongji;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import javax.net.ssl.HttpsURLConnection;

// 사용법
//System.out.println("GET으로 데이터 가져오기");
//http.sendGet("http://www.naver.com");
//System.out.println("POST로 데이터 가져오기");
//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
//http.sendPost("https://www.google.co.kr/",urlParameters);

public class HttpConnection {

    private String ipAdr;

    public HttpConnection(String _ipAdr) {
        this.ipAdr = _ipAdr;
    }

    //sendGet, sendPost 출처 : https://118k.tistory.com/225
    public JSONArray sendGet(String tragetUrl) throws Exception {
        String inputLine;
        StringBuffer response=new StringBuffer();
        JSONArray results=new JSONArray();

        URL url = new URL(ipAdr+tragetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET"); // optional default is GET
        con.setRequestProperty("content-type", "application/x-www-form-urlencoded");// add request header

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        inputLine=in.readLine();
        in.close();
        if(inputLine.charAt(0)=='{') {
            results.put(new JSONObject(inputLine));
            return results;
        }
        return new JSONArray(inputLine);
    }

    public JSONArray sendPost(String targetUrl) throws Exception {
        StringBuffer response=new StringBuffer();
        JSONArray results=new JSONArray();
        String inputLine;
        URL url = new URL(ipAdr+targetUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST"); // HTTP POST 메소드 설정
        con.setRequestProperty("content-type", "application/x-www-form-urlencoded");// add request header
        con.setDoOutput(true);

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        inputLine=in.readLine();
        in.close();
        if(inputLine.charAt(0)=='{') {
            results.put(new JSONObject(inputLine));
            return results;
        }
        return new JSONArray(inputLine);
    }

    public JSONArray sendPut(String targetUrl, String parameters) throws Exception {

        URL url = new URL(targetUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        con.setRequestMethod("PUT"); // HTTP POST 메소드 설정
        con.setRequestProperty("content-type", "application/x-www-form-urlencoded");// add request header
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(parameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        JSONArray results = new JSONArray(in.readLine());
        in.close();
        return results;
    }

    public JSONArray sendDelete(String targetUrl, String parameters) throws Exception {

        URL url = new URL(targetUrl);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        con.setRequestMethod("DELETE"); // HTTP POST 메소드 설정
        con.setRequestProperty("content-type", "application/x-www-form-urlencoded");// add request header
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(parameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        JSONArray results = new JSONArray(in.readLine());
        in.close();
        return results;
    }

}

