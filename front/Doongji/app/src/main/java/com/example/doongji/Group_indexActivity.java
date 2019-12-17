package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpRetryException;
import java.util.ArrayList;

import static com.example.doongji.User.*;

public class Group_indexActivity extends AppCompatActivity {
    final private String TAG = "Group_indexActivity";
//    private JSONArray results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView listView = (ListView) findViewById(R.id.group_list);
        class MyRunnable implements Runnable {
            JSONArray groups;

            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {
                    groups = connecter.sendHttp("/api/members/" + getId() + "/belongs/groups", "GET");
                    User.clearMygroups();
                    for (int i = 0; i < groups.length(); i++) {
                        JSONObject group = groups.getJSONObject(i);
                        User.addGroup(new Group(
                                group.getInt("grp_id"),
                                group.getString("grp_name"),
                                group.getDouble("grp_xpos"),
                                group.getDouble("grp_ypos")
                        ));
                    }
                } catch (Exception e) {
                    Log.i(TAG, "HTTP 요청 실패 " + e.getMessage());
                }
            }
        }
        Runnable r = new MyRunnable();
        Thread t = new Thread(r);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            Log.i(TAG, "비정상적인 쓰레드종료 " + e.getMessage());
        }
//        ArrayList<String> group_names = new ArrayList<String>();
//        for (Group grp : User.getMyGroups()){
//            group_names.add(grp.getName());
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this, R.layout.group_list, R.id.group_index, group_names
//        );
        GroupAdapter adapter = new GroupAdapter(this, User.getMyGroups());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                        intent.putExtra("grp_id", getMyGroups().get(i).getId());
                        Toast.makeText(Group_indexActivity.this, getMyGroups().get(i).getName() + " selected", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
        );
    }

    public void onClickButton(View v) {
        Intent i = new Intent(Group_indexActivity.this, Create_groupActivity.class);
        startActivity(i);
    }

}
