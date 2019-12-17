package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    HttpTask conn = new HttpTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent i;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 디바이스 등록 여부
        if (!isDupDevice()) {
            i = new Intent(MainActivity.this, LoginActivity.class); // 미등록, 등록창으로
        } else {
            i = new Intent(MainActivity.this, Group_indexActivity.class);// 등록, 둥지목록 창으로
        }
        startActivity(i);
        finish();
    }

    /* 디바이스 등록 여부 반환 */
    private boolean isDupDevice() {
        String resultString = null;
        String token = FirebaseInstanceId.getInstance().getToken();
        try {
            resultString = conn.execute("/api/members/isDup?token=" + token, "GET", null).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("wfwfwf","rs : " +resultString);
        if (resultString.equals("")) {
            Log.i("wfwfwf","hihihi");
            return false;
        } else {
            Log.i("wfwfwf", "oooop");
            try {
                JSONObject result = new JSONObject(resultString);
                User.setInfo(result.getString("user_name"), result.getString("token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}