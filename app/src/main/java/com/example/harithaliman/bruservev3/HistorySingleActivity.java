package com.example.harithaliman.bruservev3;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

public class HistorySingleActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String complaintId, userId;
    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

    Location mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;


    private DatabaseReference complaintsPotholeHistoryDatabase;
    private DatabaseReference complaintsPotholeLocationDatabase;

    private TextView category;
    private TextView title;
    private TextView description;
    private TextView currentStatus;
    private TextView complaintDate;
    private TextView userName;
    private TextView timestamp;
    private TextView complaintIdTextView;

    private ImageView imageComplaint;

    private ImageView imageUser;

    private LatLng complaintLatLng;

    private String complaintTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_single);

        complaintId = getIntent().getExtras().getString("complaintId");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mMapFragment.getMapAsync(this);

        category = findViewById(R.id.category);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        currentStatus = findViewById(R.id.currentStatus);
        complaintDate = findViewById(R.id.complaintDate);
        userName = findViewById(R.id.userName);
        timestamp = findViewById(R.id.timestamp);
        complaintIdTextView = findViewById(R.id.complaintIdTextView);

        imageComplaint = findViewById(R.id.complaintImage);
        imageUser = findViewById(R.id.userImage);

        complaintsPotholeHistoryDatabase = FirebaseDatabase.getInstance().getReference().child("complaintsPothole").child(complaintId);
        getComplaintInformation();

//        complaintsPotholeLocationDatabase = FirebaseDatabase.getInstance().getReference().child("complaintsPothole_location").child(complaintId);
//        getComplaintsPotholeLocation();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        complaintsPotholeLocationDatabase = FirebaseDatabase.getInstance().getReference().child("complaintsPothole_location").child(complaintId);
        getComplaintsPotholeLocation();





        //mMapFragment
    }


    private void getComplaintInformation() {
        complaintIdTextView.setText(complaintId);

        complaintsPotholeHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        if (child.getKey().equals("userId")) {
                            userId = child.getValue().toString();
                            getUserInformation("Users", userId);
                        }

                        if (child.getKey().equals("timestamp")) {
                            timestamp.setText(getDate(Long.valueOf(child.getValue().toString())));
                        }
                        if (child.getKey().equals("category")) {
                            category.setText(child.getValue().toString());
                        }

                        if (child.getKey().equals("complaintTitle")) {
                            complaintTitle = child.getValue().toString();
                            title.setText(complaintTitle);
                        }

                        if (child.getKey().equals("complaintDescription")) {
                            description.setText(child.getValue().toString());
                        }

                        if (child.getKey().equals("currentStatus")) {
                            currentStatus.setText(child.getValue().toString());
                        }
                        if (child.getKey().equals("imageAddress")) {
                            Glide.with(getApplicationContext()).load(child.getValue().toString()).into(imageComplaint);
                        }
                        if (child.getKey().equals("dateOfEncounter")) {
                            complaintDate.setText(child.getValue().toString());
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getComplaintsPotholeLocation() {
        complaintsPotholeLocationDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        if (child.getKey().equals("l")) {
                            complaintLatLng = new LatLng(Double.valueOf(child.child("0").getValue().toString()), Double.valueOf(child.child("1").getValue().toString()));
                            if (complaintLatLng != new LatLng(0,0)) {
                                mMap.addMarker(new MarkerOptions().position(complaintLatLng).title(complaintTitle));
                            }
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void getUserInformation(String otherUsers, String otherUserId) {
        DatabaseReference mOtherUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(otherUserId);
        mOtherUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("displayname") != null) {
                        userName.setText(map.get("displayname").toString());
                    }
                    if (map.get("profileImageUrl") != null) {
                        Glide.with(getApplication()).load(map.get("profileImageUrl").toString()).into(imageUser);
                    }
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
}
