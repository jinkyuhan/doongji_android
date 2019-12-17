package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Set_groupActivity extends AppCompatActivity {

    private JSONArray results;
    String group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_group);

        Intent intent = getIntent();
        group_id = intent.getExtras().getString("grp_id");
    }

    public void onClickButton(View view) {
        EditText name = (EditText) findViewById(R.id.edit_group_name);
        EditText xpos = (EditText) findViewById(R.id.edit_group_xpos);
        EditText ypos = (EditText) findViewById(R.id.edit_group_ypos);
        Spinner spi = (Spinner) findViewById(R.id.spinner1);

        ArrayList<String> array = new ArrayList<>();
        array.add(name.getText().toString());
        array.add(xpos.getText().toString());
        array.add(ypos.getText().toString());
        array.add(spi.getSelectedItem().toString());

        class MyRunnable implements Runnable {
            ArrayList<String> param;

            public MyRunnable(ArrayList<String> parameter) {
                this.param = parameter;
            }

            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {
                    results = connecter.sendHttp("/api/groups/" + group_id + "/" + param.get(0) + "/" + param.get(1) + "/" + param.get(2) +"/"+param.get(3), "PUT");
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
