package com.nextsuntech.vmail.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.nextsuntech.vmail.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout loginBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBT = findViewById(R.id.bt_login);



        loginBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_login:
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
                finish();
                break;
        }
    }
}