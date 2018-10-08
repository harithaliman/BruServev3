package com.example.harithaliman.bruservev3.HistoryRecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harithaliman.bruservev3.HistorySingleActivity;
import com.example.harithaliman.bruservev3.R;

public class HistoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView complaintId;

    public TextView time;
    public TextView siteLocation;
    public TextView currentStatus;
    public ImageView complaintImageView;



    public HistoryViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        complaintId = (TextView) itemView.findViewById(R.id.complaintId);
        time = (TextView) itemView.findViewById(R.id.time);
        siteLocation = (TextView) itemView.findViewById(R.id.siteLocation);
        currentStatus = (TextView)itemView.findViewById(R.id.currentStatus);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), HistorySingleActivity.class);
        Bundle b = new Bundle();
        b.putString("complaintId", complaintId.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);

    }
}
