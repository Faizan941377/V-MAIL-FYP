package com.nextsuntech.vmail.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nextsuntech.vmail.Dashboard.DashboardActivity;
import com.nextsuntech.vmail.R;

import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FILE_NAME = "SavedUserData" ;
    TextView loginBT;
    EditText nameET;
    EditText emailET;
    EditText phoneET;
    EditText passwordET;
    RelativeLayout registerBT;
    TextToSpeech textToSpeech;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        loginBT = findViewById(R.id.tv_register_login);
        registerBT = findViewById(R.id.bt_register);
        nameET = findViewById(R.id.et_register_name);
        emailET = findViewById(R.id.et_register_email);
        phoneET = findViewById(R.id.et_register_phone);
        passwordET = findViewById(R.id.et_register_password);

        loginBT.setOnClickListener(this);
        registerBT.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            //if user is already login then goto dashboard activity
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            finish();
        }

        //authentication initialized

        mAuth = FirebaseAuth.getInstance();


        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.UK);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_register:
                registration();
                break;
            case R.id.tv_register_login:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                break;
        }
    }

    private void registration() {
        String name = nameET.getText().toString();
        String email = emailET.getText().toString();
        String phone = phoneET.getText().toString();
        String password = passwordET.getText().toString();

        if (name.length() == 0) {
            String setNameError = "Please provide you name";
            textToSpeech.speak(setNameError, TextToSpeech.QUEUE_ADD, null);
            nameET.setError(setNameError);

        } else if (!email.matches(emailPattern)) {
            String setErrorEmail = "Email Does not match to the pattern";
            textToSpeech.speak(setErrorEmail, TextToSpeech.QUEUE_ADD, null);
            emailET.setError(setErrorEmail);

        } else if (phone.length() < 11) {
            String setPhoneError = "Phone number is less than 11";
            textToSpeech.speak(setPhoneError, TextToSpeech.QUEUE_ADD, null);
            phoneET.setError(setPhoneError);

        } else if (password.isEmpty() || password.length() < 6) {
            String setPasswordError = "Your password is less than 6 integer numbers";
            textToSpeech.speak(setPasswordError, TextToSpeech.QUEUE_ADD, null);
            passwordET.setError(setPasswordError);

        } else if (name.isEmpty() || name.length() == 0 && email.isEmpty() || email.matches(emailPattern)
                && phone.isEmpty() || phone.length() == 11 && password.isEmpty() || password.length() == 6) {

            String congratulations = "congratulations your provided detail is approved";
            textToSpeech.speak(congratulations, TextToSpeech.QUEUE_ADD, null);

            progressDialog.show();
            progressDialog.setMessage("Registering...");
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Registration");


            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //we will store the additional fields in firebase database
                                Users users = new Users(
                                        name
                                        , email
                                        , phone
                                        , password
                                );
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    storeUserRegisteredDataSharedPref(name,email,phone,password);
                                                    progressDialog.dismiss();
                                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                                    Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(RegistrationActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void storeUserRegisteredDataSharedPref(String name, String email, String phone, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME,MODE_PRIVATE).edit();
        editor.putString("name",name);
        editor.putString("email",email);
        editor.putString("phone",phone);
        editor.putString("password",password);
        editor.apply();
    }
}