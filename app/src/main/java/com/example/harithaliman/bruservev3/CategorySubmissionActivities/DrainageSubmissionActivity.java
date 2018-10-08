package com.example.harithaliman.bruservev3.CategorySubmissionActivities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harithaliman.bruservev3.Complaint;
import com.example.harithaliman.bruservev3.HomeActivity;
import com.example.harithaliman.bruservev3.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.core.GeoHash;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DrainageSubmissionActivity extends AppCompatActivity {


    EditText drainageComplaintTitle, potholeComplaintLocation, drainageComplaintDescription, drainageComplaintDateOfEncounter, drainageSiteLocation;

    Complaint complaint;

    private StorageReference mStorage;

    StorageReference filePath;

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    String complaintId = rootRef.child("complaintsDrainage").push().getKey();

    Button drainageButtonSubmit;

    String phoneNumber;



    long count;

    // Variables for location retrieval
    private static final int REQUEST_LOCATION = 1;
    Button buttonGetCurrentLocation;
    TextView textViewCoordinates;
    LocationManager locationManager;
    double latitude, longitude;

//    String complaintId;

    // Variables for image addition
    private ImageButton imageButtonAddPhoto;
    private int gallery_intent = 2;
    private StorageReference storageReference;
    StorageReference imagePath;
    StorageReference uploadeRef;

    // Variables for camera
    ImageButton imageButtonCamera;
    private ProgressDialog mProgress;
    private static final int CAMERA_REQUEST_CODE = 1;
    private Uri mImageUri = null;
    File cameraFile = null;

    AlertDialog alertDialog;

    // Camera Variables
    String mCurrentPhotoPath;

    // Database References
    private String pictureFilePath;
    private String deviceIdentifier;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();



    // GeoFire References
    DatabaseReference geoFireRef = FirebaseDatabase.getInstance().getReference().child("GeoFireCoordinates");
    //GeoFire geoFire = new GeoFire(geoFireRef);

    Location location;



    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    class myOnDismissListener implements DialogInterface.OnDismissListener {

        @Override
        public void onDismiss(DialogInterface dialog) {
            // TODO Auto-generated method stub
            alertDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drainage_submission_activity);

        mStorage = FirebaseStorage.getInstance().getReference();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uid = firebaseUser.getUid();

        DatabaseReference phoneNumberRef = rootRef.child("Users").child(uid).child("phoneNumber");
        phoneNumberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                phoneNumber = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        drainageComplaintTitle = (EditText) findViewById(R.id.drainageTitleEditText);
        drainageComplaintDescription = (EditText) findViewById(R.id.drainageDescriptionEditText);
        drainageComplaintDateOfEncounter = (EditText) findViewById(R.id.drainageDateOfEncounterEditText);
        drainageSiteLocation = (EditText) findViewById(R.id.drainageSiteLocationEditText);


        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        drainageComplaintTitle.setText(prefs.getString("autoSaveTitle", ""));

        drainageComplaintDescription.setText(prefs.getString("autoSaveDescription", ""));

        drainageComplaintDateOfEncounter.setText(prefs.getString("autoSaveDate", ""));

        drainageSiteLocation.setText(prefs.getString("autoSaveSiteLocation", ""));

        drainageComplaintTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                prefs.edit().putString("autoSaveTitle", s.toString()).commit();
            }
        });

        drainageComplaintDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                prefs.edit().putString("autoSaveDescription", s.toString()).commit();
            }
        });

        drainageComplaintDateOfEncounter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                prefs.edit().putString("autoSaveDate", s.toString()).commit();
            }
        });

        drainageSiteLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                prefs.edit().putString("autoSaveSiteLocation", s.toString()).commit();
            }
        });



        // onCreate for retrieving location
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        }, REQUEST_LOCATION);

        textViewCoordinates = (TextView) findViewById(R.id.textViewCoordinates);
        buttonGetCurrentLocation = (Button) findViewById(R.id.buttonGetCurrentLocation);

        // onCreate for image addition
        imageButtonAddPhoto = (ImageButton) findViewById(R.id.imageButtonAddPhoto);

        complaint = new Complaint();

        storageReference = FirebaseStorage.getInstance().getReference();

        // Camera

        imageButtonCamera = (ImageButton) findViewById(R.id.imageButtonCamera);

        mProgress = new ProgressDialog(this);

        drainageButtonSubmit = findViewById(R.id.drainageButtonSubmit);



        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                    File photoFile = null;
