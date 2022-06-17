package com.nextsuntech.vmail.Compose;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nextsuntech.vmail.R;

import java.util.ArrayList;
import java.util.Locale;

public class ComposeActivity extends AppCompatActivity {

    TextView composeTV;
    EditText senderEmailET;
    EditText subjectET;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        composeTV = findViewById(R.id.tv_compose_pleaseRecodeMassage);
        senderEmailET = findViewById(R.id.et_compose_senderEmail);
        subjectET = findViewById(R.id.et_compose_subject);


        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.UK);

                composeTV.setText("Please record a vice email");
                textToSpeech.speak(composeTV.getText().toString(), TextToSpeech.QUEUE_ADD, null);
            }
            senderEmailSpeaking();
        });
    }

    private void senderEmailSpeaking() {
        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.UK);
                textToSpeech.speak(senderEmailET.getText().toString(), TextToSpeech.QUEUE_ADD, null);
            }
        });

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
                    senderEmailET.setText(result.get(0));
                }
                //OpenMic1();
                break;
        }
    }
}