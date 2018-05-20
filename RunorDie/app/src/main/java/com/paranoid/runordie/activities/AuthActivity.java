package com.paranoid.runordie.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.paranoid.runordie.R;
import com.paranoid.runordie.models.User;
import com.paranoid.runordie.models.httpResponses.LoginResponse;
import com.paranoid.runordie.models.httpResponses.RegisterResponse;
import com.paranoid.runordie.server.ApiClient;
import com.paranoid.runordie.server.Callback;
import com.paranoid.runordie.server.NetworkException;
import com.paranoid.runordie.utils.SessionUtils;
import com.paranoid.runordie.utils.SnackbarUtils;
import com.paranoid.runordie.utils.ValidateCredentialsUtils;
import com.paranoid.runordie.utils.ViewUtils;

public class AuthActivity extends BaseActivity {

    private String fieldRequiredError,
            invalidEmailError,
            invalidFirstNameError,
            invalidLastNameError,
            shortPasswordError,
            notSamePasswordsError;

    private int passwordMinLength;

    private TextInputLayout tilFirstName,
            tilLastName,
            tillPasswordRepeat;

    private EditText etEmail,
            etFirstName,
            etLastName,
            etPassword,
            etPasswordRepeat;

    private Button btnSignUp,
            btnSignIn;

    public enum MODE {SIGN_IN, SIGN_UP}

