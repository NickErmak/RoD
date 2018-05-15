package com.paranoid.runordie.helpers;

import android.graphics.Paint;
import android.view.View;
import android.widget.Button;

import com.paranoid.runordie.R;
import com.paranoid.runordie.activities.AuthActivity;

public class AuthModeHelper {

    public enum MODE {SIGN_IN, SIGN_UP}

    private MODE currentMode;
    private Button signIn, signUp;

    public AuthModeHelper(Button signIn, Button signUp, MODE currentMode) {
        this.currentMode = currentMode;
        this.signIn = signIn;
        this.signUp = signUp;
        switchMode(currentMode);
    }

    private void switchMode(MODE mode) {
        switch (mode) {
            case SIGN_IN:
                currentMode = AuthActivity.MODE.SIGN_IN;
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
                currentMode = AuthActivity.MODE.SIGN_UP;
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

    private void switchMode(Button btnToActivate, Button btnToDeactivate) {
        btnToActivate.setBackgroundColor(
                getResources().getColor(R.color.auth_active_btn_background)
        );
        mBtnSignIn.setPaintFlags(mBtnSignIn.getPaintFlags() & ~(Paint.UNDERLINE_TEXT_FLAG));


        mBtnSignUp.setBackgroundColor(
                getResources().getColor(R.color.auth_unactive_btn_background)
        );
        mBtnSignUp.setPaintFlags(mBtnSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mEtFirstName.setVisibility(View.GONE);
        mEtLastName.setVisibility(View.GONE);
        mEtPasswordRepeat.setVisibility(View.GONE);
    }
}
