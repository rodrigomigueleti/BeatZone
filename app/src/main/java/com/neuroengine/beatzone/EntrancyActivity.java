package com.neuroengine.beatzone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by usuariodebian on 14/03/17.
 */

public class EntrancyActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();
        if (LoggedUser.getInstance().getId().isEmpty()) {
            Intent intent = new Intent(getApplicationContext(), LoginGoogle.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
        }
    }
}