//                    try {
//                        photoFile = createImageFile();
//                    } catch (IOException ex) {
//                        // Error occurred while creating the File
//                    }
//                    // Continue only if the File was successfully created
//                    if (photoFile != null) {
//                        Uri photoURI = FileProvider.getUriForFile(this,
//                                "com.example.android.fileprovider",
//                                photoFile);
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(takePictureIntent, 0);
//                    }
//
//                }
            }
        });


    }




    // Get Current Location Button Onclick
    public void getCurrentLocationCoordinates(View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(DrainageSubmissionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DrainageSubmissionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DrainageSubmissionActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double doubleLatitude = location.getLatitude();
                double doubleLongitude = location.getLongitude();
                latitude = doubleLatitude;
                longitude = doubleLongitude;
//                latitude = String.valueOf(doubleLatitude);
//                longitude = String.valueOf(doubleLongitude);

                textViewCoordinates.setText("Your current location is " + "\nLatitude= " + latitude + "\nLongitude= " + longitude);


            } else if (location1 != null) {
                double doubleLatitude = location1.getLatitude();
                double doubleLongitude = location1.getLongitude();
                latitude = doubleLatitude;
                longitude = doubleLongitude;
//                latitude = String.valueOf(doubleLatitude);
//                longitude = String.valueOf(doubleLongitude);

                textViewCoordinates.setText("Your current location is " + "\nLatitude= " + latitude + "\nLongitude= " + longitude);

            } else if (location2 != null) {
                double doubleLatitude = location2.getLatitude();
                double doubleLongitude = location2.getLongitude();
                latitude = doubleLatitude;
                longitude = doubleLongitude;
//                latitude = String.valueOf(doubleLatitude);
//                longitude = String.valueOf(doubleLongitude);

                textViewCoordinates.setText("Your current location is " + "\nLatitude= " + latitude + "\nLongitude= " + longitude);
            } else {
                Toast.makeText(this, "Unable to trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please turn on your GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void buttonImageChoose(View view) {
//        Intent photoIntent = new Intent(Intent.ACTION_PICK);
//
//        photoIntent.setType("image/*");
//
//        startActivityForResult(photoIntent, gallery_intent);

        AlertDialog.Builder builder = new AlertDialog.Builder(DrainageSubmissionActivity.this);
        alertDialog = builder.create();
        alertDialog.setOnDismissListener(new DrainageSubmissionActivity.myOnDismissListener());

        // alertDialog.setTitle("Complete ");
        alertDialog.setMessage("Complete action using:");

        alertDialog.setButton(DialogInterface.BUTTON1, "Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alertDialog.setButton(DialogInterface.BUTTON2, "Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }

        });
        alertDialog.show();

    }

    private void takePictureIntent() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicture.putExtra( MediaStore.EXTRA_FINISH_ON_COMPLETION, true);
//        startActivityForResult(takePicture, 0);

        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, 0);

            File pictureFile = null;
            try {
                pictureFile = getPictureFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.zoftino.android.fileprovider",
                        pictureFile);
                takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePicture, 0);
            }
        }
    }
    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "ZOFTINO_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile,  ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                imageButtonCamera.setImageURI(resultUri);
                mImageUri = resultUri;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


        switch (requestCode) {
            // Camera Case
            case 0:
                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();

                    StorageReference filePath = mStorage.child("DrainageImages").child(uri.getLastPathSegment());

                    filePath.getFile(uri).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        }
                    });

                }
                break;
            // Gallery Case
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    imageButtonAddPhoto.setImageURI(selectedImage);

                    imagePath = storageReference.child("DrainageImages");

                    UploadTask uploadTask = imagePath.putFile(selectedImage);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Map newImage = new HashMap();
                                    newImage.put("imageAddress", uri.toString());
                                    rootRef.child("complaintsDrainage").child(complaintId).updateChildren(newImage);

