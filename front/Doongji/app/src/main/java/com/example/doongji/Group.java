package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Group extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
    }

    public void onClickButton(View v) {
        switch (v.getId()) {
            case R.id.Option_btn:
                Intent i1 = new Intent(Group.this, Create_group.class);
                startActivity(i1);
                break;
            case R.id.test2_btn:
                Intent i2 = new Intent(Group.this, Message.class);
                startActivity(i2);
                break;
        }
    }
}
