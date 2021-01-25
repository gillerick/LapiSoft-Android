package com.example.lapisoft;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission_group.CAMERA;


public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    public Laptop laptopDetails;
    private DatabaseReference mRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Write a message to the database
        FirebaseApp.initializeApp(this);

        mRef = FirebaseDatabase.getInstance().getReference();
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        TextView text = findViewById(R.id.scan_result);

        if (checkPermission())
            Toast.makeText(MainActivity.this, "Permission is granted", Toast.LENGTH_LONG).show();
        else requestPermission();

    }


    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permission[], int grantResults[]) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        if (shouldShowRequestPermissionRationale(CAMERA)) {
                            displayAlertMessage("You need to allow access for both permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                        }
                                    });
                            return;
                        }

                    }
                    break;
                }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            TextView tvStatus = findViewById(R.id.btn_scan);
            TextView tvResult = findViewById(R.id.scan_result);
            if (resultCode == RESULT_OK) {
                tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
                tvResult.setText(intent.getStringExtra("SCAN_RESULT"));
            } else if (resultCode == RESULT_CANCELED) {
                tvStatus.setText("Press a button to start a scan");
                tvResult.setText("Scan Cancelled");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();

        scannerView.setResultHandler(this);
        scannerView.startCamera();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        String scanResult = result.getText();
        Context context = this;
        Toast.makeText(MainActivity.this, "Fetching results", Toast.LENGTH_LONG).show();

        mRef.child("laptops").child(scanResult).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        laptopDetails = dataSnapshot.getValue(Laptop.class);
                        if (laptopDetails != null) {
                            String colour = laptopDetails.getColour();
                            String size = laptopDetails.getscreenSize();
                            String model = laptopDetails.getModel();
                            String user = laptopDetails.getuser();
                            String residence = laptopDetails.getUserResidence();
                            String phone = laptopDetails.getUserPhone();
                            String reg = laptopDetails.getuserStudentID();

                            setContentView(R.layout.scan_result_view);
                            View view = getLayoutInflater().inflate(R.layout.scan_result_view, null);

                            TextView tv1 = (TextView) findViewById(R.id.model);
                            tv1.setText(model);

                            TextView tv2 = (TextView) findViewById(R.id.colour);
                            tv2.setText(colour);

                            TextView tv3 = (TextView) findViewById(R.id.size);
                            tv3.setText(size);

                            TextView tv4 = (TextView) findViewById(R.id.serial);
                            tv4.setText(scanResult);

                            TextView tv5 = (TextView) findViewById(R.id.user);
                            tv5.setText(user);

                            TextView tv6 = (TextView) findViewById(R.id.residence);
                            tv6.setText(residence);

                            TextView tv7 = (TextView) findViewById(R.id.userPhone);
                            tv7.setText(phone);

                            TextView tv8 = (TextView) findViewById(R.id.userID);
                            tv8.setText(reg);


                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("No Results");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    scannerView.resumeCameraPreview(MainActivity.this);
                                }
                            });
                            builder.setMessage("Oops, we couldn't find that in the database");
                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}