package com.octaviano;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageTool {

    public static final int REQUEST_CODE_GALLERY = 1;
    public static final int REQUEST_CODE_CAMERA = 2;
    private Intent intent;
    private final Activity activity;

    public ImageTool(Activity activity) {
        this.activity = activity;
    }


    public void loadFromGallery() {
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(
                Intent.createChooser(intent, "Seleccione una fotografia:"),
                REQUEST_CODE_GALLERY);
    }

    public void loadFromCamera(){
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    public void share(Bitmap bitmap){
        new sendImageTask().execute(bitmap);
    }

    private class sendImageTask extends AsyncTask<Bitmap, Void, Void> {

        @Override
        protected Void doInBackground(Bitmap... params) {

            Bitmap bitmap = params[0];

            // Salva bitmap a disco.
            try {

                File cachePath = new File(activity.getApplicationContext().getCacheDir(), "imagenes"); //path cache.
                cachePath.mkdirs(); // Crea directorio si no existe.
                FileOutputStream stream = new FileOutputStream(cachePath + "/imagen.jpg"); // Escribe imagen.
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


            File imagePath = new File(activity.getApplicationContext().getCacheDir(), "imagenes"); //obtiene directorio.
            File newFile = new File(imagePath, "imagen.jpg"); //obtiene imagen.

            String PACKAGE_NAME = activity.getApplicationContext().getPackageName() + ".providers.FileProvider";

            Uri contentUri = FileProvider.getUriForFile(activity.getApplicationContext(), PACKAGE_NAME, newFile);

            if (contentUri != null) {

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                shareIntent.setDataAndType(contentUri, activity.getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                activity.startActivity(Intent.createChooser(shareIntent, "Elige una aplicación:"));

            }
            return null;
    }

    }


}
