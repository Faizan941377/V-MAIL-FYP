package com.nextsuntech.vmail.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
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
import com.nextsuntech.vmail.User.Users;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FILE_NAME = "SavedUserData";
    RelativeLayout logoutBT;
    TextView profileEmailTV;
    TextView profileNameTV;
    TextView profilePhoneTV;
    TextView passwordTV;
    ImageView backIV;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    private String userID;
    DatabaseReference reference;


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
        passwordTV = findViewById(R.id.tv_profile_password);
        backIV = findViewById(R.id.iv_profile_back);

        logoutBT.setOnClickListener(this);
        backIV.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "data not found");
        String name = sharedPreferences.getString("name", "data not found");
        String phone = sharedPreferences.getString("phone", "data not found");
        String password = sharedPreferences.getString("password", "data not found");
        profileEmailTV.setText(email);
        profileNameTV.setText(name);
        profilePhoneTV.setText(phone);
        passwordTV.setText(password);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);
                if (userProfile != null) {

                    String email = userProfile.getEmail();
                    String name = userProfile.getName();
                    String phone = userProfile.getPhone();
                    String password = userProfile.getPassword();
                    profileEmailTV.setText(email);
                    profileNameTV.setText(name);
                    profilePhoneTV.setText(phone);
                    passwordTV.setText(password);
                    storeUserRegisteredDataSharedPref(name, email, phone,password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void storeUserRegisteredDataSharedPref(String name, String email, String phone,String password) {
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("phone", phone);
        editor.putString("password",password);
        editor.apply();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
        finish();
    }
}