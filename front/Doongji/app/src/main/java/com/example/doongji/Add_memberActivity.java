package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Add_memberActivity extends AppCompatActivity {
    private HttpTask conn;
    int group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        Intent intent = getIntent();
        group_id = intent.getExtras().getInt("grp_id");
    }

    public void onClickButton(View view) {
        String resultString = null;
        String name = ((EditText) findViewById(R.id.add_member_name)).getText().toString();

        try {
            conn = new HttpTask();
            resultString = conn.execute("/api/groups/" + group_id + "/" + name, "POST", null).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject result = new JSONObject(resultString);
            if (result.getBoolean("success")) {
                Toast.makeText(Add_memberActivity.this, "멤버가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Add_memberActivity.this, "멤버 추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.add_member_name)).setText(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
