package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MessageActivity extends AppCompatActivity {
    private HttpTask conn;
    String target_name;
    String target_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        target_name = intent.getExtras().getString("target_name");
        target_id = intent.getExtras().getString("target_id");
        TextView T = (TextView) findViewById(R.id.send_message_mem);
        T.setText(target_name);
    }

    public void onClickButton(View view) {
        String resultString= null;
        String msg = ((EditText) findViewById(R.id.send_message)).getText().toString();

        /* DB에 메세지 저장하기 */
        try {
            conn = new HttpTask();
            JSONObject body  = new JSONObject();
            body.put("message",msg);
            resultString = conn.execute("/api/messages/" + User.getToken() + "/" + target_id, "POST", body.toString()).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray results = new JSONArray(resultString);
            if ((Boolean) results.getJSONObject(0).get("success")) {
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
