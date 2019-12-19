package com.example.doongji;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// 사용법
//System.out.println("GET으로 데이터 가져오기");
//http.sendGet("http://www.naver.com");
//System.out.println("POST로 데이터 가져오기");
//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
//http.sendPost("https://www.google.co.kr/",urlParameters);

public class HttpTask extends AsyncTask<String, String, String> {

    private String ipAdr;

    @Override
    protected void onPreExecute() {
        this.ipAdr = "http://35.243.97.109:3000";
    }

    protected String doInBackground(String... args) {
            String inputLine;
            StringBuffer response=new StringBuffer();
            String targetUrl = args[0];
            String method = args[1];
            String body = args[2];
            try {
                URL url = new URL(ipAdr + targetUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod(method); // optional default is GETs
                con.setRequestProperty("content-type", "application/json");// add request header

                if (body != null) {
                    con.setDoOutput(true);
                    OutputStream os = con.getOutputStream();
                    os.write(body.getBytes());
                    os.flush();
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));


                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }

}

