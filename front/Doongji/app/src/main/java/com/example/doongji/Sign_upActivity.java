package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Sign_upActivity extends AppCompatActivity {
    final private String TAG = "Sign_upActivity";
    private JSONArray results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onClickButton(View view) {

        EditText id = (EditText) findViewById(R.id.Sign_up_id);
        EditText pw = (EditText) findViewById(R.id.Sign_up_pw);
        EditText name = (EditText) findViewById(R.id.Sign_up_name);
        ArrayList<String> signupInfo = new ArrayList<>();
        signupInfo.add(id.getText().toString());
        signupInfo.add(pw.getText().toString());
        signupInfo.add(name.getText().toString());

        class MyRunnable implements Runnable {
            ArrayList<String> param;

            public MyRunnable(ArrayList<String> parameter) {
                this.param = parameter;
            }

            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {
                    results = connecter.sendHttp("/api/members/" + param.get(0) + "/" + param.get(1) + "/" + param.get(2), "POST");
                } catch (Exception e) {
                    Log.i(TAG,"HTTP 요청 실패" + e.getMessage());
                }
            }

        }
        Runnable r = new MyRunnable(signupInfo);
        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.i(TAG, "비정상적인 쓰레드종료 " + e.getMessage());
        }
        try {
            if (((Boolean) results.getJSONObject(0).get("success")).booleanValue()) {
                Toast.makeText(Sign_upActivity.this, "회원가입이 성공적으로 이루어졌습니다..", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Sign_upActivity.this, "회원가입에 실패하였습니다..", Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.Sign_up_id)).setText(null);
                ((EditText) findViewById(R.id.Sign_up_pw)).setText(null);
                ((EditText) findViewById(R.id.Sign_up_name)).setText(null);
            }
        } catch (JSONException e) {
            Log.i(TAG,"HTTP 응답으로 부터 JSON 객체 추출 실패" + e.getMessage());
        }

    }


}
