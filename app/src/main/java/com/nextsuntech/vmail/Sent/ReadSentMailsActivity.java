package com.nextsuntech.vmail.Sent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextsuntech.vmail.Email;
import com.nextsuntech.vmail.R;

import java.util.Locale;

public class ReadSentMailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView senderEmailTV;
    TextView subjectTV;
    TextView messageTV;
    TextView receiverEmailTV;
    TextView timeTV;
    TextToSpeech textToSpeech;
    ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_sent_mails);

        backIV = findViewById(R.id.iv_readSendActivity_back);
        senderEmailTV = findViewById(R.id.tv_readSent_email);
        subjectTV = findViewById(R.id.tv_readSent_subject);
        messageTV = findViewById(R.id.tv_readSent_message);
        receiverEmailTV = findViewById(R.id.tv_readSent_receiverEmail);
        timeTV = findViewById(R.id.tv_readSent_time);

        String senderEmail = getIntent().getStringExtra("Sender_Email");
        String subject = getIntent().getStringExtra("Subject");
        String message = getIntent().getStringExtra("Message");
        String receiverEmail = getIntent().getStringExtra("Receiver_Email");
        String time = getIntent().getStringExtra("Time");

        senderEmailTV.setText(senderEmail);
        subjectTV.setText(subject);
        messageTV.setText(message);
        receiverEmailTV.setText(receiverEmail);
        timeTV.setText(time);

        backIV.setOnClickListener(this);

        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.UK);
                String readMail = "Welcome to read send read mail activity";
                textToSpeech.speak(readMail, TextToSpeech.QUEUE_ADD, null);
            }
        });

        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            String SenderEmail = "Sender email";
            textToSpeech.speak(SenderEmail,TextToSpeech.QUEUE_ADD,null);
            textToSpeech.speak(senderEmailTV.getText().toString(),TextToSpeech.QUEUE_ADD,null);
            String receiverEmails ="Receiver Email";
            textToSpeech.speak(receiverEmails,TextToSpeech.QUEUE_ADD,null);
            textToSpeech.speak(receiverEmailTV.getText(), TextToSpeech.QUEUE_ADD, null, toString());
            String subjects ="subject";
            textToSpeech.speak(subjects,TextToSpeech.QUEUE_ADD,null);
            textToSpeech.speak(subjectTV.getText(), TextToSpeech.QUEUE_ADD, null, toString());
            String messages = "Message";
            textToSpeech.speak(messages,TextToSpeech.QUEUE_ADD,null);
            textToSpeech.speak(messageTV.getText().toString(),TextToSpeech.QUEUE_ADD,null);
            String times = "time";
            textToSpeech.speak(times,TextToSpeech.QUEUE_ADD,null);
            textToSpeech.speak(timeTV.getText().toString(),TextToSpeech.QUEUE_ADD,null);
        }, 2000);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_readSendActivity_back:
                finish();
                break;
        }
    }
}