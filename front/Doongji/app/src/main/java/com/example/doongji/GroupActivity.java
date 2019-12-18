package com.example.doongji;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

class MyComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
}

public class GroupActivity extends FragmentActivity implements OnMapReadyCallback {
    final private String TAG = "GroupActivity";
    private HttpTask conn;

    private Group groupInstance;

    LatLng Doong_ji;
    MarkerOptions marker;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Doong_ji = new LatLng(groupInstance.getXpos(), groupInstance.getYpos());
        marker = new MarkerOptions().position(Doong_ji).title("우리 둥지");
        googleMap.addMarker(marker);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Doong_ji));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    protected void onResume() {
        String resultString = null;

        super.onResume();

        /* 이전 액티비티로 현재창 그룹 인스턴스 초기화*/
        this.groupInstance = User.getGroupById(getIntent().getExtras().getInt("grp_id"));

        /* Group Name 렌더링 */
        TextView textView_groupName = (TextView) findViewById(R.id.group_name);
        textView_groupName.setText(groupInstance.getName());

        /* 지도 렌더링 */
        mapFragment.getMapAsync(this);

        /* 그룹의 멤버 받아오기 */
        Log.i("hoool", Integer.toString(groupInstance.getId()));
        try {
            conn = new HttpTask();
            resultString = conn.execute("/api/groups/" + groupInstance.getId() + "/members", "GET", null).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        /* 받아온 멤버로 멤버 리스트 채우기 */
        JSONArray results = null;
        try {
            Log.i("opoppop", resultString);
            results = new JSONArray(resultString);
            groupInstance.clearMember();
            for (int i = 0; i < results.length(); i++) {
                if (!results.getJSONObject(i).getString("token").equals(User.getToken())) {
                    groupInstance.addMember(new Member(
                            results.getJSONObject(i).getString("token"),
                            results.getJSONObject(i).getString("user_name")
                    ));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /* 리스트뷰 세팅 */
        ListView listView = (ListView) findViewById(R.id.group);
        MemberAdapter adapter = new MemberAdapter(this, groupInstance.getMembers());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Member member = (Member) adapterView.getItemAtPosition(i); // i : position
                        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                        intent.putExtra("target_id", member.getToken());
                        intent.putExtra("target_name", member.getName());

                        Toast.makeText(GroupActivity.this, member.getName() + " selected", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
        );

    }

    public void onClickButton(View v) {

        final ArrayList<String> array = new ArrayList<>();
        array.add(Integer.toString(groupInstance.getId())); // group_id
        array.add(User.getToken()); //user token
        switch (v.getId()) {
            case R.id.Option_btn:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.END);
                break;
            case R.id.Add_member_btn:
                Intent i2 = new Intent(GroupActivity.this, Add_memberActivity.class);
                i2.putExtra("grp_id", groupInstance.getId());
                startActivity(i2);
                break;
            case R.id.Group_setting_btn:
                Intent i3 = new Intent(GroupActivity.this, Set_groupActivity.class);
                i3.putExtra("grp_id", groupInstance.getId());
                startActivity(i3);
                break;
            case R.id.Group_exit_btn:
                new AlertDialog.Builder(this)
                        .setTitle("둥지 나가기")
                        .setMessage("둥지를 나가시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        String resultString = null;

                                        try {
                                            conn = new HttpTask();
                                            resultString = conn.execute("/api/groups/" + groupInstance.getId() + "/" + User.getToken(), "DELETE", null).get();
                                        } catch (ExecutionException | InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            JSONObject results = new JSONObject(resultString);
                                            if (results.getBoolean("success")) {
                                                Toast.makeText(GroupActivity.this, "둥지를 나갔습니다.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(GroupActivity.this, "둥지를 나가기에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        )
                        .setNegativeButton("아니요", null)
                        .setCancelable(false)
                        .show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void testClick(View view) {
        conn = new HttpTask();
        conn.execute("/services/sentry/"+User.getToken()+"/come/"+groupInstance.getId()+"/public", "POST", null);
    }
}
