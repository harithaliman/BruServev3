package com.example.harithaliman.bruservev3.HistoryActivities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;

import com.example.harithaliman.bruservev3.HistoryRecyclerView.HistoryAdapter;
import com.example.harithaliman.bruservev3.HistoryRecyclerView.HistoryObject;
import com.example.harithaliman.bruservev3.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PotholeHistoryActivity extends AppCompatActivity {

    private RecyclerView mHistoryRecyclerView;
    private RecyclerView.Adapter mHistoryAdapter;
    private RecyclerView.LayoutManager mHistoryLayoutManager;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    String complaintId;
    String siteLocation;
    String currentStatus;
    String imageAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mHistoryRecyclerView = (RecyclerView)findViewById(R.id.historyRecyclerView);
        mHistoryRecyclerView.setNestedScrollingEnabled(false);

        mHistoryRecyclerView.setHasFixedSize(true);
        mHistoryLayoutManager = new LinearLayoutManager(PotholeHistoryActivity.this);
        mHistoryRecyclerView.setLayoutManager(mHistoryLayoutManager);
        mHistoryAdapter = new HistoryAdapter(getDataSetHistory(), PotholeHistoryActivity.this);
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);

        getComplaintIds();


        complaintId = rootRef.child("complaintsPothole").getKey();


    }

    private void getComplaintIds() {
        DatabaseReference idDatabaseRef = FirebaseDatabase.getInstance().getReference().child("complaintsPothole");
        idDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot history : dataSnapshot.getChildren()) {
                        FetchComplaintHistory(history.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchComplaintHistory(String complaintKey) {
        DatabaseReference complaintDatabaseRef = FirebaseDatabase.getInstance().getReference().child("complaintsPothole").child(complaintKey);
        complaintDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String complaintData = dataSnapshot.getKey();
                    long timestamp = 0L;
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        if (child.getKey().equals("timestamp")) {
                            timestamp = Long.valueOf(child.getValue().toString());
                        }
                        if (child.getKey().equals("siteLocation")) {
                            siteLocation = child.getValue().toString();
                        }
                        if (child.getKey().equals("currentStatus")) {
                            currentStatus = child.getValue().toString();
                        }
                        if (child.getKey().equals("imageAddress")) {
                            imageAddress = child.getValue().toString();
                        }

                    }
                    HistoryObject obj = new HistoryObject(complaintData, getDate(timestamp), siteLocation, currentStatus, imageAddress);
                    resultsHistory.add(obj);
                    mHistoryAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String getDate (Long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp*1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm", cal).toString();
        return date;
    }

    private ArrayList resultsHistory = new ArrayList<HistoryObject>();
    private ArrayList<HistoryObject> getDataSetHistory() {
        return resultsHistory;
    }
}
