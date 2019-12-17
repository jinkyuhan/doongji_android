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
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class MyComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
        return s1.compareTo(s2);
    }
}

public class GroupActivity extends FragmentActivity implements OnMapReadyCallback {
    final private String TAG = "GroupActivity";
    private JSONArray results;
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
    protected void onDestroy() {
        User.clearMySubscribeTopics();
        super.onDestroy();
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
        super.onResume();
        this.groupInstance = User.getGroupById(getIntent().getExtras().getInt("grp_id"));

        TextView textView_groupName = (TextView) findViewById(R.id.group_name);
        textView_groupName.setText(groupInstance.getName());

        mapFragment.getMapAsync(this);
        ListView listView = (ListView) findViewById(R.id.group);

        class MyRunnable implements Runnable {
            public MyRunnable() {
            }

            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {
                    results = connecter.sendHttp("/api/groups/" + groupInstance.getId() + "/members", "GET");
                    groupInstance.clearMember();
                    for (int i = 0; i < results.length(); i++) {
                        if (!results.getJSONObject(i).getString("user_id").equals(User.getId())) {
                            groupInstance.addMember(new Member(
                                    results.getJSONObject(i).getString("user_id"),
                                    results.getJSONObject(i).getString("user_name")
                            ));
                        }
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
//
//        ArrayList<String> memberList = new ArrayList<>();
//        try {
//            t.join();
//            for (Member member : groupInstance.getMembers()) {
//                if (!member.getId().equals(User.getId())) {
//                    memberList.add(member.getName());
//                }
//            }
//        } catch (InterruptedException e) {
//            Log.i(TAG, "비정상적인 쓰레드종료 " + e.getMessage());
//        }

        MemberAdapter adapter = new MemberAdapter(this, groupInstance.getMembers());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Member member = (Member) adapterView.getItemAtPosition(i); // i : position
                        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                        intent.putExtra("target_id", member.getId());
                        intent.putExtra("target_name", member.getName());

                        Toast.makeText(GroupActivity.this, member.getName() + " selected", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
        );

    }

    public void onClickButton(View v) {
        final ArrayList<String> array = new ArrayList<>();
        array.add(Integer.toString(groupInstance.getId()));
        array.add(User.getId());
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
                                        class MyRunnable implements Runnable {
                                            ArrayList<String> param;

                                            public MyRunnable(ArrayList<String> parameter) {
                                                this.param = parameter;
                                            }

                                            public void run() {
                                                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                                                try {
                                                    results = connecter.sendHttp("/api/groups/" + param.get(0) + "/" + param.get(1), "DELETE");
                                                    User.unsubscribeMyGroupById(Integer.parseInt(param.get(0)));
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
                                            Log.i(TAG, "비정상적인 쓰레드종료 " + e.getMessage());
                                        }
                                        try {
                                            if (((Boolean) results.getJSONObject(0).get("success")).booleanValue()) {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Toast.makeText(GroupActivity.this, "둥지를 나갔습니다.", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });

                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        Toast.makeText(GroupActivity.this, "둥지를 나가기에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
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
        class MyRunnable implements Runnable {
            public MyRunnable() {
            }

            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {
                    results = connecter.sendHttp("/services/sentry/test_user2/come/1/public", "POST");
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
    }
}
