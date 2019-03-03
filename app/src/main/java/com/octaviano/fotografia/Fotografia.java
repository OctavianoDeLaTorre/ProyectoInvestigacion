package com.octaviano.fotografia;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import com.octaviano.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Fotografia {

    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    private Intent intent;
    private final Activity activity;

    public Fotografia(Activity activity) {
        this.activity = activity;
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
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, getImageName()+currentDateAndTime() + ".jpg");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            imagen.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            return true;
        }catch(FileNotFoundException e  ) {
            return false;
        }catch (IOException e1){
            return false;
        }
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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private String getImageName(){
        return "Detector_retinoblastoma_"+currentDateAndTime();
    }

}
