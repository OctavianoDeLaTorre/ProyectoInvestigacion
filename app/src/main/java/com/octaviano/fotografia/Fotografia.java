package com.octaviano.fotografia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.octaviano.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Fotografia {

    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    private Intent intent;
    private final Activity activity;
    private SimpleDateFormat df;
    private Bitmap fotografia;

    public Fotografia(Activity activity) {
        this.activity = activity;
        df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
    }

    public Bitmap getFotografia() {
        return fotografia;
    }

    public void setFotografia(Bitmap fotografia) {
        this.fotografia = fotografia;
    }

    public void loadFromGallery() {
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(
                Intent.createChooser(intent, activity.getResources().getString(R.string.seleccionarIMG)),
                REQUEST_CODE_GALLERY);
    }

    public void loadFromCamera(){
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    public boolean save(Bitmap imagen) {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Detector_retinoblastoma";
        File dir = new File(file_path);
        final boolean b = false;
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, getImageName() + currentDateAndTime() + ".jpg");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            imagen.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            return true;
        } catch (FileNotFoundException e) {
            return b;
        } catch (IOException e1) {
            return b;
        }
    }

    public boolean getBitmat(Intent data) {
        Uri selectedImageUri = null;
        Uri selectedImage;
        String filePath = null;
        selectedImage = data.getData();
        String selectedPath = selectedImage.getPath();
        if (selectedPath != null) {
            InputStream imageStream = null;
            try {
                imageStream = activity.getContentResolver().openInputStream(
                        selectedImage);
            } catch (FileNotFoundException e) {
                return false;
            }

            fotografia = BitmapFactory.decodeStream(imageStream);
        }
        return true;
    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(activity,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private String currentDateAndTime() {
        Calendar c = Calendar.getInstance();
        return df.format(c.getTime());
    }

    private String getImageName(){
        return "Detector_retinoblastoma_"+currentDateAndTime();
    }

}
