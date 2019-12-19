package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Set_groupActivity extends AppCompatActivity {
    private HttpTask conn = new HttpTask();
    private JSONArray results;
    int group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_group);
        Intent intent = getIntent();
        group_id = intent.getExtras().getInt("grp_id");
    }

    public void onClickButton(View view) {
        String resultString = null;
        String name = ((EditText)findViewById(R.id.edit_group_name)).getText().toString();
        String xpos = ((EditText)findViewById(R.id.edit_group_xpos)).getText().toString();
        String ypos = ((EditText)findViewById(R.id.edit_group_ypos)).getText().toString();
        String selectedItem = ((Spinner) findViewById(R.id.spinner1)).getSelectedItem().toString();

        try {
            resultString = conn.execute("/api/groups/" + group_id + "/" + name + "/" + xpos + "/" + ypos +"/"+selectedItem, "PUT", null).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONObject result = new JSONObject(resultString);
            if (result.getBoolean("success")) {
                Toast.makeText(Set_groupActivity.this, "그룹정보가 성공적으로 변경되었습니다..", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Set_groupActivity.this, "그룹정보 변경에 실패하였습니다..", Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.edit_group_name)).setText(null);
                ((EditText) findViewById(R.id.edit_group_xpos)).setText(null);
                ((EditText) findViewById(R.id.edit_group_ypos)).setText(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
