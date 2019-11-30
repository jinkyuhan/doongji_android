package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

public class Sign_up extends AppCompatActivity {
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
        ArrayList<String> array=new ArrayList<>();
        array.add(id.getText().toString());
        array.add(pw.getText().toString());
        array.add(name.getText().toString());

        class MyRunnable implements Runnable {
            ArrayList<String> param;

            public MyRunnable(ArrayList<String> parameter) {
                this.param = parameter;
            }

            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {
                    results = connecter.sendPost("/api/members/"+param.get(0) + "/" + param.get(1)+"/"+param.get(2));

                    if (((Boolean)results.getJSONObject(0).get("success")).booleanValue()) {
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                Toast.makeText(Sign_up.this,"회원가입이 성공적으로 이루어졌습니다..", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                Toast.makeText(Sign_up.this,"회원가입에 실패하였습니다..", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
        Runnable r = new MyRunnable(array);
        new Thread(r).start();
        finish();
    }


}
