package com.nextsuntech.vmail.User;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FILE_NAME = "SavedUserData";
    private static final int REQUEST_CODE = 1001;
    EditText emailET;
    EditText passwordET;
    TextView signUpTV;
    TextView loginByVoice;
    ImageView loginWithVoiceIV;
    ImageView loginWithBiometric;
    TextToSpeech textToSpeech;
    RelativeLayout loginBT;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            //if user is already login then goto dashboard activity
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            finish();
        }

        emailET = findViewById(R.id.et_login_email);
        passwordET = findViewById(R.id.et_login_password);
        signUpTV = findViewById(R.id.tv_login_signUp);
        loginBT = findViewById(R.id.bt_login);
        loginWithVoiceIV = findViewById(R.id.voice_login);
        loginByVoice = findViewById(R.id.tv_user_loginVoice);
        loginWithBiometric = findViewById(R.id.biometric_login);


        progressDialog = new ProgressDialog(this);

        loginBT.setOnClickListener(this);
        signUpTV.setOnClickListener(this);
        loginWithVoiceIV.setOnClickListener(this);
        loginWithBiometric.setOnClickListener(this);

        biometric();
        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.UK);
                String email = "welcome to login activity if you are already registered then scan your finger otherwise" +
                        "you will login via voice and using keyboard";
                textToSpeech.speak(email, TextToSpeech.QUEUE_ADD, null);

                Handler handler = new Handler();
                handler.postDelayed(this::loginWithBiometric, 2000);
            }
        });


    }

    private void loginWithBiometric() {
        biometricPrompt.authenticate(promptInfo);
    }

    private void loginWithVoice() {

        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.UK);
                String email = "what is your email";
                textToSpeech.speak(email, TextToSpeech.QUEUE_ADD, null);

                Handler handler = new Handler();
                handler.postDelayed(this::openMic1, 2000);
            }
        });

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                emailET.setText(result.get(0).replaceAll("\\s+", "") + "@gmail.com");


                String password = "what is your password";
                textToSpeech.speak(password, TextToSpeech.QUEUE_ADD, null);
                Handler handler = new Handler();
                handler.postDelayed(this::openMic2, 2000);
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                passwordET.setText(result.get(0));

                Handler handler = new Handler();
                handler.postDelayed(this::confirmLoginDetails, 2000);
            }
        }
        if (requestCode == 3) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                loginByVoice.setText(result.get(0));
            }
            if (loginByVoice.getText().toString().equals("yes")) {
                login();
            }
            if (loginByVoice.getText().toString().equals("no")) {
                String clearData = "we are clear your all provided data please try again";
                textToSpeech.speak(clearData, TextToSpeech.QUEUE_ADD, null);
                emailET.setText("");
                passwordET.setText("");
            }
        }
    }

    private void confirmLoginDetails() {
        String confirm = "please confirm your provided details";
        textToSpeech.speak(confirm, TextToSpeech.QUEUE_ADD, null);
        String email = "your email is";
        textToSpeech.speak(email, TextToSpeech.QUEUE_ADD, null);
        textToSpeech.speak(emailET.getText().toString(), TextToSpeech.QUEUE_ADD, null);
        String password = "your password is";
        textToSpeech.speak(password, TextToSpeech.QUEUE_ADD, null);
        textToSpeech.speak(passwordET.getText().toString(), TextToSpeech.QUEUE_ADD, null);


        String ask = "if you want to login please say yes or don't want to login say no";
        textToSpeech.speak(ask, TextToSpeech.QUEUE_ADD, null);

        Handler handler = new Handler();
        handler.postDelayed(this::openMic3, 18000);
    }


    private void biometric() {
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "No biometric features available on this device", Toast.LENGTH_SHORT).show();
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "Biometric features are currently unavailable", Toast.LENGTH_SHORT).show();
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;

        }

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                SharedPreferences sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
                String email = sharedPreferences.getString("email", "data not found");
                String password = sharedPreferences.getString("password", "data not found");

                performAuth(email, password);
                String pass = "Authentication succeeded";
                textToSpeech.speak(pass, TextToSpeech.QUEUE_ADD, null);
                Toast.makeText(getApplicationContext(),
                        pass, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                String failed = "Authentication failed";
                textToSpeech.speak(failed, TextToSpeech.QUEUE_ADD, null);
                Toast.makeText(getApplicationContext(), failed,
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

    }

    private void performAuth(String email, String password) {
        progressDialog.show();
        progressDialog.setMessage("Logging...");
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Login");
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            progressDialog.dismiss();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;

            case R.id.tv_login_signUp:
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
                finish();
                break;

            case R.id.voice_login:
                loginWithVoice();
                break;

            case R.id.biometric_login:
                biometricPrompt.authenticate(promptInfo);
                break;
        }
    }

    private void login() {
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();


        if (email.isEmpty() || email.length() == 0) {
            emailET.setError("Please enter your email");
        } else if (password.isEmpty() || password.length() < 6) {
            passwordET.setError("Your password should be 6 integer numbers");
        } else {

            progressDialog.show();
            progressDialog.setMessage("Logging...");
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Login");

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login Failed Please try Again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}