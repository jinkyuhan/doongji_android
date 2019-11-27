package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

//HttpGet, HttpPost 출처
//https://kwon8999.tistory.com/entry/HttpURLConnection-SampleGet-Post

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(MainActivity.this,Login.class);
        startActivity(i);
        finish();

//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                HttpGet();
//            }
//        };
//        thread.start();
    }

    private void HttpGet() {

        new AsyncTask<Void, Void, JSONObject>() {

            @Override
            protected JSONObject doInBackground(Void... voids) {

                JSONObject result = null;
                try {
                    URL url = new URL("http://192.168.43.113:3000/api/members");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    connection.setRequestMethod("GET");         // 통신방식
                    connection.setDoInput(true);                // 읽기모드 지정
                    connection.setUseCaches(false);             // 캐싱데이터를 받을지 안받을지
                    connection.setConnectTimeout(15000);        // 통신 타임아웃

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                    } else {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                    }

                } catch (ConnectException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
            }

        }.execute();
    }

    private void HttpPost() {

        new AsyncTask<Void, Void, JSONObject>() {

            @Override
            protected JSONObject doInBackground(Void... voids) {

                JSONObject result = null;
                try {
                    URL url = new URL("http://192.168.43.113:3000/api/members");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestProperty("content-type", "application/x-www-form-urlencoded");
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setUseCaches(false);
                    connection.setConnectTimeout(15000);

                    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

                    HashMap<String, String> map = new HashMap<>();
                    map.put("키값", "데이터값");

                    StringBuffer sbParams = new StringBuffer();

                    boolean isAnd = false;

                    for (String key : map.keySet()) {
                        if (isAnd)
                            sbParams.append("&");

                        sbParams.append(key).append("=").append(map.get(key));

                        if (!isAnd)
                            if (map.size() >= 2)
                                isAnd = true;

                    }
                    wr.write(sbParams.toString());
                    wr.flush();
                    wr.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                    } else {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();
                        result = new JSONObject(response.toString());
                    }

                } catch (ConnectException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
            }

        }.execute();
    }
}
