package com.neuroengine.beatzone;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.neuroengine.beatzone.R;

/**
 * Created by usuariodebian on 11/01/17.
 */

public class StartActivity extends AppCompatActivity {

    private MenuItem mActionLoginMenuItem;
    private static final int RC_CALL_LOGIN = 1;
    private static final String TAG = "StartActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar)findViewById(R.id.start_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                Intent intent = new Intent(getApplicationContext(), LoginGoogle.class);
                startActivityForResult(intent, RC_CALL_LOGIN);
                return true;
            case R.id.action_main_settings:
                Log.i("MAIN_MENU", "Settings");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_CALL_LOGIN) {
            Log.i(TAG, "Resultado");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mActionLoginMenuItem = menu.findItem(R.id.action_login);
        //updateUI();
        return true;
    }

    private void updateUI() {
        if (mActionLoginMenuItem != null)
            if (LoggedUser.getInstance().getImagemUri() == null) {
                String uriImage = "@drawable/ic_action_person";
                Drawable icon = getResources().getDrawable(getResources().getIdentifier(uriImage, null, getPackageName()));
                mActionLoginMenuItem.setIcon(icon);
            } else {
                Drawable icon = null;
                new DownloadImageTask(icon).execute(LoggedUser.getInstance().getImagemUri());
                mActionLoginMenuItem.setIcon(icon);
            }

    }
}
