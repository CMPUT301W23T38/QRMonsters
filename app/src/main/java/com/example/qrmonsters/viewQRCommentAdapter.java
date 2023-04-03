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
/**
 The viewQRCommentAdapter class provides an adapter to display a list of comments
 for a QR code. It extends the ArrayAdapter class and uses a list of Map.Entry objects.
 Each entry contains a key-value pair representing a comment.
 */
public class viewQRCommentAdapter extends ArrayAdapter<Map.Entry<String, String>> {
    /**
     * Constructs a new viewQRCommentAdapter with the given context and comment data.
     * @param context The context of the application.
     * @param comment A list of comment data to display.
     */
    public viewQRCommentAdapter(Context context, ArrayList<Map.Entry<String, String>> comment) {
        super(context,0,comment);
    }
    /**
     * Returns a view for a single item in the list of comments.
     * @param position The position of the item in the list.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent ViewGroup that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
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
