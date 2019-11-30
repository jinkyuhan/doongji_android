package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;

import java.util.ArrayList;

public class Sign_up extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onClickButton(View view) {

        EditText id = (EditText) findViewById(R.id.Sign_up_id);
        EditText pw = (EditText) findViewById(R.id.Sign_up_pw);
        EditText name = (EditText) findViewById(R.id.Sign_up_name);

        HttpConnection connecter = new HttpConnection(getString(R.string.IPAd));
        try {
            connecter.sendPost("/api/members", "user_id=" + id.getText().toString() + "&user_pw=" + pw.getText().toString() + "&user_name=" + name.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        finish();
    }


}
