package com.example.qrmonsters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;

/**
 This is a custom adapter for the list view of nearby QR codes.
 It extends the ArrayAdapter class to provide the list of QRCodeObject objects.
 The adapter inflates the qr_nearby_list layout and sets the text of the QR code name and score.
 The getView() method overrides the ArrayAdapter's getView() method and returns a view for each item in the list.
 The adapter is used in the NearbyQrListActivity to display the list of nearby QR codes.
 */
public class QrCustomAdapter extends ArrayAdapter<QRCodeObject> {

    private ArrayList<QRCodeObject> qrs;
    private Context context;
    /**
     * Constructor for the nearbyQrCustomAdapter.
     *
     * @param context The context of the activity where the adapter is used
     * @param qrs     The ArrayList of QRCodeObject objects to be displayed in the list view
     */
    public QrCustomAdapter(Context context, ArrayList<QRCodeObject> qrs) {
        super(context, 0, qrs);
        this.qrs = qrs;
        this.context = context;
    }
    /**
     * Overrides the ArrayAdapter's getView() method to return a custom view for each item in the list.
     * The view is inflated from qr_nearby_list layout and the QR code name and score are set for each item.
     *
     * @param position    The position of the item in the list
     * @param convertView The view to be converted
     * @param parent      The parent ViewGroup
     * @return The converted view for each item in the list
     */
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
