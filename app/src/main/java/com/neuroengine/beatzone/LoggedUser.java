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

    private String id = "algo";
    private Uri imagemUri;
    static final String LoggedUserId = "LoggedUser";
    public static final String TAG = "LoggedUser";

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


}
