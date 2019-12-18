package com.example.doongji;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MemberAdapter extends ArrayAdapter {
    public MemberAdapter(Context context, ArrayList members) {
        super(context, 0, members);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.member_list, parent, false);
        }

        Member member = (Member) getItem(position);

        TextView memberName = (TextView) convertView.findViewById(R.id.member_name);
        memberName.setText(member.getName());

        return convertView;
    }
}
