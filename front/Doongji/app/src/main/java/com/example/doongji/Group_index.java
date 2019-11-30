package com.example.doongji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Group_index extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_index);

        ListView listView = (ListView) findViewById(R.id.group_list);

        ArrayList<String> aryList = new ArrayList<>();
        aryList.add("그룹 1");
        aryList.add("그룹 2");
        aryList.add("그룹 3");
        aryList.add("그룹 4");
        aryList.add("그룹 5");
        aryList.add("그룹 6");
        aryList.add("그룹 7");
        aryList.add("그룹 8");
        aryList.add("그룹 9");
        aryList.add("그룹 10");
        aryList.add("그룹 11");
        aryList.add("그룹 12");
        aryList.add("그룹 13");
        aryList.add("그룹 14");
        aryList.add("그룹 15");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.group_list, R.id.group_index, aryList
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = (String) adapterView.getItemAtPosition(i); // i : position
                        Intent intent = new Intent(getApplicationContext(),Group.class);
                        intent.putExtra("group_name",item);
                        Toast.makeText(Group_index.this, item + " selected", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
        );


    }

    public void onClickButton(View v) {

        Intent i = new Intent(Group_index.this, Create_group.class);
        startActivity(i);


    }

}
