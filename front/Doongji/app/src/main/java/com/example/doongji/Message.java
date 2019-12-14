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
import org.json.JSONException;

import java.util.ArrayList;

public class Message extends AppCompatActivity {
    private JSONArray results;
    String member_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        String member_name = intent.getExtras().getString("send_message_mem");
        member_id = intent.getExtras().getString("send_mem_id");
        TextView T = (TextView) findViewById(R.id.send_message_mem);
        T.setText(member_name);
    }

    public void onClickButton(View view) {

        EditText msg = (EditText) findViewById(R.id.send_message);
        ArrayList<String> array = new ArrayList<>();
        array.add(msg.getText().toString());

        class MyRunnable implements Runnable {
            ArrayList<String> param;

            public MyRunnable(ArrayList<String> parameter) {
                this.param = parameter;
            }

            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {
                    results = connecter.sendHttp("/api/members/" + User.getId() + "/" + param.get(0) + "/" + member_id, "POST");
                    Log.i("rrrrrrrr",User.getId() + "/" + param.get(0) + "/" + member_id);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
        Runnable r = new MyRunnable(array);
        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if (((Boolean) results.getJSONObject(0).get("success")).booleanValue()) {
                Toast.makeText(Message.this, "전송 완료", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Message.this, "전송 실패", Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.send_message)).setText(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
