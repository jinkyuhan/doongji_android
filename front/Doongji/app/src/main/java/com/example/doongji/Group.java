package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.app.FragmentManager;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Group extends AppCompatActivity {

    private JSONArray results;
    String group_id;
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

/*        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.f1);
        mapFragment.getMapAsync(this);*/
    }

/*    @Override
    public void onMapReady(GoogleMap Map) {
        mMap=Map;
        Double xpos=0.0,ypos=0.0;
        try {
            xpos=((Double)(results.getJSONObject(Integer.parseInt(group_id)).get("grp_xpos"))).doubleValue();
            ypos=((Double)(results.getJSONObject(Integer.parseInt(group_id)).get("grp_ypos"))).doubleValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LatLng Doong_ji=new LatLng(xpos,ypos);
        MarkerOptions marker=new MarkerOptions();
        marker.title("우리 둥지");

        Map.addMarker(marker);
        Map.moveCamera(CameraUpdateFactory.newLatLng(Doong_ji));
        Map.animateCamera(CameraUpdateFactory.zoomTo(14));
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        group_id = intent.getExtras().getString("grp_id");
        TextView T = (TextView) findViewById(R.id.group_name);
        T.setText(group_id);

        ListView listView = (ListView) findViewById(R.id.group);

        class MyRunnable implements Runnable {
            String id;
            public MyRunnable(String id) {
                this.id = id;
            }

            public void run() {
                HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
                try {
                    results = connecter.sendHttp("/api/groups/" + id + "/members", "GET");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Runnable r = new MyRunnable(group_id);
        Thread t = new Thread(r);
        t.start();
        ArrayList<String> aryList = new ArrayList<>();
        try {
            t.join();
            for (int i = 0; i < results.length(); i++) {
                aryList.add(results.getJSONObject(i).get("user_name").toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.member_list, R.id.member_name, aryList
        );

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = (String) adapterView.getItemAtPosition(i); // i : position
                        Intent intent = new Intent(getApplicationContext(), Message.class);
                        intent.putExtra("send_message_mem", item);
                        Toast.makeText(Group.this, item + " selected", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
        );

    }

    public void onClickButton(View v) {
        switch (v.getId()) {
            case R.id.Option_btn:
                //OpenDrawer 출처 https://g-y-e-o-m.tistory.com/128
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.END);
                break;
            case R.id.Add_member_btn:
                Intent i2 = new Intent(Group.this, Add_member.class);
                i2.putExtra("grp_id", group_id);
                startActivity(i2);
                break;
            case R.id.Group_setting_btn:
                Intent i3 = new Intent(Group.this, Set_group.class);
                i3.putExtra("grp_id", group_id);
                startActivity(i3);
                break;
            case R.id.Group_exit_btn:
                new AlertDialog.Builder(this)
                        .setTitle("둥지 나가기")
                        .setMessage("둥지를 나가시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                }
                        )
                        .setNegativeButton("아니요", null)
                        .setCancelable(false)
                        .show();
                break;
        }
    }

    //이전 버튼 클릭시 Drawer 닫기 출처 https://lktprogrammer.tistory.com/168
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
