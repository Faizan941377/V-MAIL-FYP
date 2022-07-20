package com.nextsuntech.vmail.Dashboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nextsuntech.vmail.Compose.ComposeActivity;
import com.nextsuntech.vmail.Inbox.InboxActivity;
import com.nextsuntech.vmail.Profile.ProfileActivity;
import com.nextsuntech.vmail.R;
import com.nextsuntech.vmail.Sent.SentActivity;
import com.nextsuntech.vmail.Splash.SplashActivity;
import com.nextsuntech.vmail.User.LoginActivity;

import java.util.ArrayList;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    CardView composeBT;
    CardView inboxBT;
    CardView sentBT;
    CardView profileBT;
    TextView speechTextTV;
    ImageView speakIV;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        composeBT = findViewById(R.id.cv_dashboard_compose);
        inboxBT = findViewById(R.id.cd_dashboard_inbox);
        sentBT = findViewById(R.id.cd_dashboard_sent);
        profileBT = findViewById(R.id.cv_dashboard_profile);
        speechTextTV = findViewById(R.id.tv_dashboard_speechText);
        speakIV = findViewById(R.id.iv_dashboard_speak);

        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.UK);
                String help = "Welcome to V mail app tell me how can I help You";
                textToSpeech.speak(help, TextToSpeech.QUEUE_ADD, null);
            }
        });

        speakIV.setOnClickListener(this);

        Handler handler = new Handler();
        handler.postDelayed(this::openMic, 5000);
    }

    private void openMic() {
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
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechTextTV.setText(result.get(0));

                    if (speechTextTV.getText().toString().equals("compose")) {
                        startActivity(new Intent(getApplicationContext(), ComposeActivity.class));
                        finish();
                        break;
                    }
                    if (speechTextTV.getText().toString().length() > 7) {
                        openMic();
                    }
                }

                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechTextTV.setText(result1.get(0));

                    if (speechTextTV.getText().toString().equals("inbox")) {
                        startActivity(new Intent(getApplicationContext(), InboxActivity.class));
                        finish();
                        break;
                    }
                    if (speechTextTV.getText().toString().length() != 5) {
                        openMic();
                    }
                }

                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result2 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechTextTV.setText(result2.get(0));

                    if (speechTextTV.getText().toString().equals("profile")) {
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                        break;
                    }
                    if (speechTextTV.getText().toString().length() != 7) {
                        openMic();
                    }
                }

                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result3 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speechTextTV.setText(result3.get(0));

                    if (speechTextTV.getText().toString().equals("send")) {
                        startActivity(new Intent(getApplicationContext(), SentActivity.class));
                        finish();
                        break;
                    }
                    if (speechTextTV.getText().toString().length() != 4) {
                        openMic();
                    }
                }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_dashboard_speak:
                openMic();
                break;
        }
    }
}