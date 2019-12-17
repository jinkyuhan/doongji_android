package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Create_groupActivity extends AppCompatActivity {

    private JSONArray results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }

    public void onClickButton(View view) {

        EditText name = (EditText) findViewById(R.id.group_name);
        EditText xpos = (EditText) findViewById(R.id.group_xpos);
        EditText ypos = (EditText) findViewById(R.id.group_ypos);

        ArrayList<String> array = new ArrayList<>();
        array.add(name.getText().toString());
        array.add(xpos.getText().toString());
        array.add(ypos.getText().toString());

        class MyRunnable implements Runnable {
            ArrayList<String> param;

            public MyRunnable(ArrayList<String> parameter) {
                this.param = parameter;
            }

            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {
                    results = connecter.sendHttp("/api/groups/" + param.get(0) + "/" + param.get(1) + "/" + param.get(2) + "/" + User.getId(), "POST");

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
                Toast.makeText(Create_groupActivity.this, "그룹이 성공적으로 만들어졌습니다..", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Create_groupActivity.this, "그룹생성에 실패하였습니다..", Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.group_name)).setText(null);
                ((EditText) findViewById(R.id.group_xpos)).setText(null);
                ((EditText) findViewById(R.id.group_ypos)).setText(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
