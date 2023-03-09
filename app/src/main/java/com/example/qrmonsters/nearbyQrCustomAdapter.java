package com.example.qrmonsters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;


import java.util.ArrayList;

public class nearbyQrCustomAdapter extends ArrayAdapter<QRCodeObject> {

    private ArrayList<QRCodeObject> qrs;
    private Context context;

    public nearbyQrCustomAdapter(Context context, ArrayList<QRCodeObject> qrs) {
        super(context, 0, qrs);
        this.qrs = qrs;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.qr_nearby_list, parent,
                    false);
        }

        QRCodeObject qr = qrs.get(position);

        TextView qrNameView = view.findViewById(R.id.qrName);
        TextView qrScoreView = view.findViewById(R.id.qrScore);

        qrNameView.setText(qr.getCodeName());
        qrScoreView.setText(qr.getCodeScore().toString());

        return view;

    }

}
