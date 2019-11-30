package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Group_index extends AppCompatActivity {

    private JSONArray results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_index);

        ListView listView = (ListView) findViewById(R.id.group_list);
        class MyRunnable implements Runnable {

            public MyRunnable() {
            }
            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {
                    results = connecter.sendHttp("/api/groups/belongs/" + User.getId(), "GET");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Runnable r = new MyRunnable();
        Thread t= new Thread(r);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<String> aryList = new ArrayList<>();

        try {
            for (int i = 0; i < results.length(); i++) {
                aryList.add(results.getJSONObject(i).get("grp_name").toString());
            }
        } catch (JSONException e) {
            Log.i("qqqqq",e.getMessage());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.group_list, R.id.group_index, aryList
        );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = (String) adapterView.getItemAtPosition(i); // i : position
                        Intent intent = new Intent(getApplicationContext(), Group.class);
                        intent.putExtra("group_name", item);
                        Toast.makeText(Group_index.this, item + " selected", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
        );


    }

    public void onClickButton(View v) {

        Intent i = new Intent(Group_index.this, Create_group.class);
        startActivity(i);


    }

}
