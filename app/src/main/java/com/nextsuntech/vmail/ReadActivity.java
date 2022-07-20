package com.nextsuntech.vmail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class ReadActivity extends AppCompatActivity implements View.OnClickListener {

    Email email;
    TextView tofrom,subject,body;
    TextToSpeech textToSpeech;
    ImageView backIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        email = (Email) getIntent().getSerializableExtra("Email");
        tofrom = findViewById(R.id.txttofrom);
        subject = findViewById(R.id.txtrsubject);
        body = findViewById(R.id.txtrbody);
        backIV = findViewById(R.id.iv_readActivity_back);

        tofrom.setText(email.getRecid());
        subject.setText(email.getSubject());
        body.setText(email.getBody());

        backIV.setOnClickListener(this);


        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.UK);
                String readMail = "Welcome to read mail activity";
                textToSpeech.speak(readMail, TextToSpeech.QUEUE_ADD, null);
            }
        });

        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            String from = "From";
            textToSpeech.speak(from,TextToSpeech.QUEUE_ADD,null);
            textToSpeech.speak(tofrom.getText().toString(),TextToSpeech.QUEUE_ADD,null);
            String subjects ="subject";
            textToSpeech.speak(subjects,TextToSpeech.QUEUE_ADD,null);
            textToSpeech.speak(subject.getText(), TextToSpeech.QUEUE_ADD, null, toString());
            String message ="Message";
            textToSpeech.speak(message,TextToSpeech.QUEUE_ADD,null);
            textToSpeech.speak(body.getText(), TextToSpeech.QUEUE_ADD, null, toString());
        }, 2000);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_readActivity_back:
                finish();
                break;
        }
    }
}