//                                    finish();
                                    return;
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    finish();
                                    return;
                                }
                            });

                        }
                    });

//                    imagePath.putFile(selectedImage).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>(){
//                        @Override
//                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                            if (!task.isSuccessful()) {
//                                throw task.getException();
//                            }
//                            return imagePath.getDownloadUrl();
//                        }
//                    });




//                    imagePath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                           imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                               @Override
//                               public void onSuccess(Uri uri) {
//                                   Uri downloadUrl = uri;
//
//                               }
//                           });
//
//                            //Toast.makeText(PotholeSubmissionActivity.this, "", Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });
                }
                break;
        }
    }

    private void addToGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(pictureFilePath);
        Uri picUri = Uri.fromFile(f);
        galleryIntent.setData(picUri);
        this.sendBroadcast(galleryIntent);
    }

    private void addToCloudStorage() {
        File f = new File(pictureFilePath);
        Uri picUri = Uri.fromFile(f);
        final String cloudFilePath = picUri.getLastPathSegment();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference uploadeRef = storageRef.child(cloudFilePath);

        uploadeRef.putFile(picUri).addOnFailureListener(new OnFailureListener(){
            public void onFailure(@NonNull Exception exception){
//                Log.e(TAG,"Failed to upload picture to cloud storage");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                Toast.makeText(DrainageSubmissionActivity.this,
                        "Image has been uploaded to cloud storage",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void submitComplaint(View v) {

        DatabaseReference rootCountRef = rootRef.child("complaintsDrainage");

        //GeoFire geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("complaintsPothole_location"));


        rootRef.child("complaintsDrainage").child(complaintId).setValue(complaint);



        GeoFire geoFire = new GeoFire(rootRef.child("complaintsDrainage_location"));

        geoFire.setLocation(complaintId, new GeoLocation(latitude, longitude));

        GeoHash geoHash = new GeoHash(new GeoLocation(latitude, longitude));

        Map<String, Object> complaintsAddition = new HashMap<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String category = "Drainage";

        String title = drainageComplaintTitle.getText().toString();
        String description = drainageComplaintDescription.getText().toString();
        String dateOfEncounter = drainageComplaintDateOfEncounter.getText().toString();
        String siteLocation = drainageSiteLocation.getText().toString();

//        Long timeStampLong = System.currentTimeMillis()/1000;
//        String timeStampString = timeStampLong.toString();

        Complaint complaint = new Complaint();
        complaint.setUserId(userId);
        complaint.setComplaintId(complaintId);
        complaint.setComplaintTitle(title);
        complaint.setComplaintDescription(description);
        complaint.setSiteLocation(siteLocation);
        complaint.setCategory(category);
        complaint.setDateOfEncounter(dateOfEncounter);
        complaint.setTimestamp(getCurrentTimestamp());

        rootCountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DatabaseReference countRef = FirebaseDatabase.getInstance().getReference();

                count = dataSnapshot.getChildrenCount();
                countRef.child("count").child("2").child("value").setValue(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        complaint.setComplaintValue(1);

        if (imagePath != null){
            complaint.setImageAddress(imagePath.getDownloadUrl().toString());

        } else if (filePath != null) {
            complaint.setImageAddress((filePath.toString()));
        }

        complaint.setCurrentStatus("Active");

        complaintsAddition.put("complaintsDrainage/" + complaintId, complaint);
        complaintsAddition.put("complaintsDrainage_location/" + complaintId + "/g", geoHash.getGeoHashString());
        complaintsAddition.put("complaintsDrainage_location/" + complaintId + "/l", Arrays.asList(latitude,longitude));
        rootRef.updateChildren(complaintsAddition).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DrainageSubmissionActivity.this, "Complaint Submitted!", Toast.LENGTH_SHORT).show();

                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
                        edit().clear().apply();
                Intent intent = new Intent(DrainageSubmissionActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DrainageSubmissionActivity.this, "Error on submission", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private long getCurrentTimestamp() {
        Long timestamp = System.currentTimeMillis()/1000;
        return timestamp;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(DrainageSubmissionActivity.this, "Draft saved", Toast.LENGTH_SHORT).show();
    }
}
