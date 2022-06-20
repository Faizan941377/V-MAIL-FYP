package com.nextsuntech.vmail.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telecom.RemoteConference;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
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

import java.util.ArrayList;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FILE_NAME = "SavedUserData";
    TextView loginBT;
    TextView registerOptionTV;
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
        registerOptionTV = findViewById(R.id.tv_yes);

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
                if (mAuth.getCurrentUser() == null) {
                    String welcomeMsg = "Welcome to v mail app registration form please tell me the details";
                    textToSpeech.speak(welcomeMsg, TextToSpeech.QUEUE_ADD, null);
                    getDetailForRegistration();
                }
            }
        });
    }

    private void getDetailForRegistration() {
        if (nameET.length() == 0) {
            String name = "what is your name";
            textToSpeech.speak(name, TextToSpeech.QUEUE_ADD, null);
            Handler handler = new Handler();
            handler.postDelayed(this::openMic1, 8000);
        }
    }

    private void openMic2() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi Speak Something");

        try {
            startActivityForResult(intent, 2);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openMic1() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi Speak Something");

        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                nameET.setText(result.get(0));
                String email = "what is your email";
                textToSpeech.speak(email,TextToSpeech.QUEUE_ADD,null);
                Handler handler = new Handler();
                handler.postDelayed(this::openMic2, 3000);
            }
        }
        if (requestCode == 2){
            if (resultCode == RESULT_OK && null != data){
                ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                emailET.setText(result1.get(0).replaceAll("\\s+", "") + "@gmail.com");

                String phone = "what is your mobile number";
                textToSpeech.speak(phone,TextToSpeech.QUEUE_ADD,null);
                Handler handler = new Handler();
                handler.postDelayed(this::openMic3, 3000);
            }
        }
        if (requestCode ==3){
            if (resultCode == RESULT_OK && null !=data){
                ArrayList<String> result2 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                phoneET.setText(result2.get(0).replaceAll("\\s+",""));

                String password = "what is your password";
                textToSpeech.speak(password,TextToSpeech.QUEUE_ADD,null);
                Handler handler = new Handler();
                handler.postDelayed(this::openMic4, 3000);
            }
        }

        if (requestCode ==4){
            if (resultCode == RESULT_OK && null !=data){
                ArrayList<String> result3 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                passwordET.setText(result3.get(0).replaceAll("\\+",""));

                reviewDetails();
            }
        }

        if (requestCode==5){
            if (resultCode == RESULT_OK && null !=data){
                ArrayList<String> result4 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                registerOptionTV.setText(result4.get(0));
            }
            if (registerOptionTV.getText().toString().equals("yes")){
                registration();
            }
            if (registerOptionTV.getText().toString().equals("no")){
                String clearData = "we your clear all provided data please reopen the app";
                textToSpeech.speak(clearData,TextToSpeech.QUEUE_ADD,null);
                nameET.setText("");
                emailET.setText("");
                phoneET.setText("");
                passwordET.setText("");
            }
        }
    }

    private void reviewDetails() {
        String listenMessage = "Please confirm your provided details";
        textToSpeech.speak(listenMessage, TextToSpeech.QUEUE_ADD, null);
        String name = "your name is";
        textToSpeech.speak(name,TextToSpeech.QUEUE_ADD,null);
        textToSpeech.speak(nameET.getText().toString(),TextToSpeech.QUEUE_ADD,null);
        String email = "your email is";
        textToSpeech.speak(email,TextToSpeech.QUEUE_ADD,null);
        textToSpeech.speak(emailET.getText().toString(),TextToSpeech.QUEUE_ADD,null);
        String phone = "your phone number is";
        textToSpeech.speak(phone,TextToSpeech.QUEUE_ADD,null);
        textToSpeech.speak(phoneET.getText().toString(),TextToSpeech.QUEUE_ADD,null);
        String password = "Your password is";
        textToSpeech.speak(password,TextToSpeech.QUEUE_ADD,null);
        textToSpeech.speak(passwordET.getText().toString(),TextToSpeech.QUEUE_ADD,null);

        String askQues = "if you want to register then say yes or don't want to register say no";
        textToSpeech.speak(askQues,TextToSpeech.QUEUE_ADD,null);

        Handler handler = new Handler();
        handler.postDelayed(this::openMic5, 25000);
    }

    private void openMic5() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi Speak Something");

        try {
            startActivityForResult(intent, 5);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void openMic4() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi Speak Something");

        try {
            startActivityForResult(intent, 4);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openMic3() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi Speak Something");

        try {
            startActivityForResult(intent, 3);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
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
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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
                                                    storeUserRegisteredDataSharedPref(name, email, phone, password);
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
        SharedPreferences.Editor editor = getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("phone", phone);
        editor.putString("password", password);
        editor.apply();
    }
}