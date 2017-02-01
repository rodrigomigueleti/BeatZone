package com.neuroengine.beatzone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by usuariodebian on 17/01/17.
 */

public class DialogLogin extends AppCompatActivity {

    static final int LOGIN_RESULT = 333;
    private static final String TAG = "DialogLogin";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);

    }

    public void startSignInGoogle(View target) {
        Intent intent = new Intent(getApplicationContext(), LoginGoogle.class);
        startActivity(intent);
        //startActivityForResult(intent, LOGIN_RESULT);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == LOGIN_RESULT) {
//            if(resultCode == RESULT_OK) {
//                LoggedUser lu = (LoggedUser) data.getSerializableExtra(LoggedUser.LoggedUserId);
//                Log.v(TAG, "");
//            }
//        }
//    }
}
