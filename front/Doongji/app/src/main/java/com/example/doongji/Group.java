package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                //OpenDrawer 출처 https://g-y-e-o-m.tistory.com/128
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.END);
                break;
            case R.id.test2_btn:
                Intent i1 = new Intent(Group.this, Message.class);
                startActivity(i1);
                break;
            case R.id.Add_member_btn:
                Intent i2 = new Intent(Group.this, Add_member.class);
                startActivity(i2);
                break;
            case R.id.Group_setting_btn:
                Intent i3 = new Intent(Group.this, Set_group.class);
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
