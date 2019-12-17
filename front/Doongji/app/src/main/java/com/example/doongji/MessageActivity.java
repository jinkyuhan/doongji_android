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
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    private JSONArray results;
    String target_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        String target_name = intent.getExtras().getString("target_name");
        target_id = intent.getExtras().getString("target_id");
        TextView T = (TextView) findViewById(R.id.send_message_mem);
        T.setText(target_name);
    }

    @Override
    protected void onDestroy() {
        User.clearMySubscribeTopics();
        super.onDestroy();
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
                    JSONObject json=new JSONObject();
                    json.put("message",param.get(0));
                    results = connecter.sendHttp("/api/messages/" + User.getId() + "/" + target_id, "POST",json.toString());

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
                Toast.makeText(MessageActivity.this, "전송 완료", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(MessageActivity.this, "전송 실패", Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.send_message)).setText(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
