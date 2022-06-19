package com.nextsuntech.vmail.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nextsuntech.vmail.Dashboard.DashboardActivity;
import com.nextsuntech.vmail.R;
import com.nextsuntech.vmail.User.LoginActivity;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FILE_NAME = "SavedUserData";
    RelativeLayout logoutBT;
    TextView profileEmailTV;
    TextView profileNameTV;
    TextView profilePhoneTV;
    ImageView backIV;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        logoutBT = findViewById(R.id.bt_profile_logout);
        profileEmailTV = findViewById(R.id.tv_profile_email);
        profileNameTV = findViewById(R.id.tv_profile_name);
        profilePhoneTV = findViewById(R.id.tv_profile_phone);
        backIV = findViewById(R.id.iv_profile_back);

        logoutBT.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "data not found");
        String name = sharedPreferences.getString("name", "data not found");
        String phone = sharedPreferences.getString("phone", "data not found");
        profileEmailTV.setText(email);
        profileNameTV.setText(name);
        profilePhoneTV.setText(phone);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_profile_logout:
                logout();
                break;

            case R.id.iv_profile_back:
                finish();
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                break;
        }
    }

    private void logout() {
        mAuth.signOut();
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}