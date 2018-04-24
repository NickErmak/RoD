package com.paranoid.runordie.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.paranoid.runordie.R;
import com.paranoid.runordie.network.NetworkUtils;
import com.paranoid.runordie.utils.PreferenceUtils;

public class AuthActivity extends BaseActivity {

    private EditText mEtEmail, mEtFirstName, mEtLastName, mEtPassword, mEtPasswordRepeat;
    private Button mBtnSignUp, mBtnSignIn;
    private View mProgressView;

    private enum MODE {SIGN_IN, SIGN_UP}
    private MODE currentMode = MODE.SIGN_UP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        setActionBarTitle(R.string.auth_title);

        mEtEmail = (EditText) findViewById(R.id.auth_et_email);
        mEtFirstName = (EditText) findViewById(R.id.auth_et_firstName);
        mEtLastName = (EditText) findViewById(R.id.auth_et_lastName);
        mEtPassword = (EditText) findViewById(R.id.auth_et_password);
        mEtPasswordRepeat = (EditText) findViewById(R.id.auth_et_passwordRepeat);

        mBtnSignUp = (Button) findViewById(R.id.auth_btn_signUp);
        mBtnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentMode == MODE.SIGN_UP) {
                    attemptSignUp();
                } else {
                    switchMode(MODE.SIGN_UP);
                }
            }
        });

        mBtnSignIn = (Button) findViewById(R.id.auth_btn_signIn);
        mBtnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMode == MODE.SIGN_IN) {
                    attemptSignIn();
                } else {
                    switchMode(MODE.SIGN_IN);
                }
            }
        });

        mProgressView = findViewById(R.id.login_progress);
        switchMode(MODE.SIGN_UP);
    }

    private void switchMode(MODE mode) {
        switch (mode) {
            case SIGN_IN:
                currentMode = MODE.SIGN_IN;
                mBtnSignUp.setPaintFlags(mBtnSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                mBtnSignIn.setPaintFlags(mBtnSignIn.getPaintFlags() & ~(Paint.UNDERLINE_TEXT_FLAG));
                mEtFirstName.setVisibility(View.GONE);
                mEtLastName.setVisibility(View.GONE);
                mEtPasswordRepeat.setVisibility(View.GONE);
                break;
            case SIGN_UP:
                currentMode = MODE.SIGN_UP;
                mBtnSignIn.setPaintFlags(mBtnSignIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                mBtnSignUp.setPaintFlags(mBtnSignUp.getPaintFlags() & ~(Paint.UNDERLINE_TEXT_FLAG));
                mEtFirstName.setVisibility(View.VISIBLE);
                mEtLastName.setVisibility(View.VISIBLE);
                mEtPasswordRepeat.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void attemptSignUp() {
        //TODO: check fields for errors


    }


    private void attemptSignIn() {
        //TODO: check if login background is not already running
        mEtEmail.setError(null);
        mEtPassword.setError(null);

        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!(TextUtils.isEmpty(password) || isPasswordValid(password))) {
            mEtPassword.setError(getString(R.string.error_invalid_password));
            focusView = mEtPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEtEmail.setError(getString(R.string.error_field_required));
            focusView = mEtEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEtEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEtEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            PreferenceUtils.setLogin(email);
            PreferenceUtils.setPassword(password);
            NetworkUtils.login(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void showProgress(final boolean show) {
        // The ViewPropertyAnimator APIs are not available, so simply show
        // and hide the relevant UI components.
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}

