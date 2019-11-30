package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Add_member extends AppCompatActivity {

    private JSONArray results;
    String group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent intent = getIntent();
        group_id = intent.getExtras().getString("grp_id");
    }

    public void onClickButton(View view) {

        EditText name = (EditText) findViewById(R.id.add_member_name);

        ArrayList<String> array = new ArrayList<>();
        array.add(name.getText().toString());

        class MyRunnable implements Runnable {
            ArrayList<String> param;

            public MyRunnable(ArrayList<String> parameter) {
                this.param = parameter;
            }

            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {
                    results = connecter.sendHttp("/api/groups/" + group_id + "/" + param.get(0), "POST");

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
                Toast.makeText(Add_member.this, "멤버가 추가되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Add_member.this, "멤버 추가에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                ((EditText) findViewById(R.id.add_member_name)).setText(null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
