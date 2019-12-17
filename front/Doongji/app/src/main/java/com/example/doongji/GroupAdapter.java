package com.example.doongji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter {
    public GroupAdapter(Context context, ArrayList groups) {
        super(context, 0, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.group_list, parent, false);
        }

        Group group = (Group) getItem(position);

        TextView groupName = (TextView) convertView.findViewById(R.id.group_index);
        groupName.setText(group.getName());

        return convertView;
    }
}
