package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Create_groupActivity extends AppCompatActivity {
    private HttpTask conn = new HttpTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }

    public void onClickButton(View view) {

        final EditText name = (EditText) findViewById(R.id.group_name);
        final EditText loc = (EditText) findViewById(R.id.group_loc);
        final Geocoder geocoder = new Geocoder(this);
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName(loc.getText().toString(), 5);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (list.size() == 0) {
            Toast.makeText(getApplicationContext(), "유효하지 않은 주소를 입력했습니다.", Toast.LENGTH_SHORT).show();
        } else {
            final Double xpos = list.get(0).getLatitude();
            final Double ypos = list.get(0).getLongitude();
            new AlertDialog.Builder(this)
                    .setTitle("해당 주소가 맞습니까?")
                    .setMessage(list.get(0).getAddressLine((0)).toString())
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0, int arg1) {
                                    String resultString = null;
                                    /* DB에 그룹 생성 */
                                    try {
                                        resultString = conn.execute("/api/groups/" + name.getText().toString() + "/" + xpos + "/" + ypos + "/" + User.getToken(),
                                                "POST",
                                                null).get();
                                    } catch (ExecutionException | InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    /* 생성여부 판단  */
                                    if (resultString.equals("true")) {   // 성공 -> 이전화면
                                        Toast.makeText(Create_groupActivity.this, "그룹이 성공적으로 만들어졌습니다..", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {    // 실패 -> 새로고침
                                        Toast.makeText(Create_groupActivity.this, "그룹생성에 실패하였습니다..", Toast.LENGTH_SHORT).show();
                                        ((EditText) findViewById(R.id.group_name)).setText(null);
                                        ((EditText) findViewById(R.id.group_loc)).setText(null);
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
