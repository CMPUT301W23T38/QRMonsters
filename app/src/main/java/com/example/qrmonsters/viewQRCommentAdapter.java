package com.example.qrmonsters;

import android.content.Context;
import android.content.Entity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class viewQRCommentAdapter extends ArrayAdapter<Map.Entry<String, String>> {
    public viewQRCommentAdapter(Context context, ArrayList<Map.Entry<String, String>> comment) {
        super(context,0,comment);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.content,
                    parent, false);
        } else {
            view = convertView;
        }
        Map.Entry<String, String> comment = getItem(position);
        TextView cityName = view.findViewById(R.id.idssss);
        TextView provinceName = view.findViewById(R.id.commmm);
        cityName.setText(comment.getKey());
        provinceName.setText(comment.getValue());
        return view;
    }
}
