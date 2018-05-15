package com.paranoid.runordie.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.paranoid.runordie.App;
import com.paranoid.runordie.R;
import com.paranoid.runordie.helpers.PreferenceHelper;
import com.paranoid.runordie.models.Session;
import com.paranoid.runordie.models.User;
import com.paranoid.runordie.models.httpResponses.LoginResponse;
import com.paranoid.runordie.models.httpResponses.RegisterResponse;
import com.paranoid.runordie.server.ApiClient;
import com.paranoid.runordie.server.Callback;
import com.paranoid.runordie.server.NetworkException;
import com.paranoid.runordie.utils.SnackbarUtils;

public class AuthActivity extends BaseActivity {

    private EditText mEtEmail, mEtFirstName, mEtLastName, mEtPassword, mEtPasswordRepeat;
    private Button mBtnSignUp, mBtnSignIn;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        setActionBarTitle(R.string.auth_title);
        findViews();
        switchMode(MODE.SIGN_IN);
    }

    private void findViews() {
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
    }


    private void switchMode(MODE mode) {
        switch (mode) {
            case SIGN_IN:
                currentMode = MODE.SIGN_IN;
                mBtnSignIn.setBackgroundColor(
                        getResources().getColor(R.color.auth_active_btn_background)
                );
                mBtnSignUp.setBackgroundColor(
                        getResources().getColor(R.color.auth_unactive_btn_background)
                );
                mBtnSignUp.setPaintFlags(mBtnSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                mBtnSignIn.setPaintFlags(mBtnSignIn.getPaintFlags() & ~(Paint.UNDERLINE_TEXT_FLAG));
                mEtFirstName.setVisibility(View.GONE);
                mEtLastName.setVisibility(View.GONE);
                mEtPasswordRepeat.setVisibility(View.GONE);
                break;
            case SIGN_UP:
                currentMode = MODE.SIGN_UP;
                mBtnSignUp.setBackgroundColor(
                        getResources().getColor(R.color.auth_active_btn_background)
                );
                mBtnSignIn.setBackgroundColor(
                        getResources().getColor(R.color.auth_unactive_btn_background)
                );
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
        User user = new User(
                mEtEmail.getText().toString(),
                mEtFirstName.getText().toString(),
                mEtLastName.getText().toString(),
                mEtPassword.getText().toString()
        );
        ApiClient.getInstance().register(user, new Callback<RegisterResponse>() {
            @Override
            public void success(RegisterResponse result) {

            }

            @Override
            public void failure(NetworkException exception) {

            }
        });
    }


    private void attemptSignIn() {
        CoordinatorLayout mainLayout = findViewById(R.id.auth_root_layout);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);


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
            final User user = new User(email, password);

            //TODO
            ApiClient.getInstance().login(user, new Callback<LoginResponse>() {
                @Override
                public void success(LoginResponse result) {
                    user.setFirstName(result.getFirstName());
                    user.setLastName(result.getLastName());
                    PreferenceHelper.saveUser(user);
                    Session activeSession = new Session(user, result.getToken());
                    App.getInstance().getState().setActiveSession(activeSession);
                    startActivity(new Intent(
                            getApplicationContext(),
                            MainActivity.class
                    ));
                    finish();
                }

                @Override
                public void failure(NetworkException exception) {
                    SnackbarUtils.showSnackbar(exception);
                    showProgress(false);
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 4;
    }

    @Override
    public CoordinatorLayout getRootLayout() {
        return findViewById(R.id.auth_root_layout);
    }
}

