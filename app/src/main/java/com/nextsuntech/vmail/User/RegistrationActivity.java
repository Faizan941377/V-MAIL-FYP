package com.nextsuntech.vmail.User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.nextsuntech.vmail.R;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout registerBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerBT = findViewById(R.id.bt_register);

        registerBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_register:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                break;
        }
    }
}