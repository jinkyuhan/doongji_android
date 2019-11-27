package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;


public class Login extends AppCompatActivity {
    private JSONArray results;
    private String Id;
    private String Pw;

    //    private EditText id;
//    private EditText pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickButton(View view) {
        EditText id = (EditText) findViewById(R.id.ID_input);
        EditText pw = (EditText) findViewById(R.id.PW_input);
        ArrayList<String> array = new ArrayList<>();
        array.add(id.getText().toString());
        array.add(pw.getText().toString());

        class MyRunnable implements Runnable {
            ArrayList<String> param;

            public MyRunnable(ArrayList<String> parameter) {
                this.param = parameter;
            }

            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {

                    Log.i("LoginInfo", "Id: " + param.get(0) + "Pw:" + param.get(1));
                    results = connecter.sendGet("/api/members?user_id=" + param.get(0) + "&user_pw=" + param.get(1));
                    if (results.length() != 0) {
                        Intent i = new Intent(Login.this, Group_index.class);
                        startActivity(i);
                    } else {
                        TextView textView=(TextView)findViewById(R.id.textView);
                        textView.setText("로그인 실패\n다시 시도하세요.!!!!!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Runnable r = new MyRunnable(array);
        new Thread(r).start();

    }
}
