package com.neuroengine.beatzone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;

/**
 * Created by usuariodebian on 24/01/17.
 */

public class LoginGoogle extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "LoginGoogle";
    private static final int RC_SIGN_IN = 9001;

    private TextView mStatusTextView;
    private TextView mGivenNameTextView;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_google_login);

        mStatusTextView = (TextView) findViewById(R.id.google_signin_status);
        mGivenNameTextView = ((TextView)findViewById(R.id.google_signin_user));
        findViewById(R.id.google_signin_button).setOnClickListener(this);
        findViewById(R.id.google_signout_button).setOnClickListener(this);
        findViewById(R.id.google_revoke_button).setOnClickListener(this);
        findViewById(R.id.google_back_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("1059559479089-tdmtn267k8ptt3vuh9c5b2vca5qnl87e.apps.googleusercontent.com")
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton)findViewById(R.id.google_signin_button);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);


    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (opr.isDone()) {
            Log.d(TAG, "Sign in on cache");

            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {

                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(getString(R.string.signed_in_fmt));
            mGivenNameTextView.setText(acct.getDisplayName());
            LoggedUser lu = LoggedUser.getInstance();
            lu.setId(acct.getId());
            lu.setImagemUri(acct.getPhotoUrl());
//            this.getIntent().putExtra(LoggedUser.LoggedUserId, lu);
//            setResult(RESULT_OK);
//            finish();
//            onBackPressed();
            updateUI(true);
        } else {
            updateUI(false);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        updateUI(true);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(false);
                    }
                }

        );
        updateUI(false);
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(false);
                    }
                }
        );
        updateUI(false);
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.google_signin_layout).setVisibility(View.GONE);
            findViewById(R.id.google_signed_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.google_signout_layout).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText("");
            mGivenNameTextView.setText("");
            findViewById(R.id.google_signin_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.google_signed_layout).setVisibility(View.GONE);
            findViewById(R.id.google_signout_layout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.google_signin_button:
                signIn();
                break;
            case R.id.google_signout_button:
                signOut();
                break;
            case R.id.google_revoke_button:
                revokeAccess();
                break;
            case R.id.google_back_button:
                voltarAtividade();
                break;
        }
    }

    public void voltarAtividade() {
        onBackPressed();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }


}
