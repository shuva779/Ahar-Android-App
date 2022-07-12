package com.example.ahar.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.ahar.Fragment.ProfileFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by Istiak Saif on 02/04/21.
 */

public class ImageGetHelper {

    private Fragment fragment;
    public static final int CAMERA_REQUEST_CODE=100;
    public static final int STORAGE_REQUEST_CODE=200;
    public static final int IMAGE_PICK_GALLERY_CODE=300;
    public static final int IMAGE_PICK_CAMERA_CODE=400;
    private Uri imageUri;

    String cameraPermission[] = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String storagePermission[] = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public ImageGetHelper(Fragment fragment) {
        this.fragment = fragment;
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(fragment.getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        fragment.requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(fragment.getActivity(),
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(fragment.getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission(){
        fragment.requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }

    public void showImagePicDialog() {
//        String options[] = {"Camera","Gallery"};
        String options[] = {"Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
        builder.setTitle("Pick Image");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (which ==0){
//                    if(!checkCameraPermission()){
//                        requestCameraPermission();
//                    }
//                    else{
//                        pickFromCamera();
//                    }
//                }
//                else
                    if (which == 0){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    public void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageUri = fragment.getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        fragment.startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);
    }


    public void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        fragment.startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        pickFromCamera();
                    }
                    else{
                        Toast.makeText(fragment.getActivity(),"Please camera enable",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickFromGallery();
                    }
                    else{
                        Toast.makeText(fragment.getActivity(),"Please enable",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }
}
