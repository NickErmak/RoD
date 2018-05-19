package com.paranoid.runordie.helpers;

import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paranoid.runordie.R;
import com.paranoid.runordie.models.User;

public class NavigationHelper {

    public static void refreshHeaderLogin(NavigationView navigationView, User user) {
        View navHeader = navigationView.getHeaderView(0);

        TextView nameView = navHeader.findViewById(R.id.nav_header_name);
        TextView emailView = navHeader.findViewById(R.id.nav_header_email);

        String fullName = user.getFirstName() + ' ' + user.getLastName();

        nameView.setText(fullName);
        emailView.setText(user.getEmail());
    }
}
