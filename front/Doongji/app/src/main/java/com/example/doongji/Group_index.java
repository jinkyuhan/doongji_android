package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Group_index extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_index);
    }

    public void onClickButton(View v) {
        switch (v.getId())
        {
            case R.id.Add_group_btn:
                Intent i1 = new Intent(Group_index.this, Create_group.class);
                startActivity(i1);
                break;
            case R.id.test_btn:
                Intent i2 = new Intent(Group_index.this, Group.class);
                startActivity(i2);
                break;
        }
    }

}