    private MODE currentMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        setActionBarTitle(R.string.auth_title);
        initViews();
        initFromResources();
        switchMode(MODE.SIGN_IN);
    }

    private void initViews() {
        tilFirstName = (TextInputLayout) findViewById(R.id.auth_til_firstName);
        tilLastName = (TextInputLayout) findViewById(R.id.auth_til_lastName);
        tillPasswordRepeat = (TextInputLayout) findViewById(R.id.auth_til_passwordRepeat);

        etEmail = (EditText) findViewById(R.id.auth_et_email);
        etFirstName = (EditText) findViewById(R.id.auth_et_firstName);
        etLastName = (EditText) findViewById(R.id.auth_et_lastName);
        etPassword = (EditText) findViewById(R.id.auth_et_password);
        etPasswordRepeat = (EditText) findViewById(R.id.auth_et_passwordRepeat);
        btnSignUp = (Button) findViewById(R.id.auth_btn_signUp);
        btnSignIn = (Button) findViewById(R.id.auth_btn_signIn);

        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentMode == MODE.SIGN_UP) {
                    attemptSignUp();
                } else {
                    switchMode(MODE.SIGN_UP);
                }
            }
        });

        btnSignIn.setOnClickListener(new OnClickListener() {
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

    private void initFromResources() {
        fieldRequiredError = getString(R.string.error_field_required);
        invalidEmailError = getString(R.string.error_invalid_email);
        invalidFirstNameError = getString(R.string.error_invalid_firstName);
        invalidLastNameError = getString(R.string.error_invalid_lastName);
        shortPasswordError = getString(R.string.error_short_password);
        notSamePasswordsError = getString(R.string.error_not_same_passwords);
        passwordMinLength = getResources().getInteger(R.integer.password_min_length);
    }

    private void switchMode(MODE mode) {
        clearErrors();
        switch (mode) {
            case SIGN_IN:
                currentMode = MODE.SIGN_IN;
                switchActiveButton(btnSignIn, btnSignUp);
                ViewUtils.switchVisibility(false, tilFirstName, tilLastName, tillPasswordRepeat);
                break;

            case SIGN_UP:
                currentMode = MODE.SIGN_UP;
                switchActiveButton(btnSignUp, btnSignIn);
                ViewUtils.switchVisibility(true, tilFirstName, tilLastName, tillPasswordRepeat);
                break;
        }
    }

    private void clearErrors() {
        etEmail.setError(null);
        etFirstName.setError(null);
        etLastName.setError(null);
        etPassword.setError(null);
        etPasswordRepeat.setError(null);
    }

    private void switchActiveButton(Button btnToActivate, Button btnToDeactivate) {
        btnToActivate.setBackgroundColor(
                getResources().getColor(R.color.auth_active_btn_background)
        );
        btnToDeactivate.setBackgroundColor(
                getResources().getColor(R.color.auth_unactive_btn_background)
        );
        btnToActivate.setPaintFlags(btnSignIn.getPaintFlags() & ~(Paint.UNDERLINE_TEXT_FLAG));
        btnToDeactivate.setPaintFlags(btnSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void attemptSignUp() {
        hideInputKeyboard();
        clearErrors();

        String email = etEmail.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordRepeat = etPasswordRepeat.getText().toString().trim();

        boolean cancel = false;

        if (email.length() == 0) {
            etEmail.setError(fieldRequiredError);
            cancel = true;
        } else if (!ValidateCredentialsUtils.validateEmail(email)) {
            etEmail.setError(invalidEmailError);
            cancel = true;
        }

        if (firstName.length() == 0) {
            etFirstName.setError(fieldRequiredError);
            cancel = true;
        } else if (!ValidateCredentialsUtils.validateName(firstName)) {
            etFirstName.setError(invalidFirstNameError);
            cancel = true;
        }

        if (lastName.length() == 0) {
            etLastName.setError(fieldRequiredError);
            cancel = true;
        } else if (!ValidateCredentialsUtils.validateName(lastName)) {
            etLastName.setError(invalidLastNameError);
            cancel = true;
        }

        if (password.length() == 0) {
            etPassword.setError(fieldRequiredError);
            cancel = true;
        } else if (password.length() < passwordMinLength) {
            etPassword.setError(shortPasswordError);
            cancel = true;
        }

        if (passwordRepeat.length() == 0) {
            etPasswordRepeat.setError(fieldRequiredError);
            cancel = true;
        } else if (!password.equals(passwordRepeat)) {
            etPasswordRepeat.setError(notSamePasswordsError);
            cancel = true;
        }

        if (!cancel) {
            showProgress(true);
            final User user = new User(
                    email,
                    firstName,
                    lastName,
                    password
            );

            ApiClient.getInstance().register(user, new Callback<RegisterResponse>() {
                @Override
                public void success(RegisterResponse result) {
                    SessionUtils.createSession(user, result.getToken());
                    routeToMain();
                }

                @Override
                public void failure(NetworkException exception) {
                    SnackbarUtils.showSnack(exception);
                    showProgress(false);
                }
            });
        }
    }


    private void attemptSignIn() {
        hideInputKeyboard();

        etEmail.setError(null);
        etPassword.setError(null);

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;

        if (email.length() == 0) {
            etEmail.setError(fieldRequiredError);
            cancel = true;
        } else if (!ValidateCredentialsUtils.validateEmail(email)) {
            etEmail.setError(invalidEmailError);
            cancel = true;
        }

        if (password.length() == 0) {
            etPassword.setError(fieldRequiredError);
            cancel = true;
        } else if (password.length() < passwordMinLength) {
            etPassword.setError(shortPasswordError);
            cancel = true;
        }

        if (!cancel) {
            showProgress(true);
            final User user = new User(email, password);

            ApiClient.getInstance().login(user, new Callback<LoginResponse>() {
                @Override
                public void success(LoginResponse result) {
                    user.setFirstName(result.getFirstName());
                    user.setLastName(result.getLastName());
                    SessionUtils.createSession(user, result.getToken());
                    routeToMain();
                }

                @Override
                public void failure(NetworkException exception) {
                    SnackbarUtils.showSnack(exception);
                    showProgress(false);
                }
            });
        }
    }

    private void routeToMain() {
        startActivity(new Intent(
                getApplicationContext(),
                MainActivity.class
        ));
        finish();
    }

    private void hideInputKeyboard() {
        CoordinatorLayout mainLayout = findViewById(R.id.auth_root_layout);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
        }
    }

    @Override
    public CoordinatorLayout getRootLayout() {
        return findViewById(R.id.auth_root_layout);
    }
}
