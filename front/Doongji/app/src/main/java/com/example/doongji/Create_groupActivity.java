package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Create_groupActivity extends AppCompatActivity {
    private HttpTask conn = new HttpTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }

    public void onClickButton(View view) {
        String resultString = null;
        EditText name = (EditText) findViewById(R.id.group_name);
        EditText xpos = (EditText) findViewById(R.id.group_xpos);
        EditText ypos = (EditText) findViewById(R.id.group_ypos);

        /* DB에 그룹 생성 */
        try {
            resultString = conn.execute("/api/groups/" + name.getText().toString() + "/" + xpos.getText().toString() + "/" + ypos.getText().toString() + "/" + User.getToken(),
                    "POST",
                    null).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        /* 생성여부 판단  */
        if (resultString.equals("true")) {   // 성공 -> 이전화면
            Toast.makeText(Create_groupActivity.this, "그룹이 성공적으로 만들어졌습니다..", Toast.LENGTH_SHORT).show();
            finish();
        } else {    // 실패 -> 새로고침
            Toast.makeText(Create_groupActivity.this, "그룹생성에 실패하였습니다..", Toast.LENGTH_SHORT).show();
            ((EditText) findViewById(R.id.group_name)).setText(null);
            ((EditText) findViewById(R.id.group_xpos)).setText(null);
            ((EditText) findViewById(R.id.group_ypos)).setText(null);
        }

    }

}
