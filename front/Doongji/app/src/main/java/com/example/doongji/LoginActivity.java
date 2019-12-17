package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    final String TAG = "LoginActivity";
    private HttpTask conn = new HttpTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickButton(View view) {
        EditText editText_name = (EditText) findViewById(R.id.name_input);

        String token = FirebaseInstanceId.getInstance().getToken();

        /* 유저 정보 세팅 */
        User.setInfo(editText_name.getText().toString(), token);

        /* DB에 유저 등록 */

        JSONObject user = new JSONObject();
        try {
            user.put("token", token);
            user.put("user_name",User.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        conn.execute("/api/members/add", "POST", user.toString());

        Intent i = new Intent(LoginActivity.this, Group_indexActivity.class);
        startActivity(i);
    }
}
