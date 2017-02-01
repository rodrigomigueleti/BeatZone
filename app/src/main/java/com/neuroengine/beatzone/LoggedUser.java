package com.neuroengine.beatzone;

import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;

/**
 * Created by usuariodebian on 25/01/17.
 */

public class LoggedUser implements Serializable {

    private static LoggedUser instance = null;

    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }

    private String id;
//    private Drawable image = null;
    private Uri imagemUri;
    static final String LoggedUserId = "LoggedUser";
    public static final String TAG = "LoggedUser";

//    public Drawable getImage() {
//        return image;
//    }
//
//    public void setImage(Drawable image) {
//        this.image = image;
//    }

    public void setId(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public Uri getImagemUri() {
        return imagemUri;
    }

    public void setImagemUri(Uri imagemUri) {
        this.imagemUri = imagemUri;
    }
//    public void setImage(Uri uri) {
//        new DownloadImageTask(this.image).execute(uri);
//        Log.v(TAG, "image catching");
//    }
//
//    private class DownloadImageTask extends AsyncTask<Uri, Void, Drawable> {
//        private Drawable image;
//
//        public DownloadImageTask(Drawable image) {
//            this.image = image;
//        }
//
//        @Override
//        protected Drawable doInBackground(Uri... params) {
//            Drawable image = null;
//            String pathFoto = params[0].toString();
//            try {
//                InputStream is = (InputStream) new URL(pathFoto).openStream();
//                image = Drawable.createFromStream(is, "src name");
//            } catch (Exception e) {
//                Log.w(TAG, "Image catch problem: ");
//            }
//            return image;
//        }
//
//        @Override
//        protected void onPostExecute(Drawable result) {
//            super.onPostExecute(result);
//            this.image = result;
//        }
//    }
}
