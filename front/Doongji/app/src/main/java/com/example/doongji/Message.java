package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Message extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();

        String member_name = intent.getExtras().getString("send_message_mem");
        TextView T = (TextView) findViewById(R.id.send_message_mem);
        T.setText(member_name);
    }
}
