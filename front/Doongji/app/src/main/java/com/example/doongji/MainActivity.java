package com.example.doongji;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    final String TAG = "MainActivity";
    HttpTask conn = new HttpTask();

    static final Integer APP_PERMISSION = 1;

    private void askForPermission(String permission, Integer requestCode)
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {permission }, requestCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {permission }, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent i;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, APP_PERMISSION);
        // 디바이스 등록 여부
        if (!isDupDevice()) {
            i = new Intent(MainActivity.this, LoginActivity.class); // 미등록, 등록창으로
        } else {
            i = new Intent(MainActivity.this, Group_indexActivity.class);// 등록, 둥지목록 창으로
        }
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, APP_PERMISSION);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return;

    }

    /* 디바이스 등록 여부 반환 */
    private boolean isDupDevice() {
        String resultString = null;
        String token = FirebaseInstanceId.getInstance().getToken();
        try {
            resultString = conn.execute("/api/members/isDup?token=" + token, "GET", null).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (resultString.equals("")) {
            return false;
        } else {
            try {
                JSONObject result = new JSONObject(resultString);
                User.setInfo(result.getString("user_name"), result.getString("token"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}