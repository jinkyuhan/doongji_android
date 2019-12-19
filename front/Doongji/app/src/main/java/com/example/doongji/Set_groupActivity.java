package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
        final String name = ((EditText) findViewById(R.id.edit_group_name)).getText().toString();
        String loc = ((EditText) findViewById(R.id.edit_group_loc)).getText().toString();
        final String selectedItem = ((Spinner) findViewById(R.id.spinner1)).getSelectedItem().toString();
        final Geocoder geocoder = new Geocoder(this);
        List<Address> list=null;
        try {
             list = geocoder.getFromLocationName(loc, 5);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        if(list.size()==0){
            Toast.makeText(getApplicationContext(),"유효하지 않은 주소를 입력했습니다.",Toast.LENGTH_SHORT).show();
        }
        else {
            final Double xpos = list.get(0).getLatitude();
            final Double ypos = list.get(0).getLongitude();
            new AlertDialog.Builder(this)
                    .setTitle("해당 주소가 맞습니까?")
                    .setMessage(list.get(0).getAddressLine((0)).toString())
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    String resultString = null;
                                    try {
                                        resultString = conn.execute("/api/groups/" + group_id + "/" + name + "/" + xpos + "/" + ypos + "/" + selectedItem, "PUT", null).get();
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
                                            ((EditText) findViewById(R.id.group_loc)).setText(null);
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
        }
    }
}
