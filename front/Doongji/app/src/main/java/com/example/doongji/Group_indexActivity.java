package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static com.example.doongji.User.*;

public class Group_indexActivity extends AppCompatActivity {
    final private String TAG = "Group_indexActivity";
    private HttpTask conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_index);
    }

    @Override
    protected void onResume() {
        super.onResume();


        TextView nameView = (TextView) findViewById(R.id.tv_doongji_list);
        nameView.setText(User.getName()+"의 둥지 목록");
        String result = null;
        ListView listView = (ListView) findViewById(R.id.group_list);

        /* User 가 속한 그룹 받아오기 */
        try {
            conn = new HttpTask();
            result = conn.execute("/api/members/" + User.getToken() + "/belongs/groups", "GET", null).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        /* 받아온 data parsing */
        try {
            JSONArray groups = new JSONArray(result);
            User.clearMygroups();
            for (int i = 0; i < groups.length(); i++) {
                JSONObject group = groups.getJSONObject(i);
                User.addGroup(new Group(
                        group.getInt("grp_id"),
                        group.getString("grp_name"),
                        group.getDouble("grp_xpos"),
                        group.getDouble("grp_ypos"),
                        group.getInt("grp_radius")
                ));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        LocationManage locationManager=new LocationManage(getApplicationContext());
        locationManager.setGroupList(User.getMyGroups());

        GroupAdapter adapter = new GroupAdapter(this, User.getMyGroups());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                        intent.putExtra("grp_id", User.getMyGroups().get(i).getId());
                        Toast.makeText(Group_indexActivity.this, User.getMyGroups().get(i).getName() + " selected", Toast.LENGTH_SHORT).show();
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
