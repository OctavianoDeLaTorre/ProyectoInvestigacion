package com.octaviano;

import android.app.Activity;
import android.content.Intent;

public class ImageTool {
    private static final int SELECT_FILE = 1;
    private Intent intent;

    /**
     * @param activity
     */
    public void loadFromGallery(Activity activity) {
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(
                Intent.createChooser(intent, "Seleccione una fotografia:"),
                SELECT_FILE);
    }
}
