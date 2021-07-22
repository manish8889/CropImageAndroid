package com.nic.cropimage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import static android.Manifest.permission_group.CAMERA;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final int CAMERA_CAPTURE = 1;
    final int CROP_PIC = 2;
    //captured picture uri
    private Uri picUri;
    private static final int PERMISSION_REQUEST_CODE = 200;
    MarshmallowPermission permission;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //retrieve a reference to the UI button
        Button captureBtn = (Button) findViewById(R.id.capture_btn);
//handle button clicks
        captureBtn.setOnClickListener(this);
//
//        if (!checkPermission()) {
//
//            requestPermission();
//
//        } else {
//            String errorMessage = "Permission already granted.";
//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
//
//
//        }

    }


    protected void onResume() {
        // TODO Auto-generated method stub

        permission = new MarshmallowPermission(this, Manifest.permission.CAMERA);
        if (permission.result == -1 || permission.result == 0) {
            try {
            } catch (Exception e) {
            }
        } else if (permission.result == 1) {
//            if (!init) initializeCamera();
        }


        permission = new MarshmallowPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission.result == -1 || permission.result == 0) {
            try {
            } catch (Exception e) {
            }
        } else if (permission.result == 1) {
        }

        super.onResume();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.capture_btn) {

            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            }
//            try {
//                //use standard intent to capture an image
//                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                //we will handle the returned data in onActivityResult
//                startActivityForResult(captureIntent, CAMERA_CAPTURE);
//            } catch (ActivityNotFoundException anfe) {
//                //display an error message
//                String errorMessage = "Whoops - your device doesn't support capturing images!";
//                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//                toast.show();
//            }

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(MainActivity.this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //user is returning from capturing an image using the camera
            if (requestCode == CAMERA_CAPTURE) {
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                if (data.getData() != null)
//                    // get the Uri for the captured image
//                    picUri = data.getData();

                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                imageView.setImageBitmap(photo);
                // get the Uri for the captured image
                picUri = getImageUri(getApplicationContext(), photo);
                performCrop();
            }
            // user is returning from cropping the image
            else if (requestCode == CROP_PIC) {


                // get the returned data
                Bundle extras = data.getExtras();
                Bitmap thePic = (Bitmap) extras.get("data");

                // get the cropped bitmap
//                Bitmap thePic = extras.getParcelable("data");
                ImageView picView = (ImageView) findViewById(R.id.picture);
                picView.setImageBitmap(thePic);
            }

        }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }


    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result1 == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CODE);

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                if (grantResults.length > 0) {
//
//                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//
//                    if (cameraAccepted) {
//
//                        Toast.makeText(MainActivity.this,
//                                "Permission Granted, Now you can access location data and camera.",
//                                Toast.LENGTH_SHORT)
//                                .show();
//
//                    } else {
//                        Toast.makeText(MainActivity.this,
//                                "Permission Denied, You cannot access location data and camera.",
//                                Toast.LENGTH_SHORT)
//                                .show();
//
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            showMessageOKCancel("You need to allow access to both the permissions",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                requestPermissions(new String[]{CAMERA},
//                                                        PERMISSION_REQUEST_CODE);
//                                            }
//                                        }
//                                    });
//
//                        }
//
//                    }
//                }
//
//
//                break;
//        }
//    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 2);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 300);
            cropIntent.putExtra("outputY", 300);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}