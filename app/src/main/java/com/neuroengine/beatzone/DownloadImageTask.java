package com.neuroengine.beatzone;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by usuariodebian on 01/02/17.
 */

public class DownloadImageTask extends AsyncTask<Uri, Void, Drawable> {
    public static final String TAG = "DownloadImageTask";
    private Drawable image;

    public DownloadImageTask(Drawable image) {
        this.image = image;
    }

    @Override
    protected Drawable doInBackground(Uri... params) {
        Drawable image = null;
        String pathFoto = params[0].toString();
        try {
            InputStream is = (InputStream) new URL(pathFoto).openStream();
            image = Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            Log.w(TAG, "Image catch problem: ");
        }
        return image;
    }

    @Override
    protected void onPostExecute(Drawable result) {
        super.onPostExecute(result);
        this.image = result;
    }

}