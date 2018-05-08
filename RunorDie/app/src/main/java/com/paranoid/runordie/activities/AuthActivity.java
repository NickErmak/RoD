package com.paranoid.runordie.activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.paranoid.runordie.R;
import com.paranoid.runordie.models.User;
import com.paranoid.runordie.server.ApiClient;
import com.paranoid.runordie.server.NetworkProvider;
import com.paranoid.runordie.server.NetworkProviderTasks;
import com.paranoid.runordie.server.NetworkUtils;
import com.paranoid.runordie.utils.PreferenceUtils;

public class AuthActivity extends BaseActivity {

    private EditText mEtEmail, mEtFirstName, mEtLastName, mEtPassword, mEtPasswordRepeat;
    private Button mBtnSignUp, mBtnSignIn;

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
            showProgress(true);
            PreferenceUtils.setLogin(email);
            PreferenceUtils.setPassword(password);
            //TODO
            NetworkProviderTasks.loginAsync(new User(email, password));
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

    @Override
    public CoordinatorLayout getRootLayout() {
        return findViewById(R.id.auth_root_layout);
    }
}

