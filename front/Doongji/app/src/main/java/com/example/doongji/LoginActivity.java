package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class LoginActivity extends AppCompatActivity {
    final String TAG = "LoginActivity";
    private JSONArray results;
    private String Id;
    private String Pw;
    private EditText id;
    private EditText pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User.initialize();
    }

    public void onClickButton(View view) {

        EditText id = (EditText) findViewById(R.id.ID_input);
        EditText pw = (EditText) findViewById(R.id.PW_input);
        ArrayList<String> loginInfo = new ArrayList<>();

        loginInfo.add(id.getText().toString());
        loginInfo.add(pw.getText().toString());
        switch (view.getId()) {
            case R.id.Sign_up_S_btn:
                Intent i1 = new Intent(LoginActivity.this, Sign_upActivity.class);
                startActivity(i1);
                break;
            case R.id.Log_in_btn:
                class MyRunnable implements Runnable {
                    ArrayList<String> param;

                    public MyRunnable(ArrayList<String> parameter) {
                        this.param = parameter;
                    }

                    public void run() {
                        HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                        try {

                            Log.i("LoginInfo", "Id: " + param.get(0) + "Pw:" + param.get(1));
                            results = connecter.sendHttp("/api/members/" + param.get(0) + "/" + param.get(1), "GET");
                        } catch (Exception e) {
                            Log.i(TAG, "HTTP 요청 실패 " + e.getMessage());
                        }
                    }
                }

                Runnable r = new MyRunnable(loginInfo);
                Thread t = new Thread(r);
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    Log.i(TAG, "비정상적인 쓰레드종료 " + e.getMessage());
                }

                if (results.length() != 0) {
                    try {
                        User.setInfo(results.getJSONObject(0).get("user_id").toString(), results.getJSONObject(0).get("user_name").toString());
                        Intent i = new Intent(LoginActivity.this, Group_indexActivity.class);
                        startActivity(i);
                    } catch (JSONException e) {
                        Log.i(TAG,"HTTP 응답으로 부터 JSON 객체 추출 실패" + e.getMessage());
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "로그인이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
