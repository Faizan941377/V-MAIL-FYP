package com.nextsuntech.vmail.Compose;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nextsuntech.vmail.Dashboard.DashboardActivity;
import com.nextsuntech.vmail.Email;
import com.nextsuntech.vmail.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;


public class ComposeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView composeTV;
    EditText senderEmailET;
    EditText subjectET;
    EditText messageET;
    TextView speechText;
    ImageView backIV;
    TextToSpeech textToSpeech;
    RelativeLayout sendEmailBT;
    String sender_email;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);


        sender_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Toast.makeText(getApplicationContext(), sender_email, Toast.LENGTH_SHORT).show();
        composeTV = findViewById(R.id.tv_compose_pleaseRecodeMassage);
        senderEmailET = findViewById(R.id.et_compose_senderEmail);
        subjectET = findViewById(R.id.et_compose_subject);
        messageET = findViewById(R.id.et_compose_body);
        sendEmailBT = findViewById(R.id.bt_compose_sendMail);
        speechText = findViewById(R.id.tv_compose_speechText);
        backIV = findViewById(R.id.iv_compose_back);


        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.UK);
                textToSpeech.speak(composeTV.getText().toString(), TextToSpeech.QUEUE_ADD, null);
            }
            senderEmailSpeaking();
        });
    }

    private void senderEmailSpeaking() {
        textToSpeech = new TextToSpeech(getApplicationContext(), i -> {
            if (i != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.UK);
                String email = "tell me the mail address to be we want to send mail";
                textToSpeech.speak(email, TextToSpeech.QUEUE_ADD, null);
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(this::openMic, 5000);

        backIV.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                senderEmailET.setText(result.get(0).replaceAll("\\s+", "") + "@gmail.com");
            }
            subjectET.setText("what should be the subject");
            textToSpeech.speak(subjectET.getText().toString(), TextToSpeech.QUEUE_ADD, null);
            subjectET.setText("");
            Handler handler = new Handler();
            handler.postDelayed(this::OpenMic1, 3000);

        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                subjectET.setText(result1.get(0));
            }
            messageET.setText("Give me message");
            textToSpeech.speak(messageET.getText().toString(), TextToSpeech.QUEUE_ADD, null);
            messageET.setText("");
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                OpenMic2();
            }, 3000);
        }

        if (requestCode == 3) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result1 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                messageET.setText(result1.get(0));
            }
            // here i will read the details which i will provided by voice
            readDetails();
        }
        if (requestCode == 5) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result2 = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                speechText.setText(result2.get(0));
            }
            if (speechText.getText().toString().equals("yes")) {
                sendMail();
            }
            if (speechText.getText().toString().equals("no")) {
                String weClearActivity = "we clear all provided information please record your voice mail again";
                textToSpeech.speak(weClearActivity, TextToSpeech.QUEUE_ADD, null);
                senderEmailET.setText("");
                subjectET.setText("");
                messageET.setText("");
            }

        }

    }

    private void readDetails() {

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            String listenMessage = "Please confirm your provided details";
            textToSpeech.speak(listenMessage, TextToSpeech.QUEUE_ADD, null);
            String senderEmail = "Your sender email address is";
            textToSpeech.speak(senderEmail, TextToSpeech.QUEUE_ADD, null);
            textToSpeech.speak(senderEmailET.getText().toString(), TextToSpeech.QUEUE_ADD, null);
            String subject = "Your subject is";
            textToSpeech.speak(subject, TextToSpeech.QUEUE_ADD, null);
            textToSpeech.speak(subjectET.getText().toString(), TextToSpeech.QUEUE_ADD, null);
            String message = "Your message is";
            textToSpeech.speak(message, TextToSpeech.QUEUE_ADD, null);
            textToSpeech.speak(messageET.getText().toString(), TextToSpeech.QUEUE_ADD, null);
        }, 2000);

        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            confirmationEmail();
        }, 8000);
    }

    private void confirmationEmail() {

        String sendingMailQuestion = "If you want sent mail say yes or don't want to send say no";
        textToSpeech.speak(sendingMailQuestion, TextToSpeech.QUEUE_ADD, null);


        Handler handler1 = new Handler();
        handler1.postDelayed(() -> {
            openMic3();
        }, 12000);

    }

    private void openMic3() {
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

    private void sendMail() {
        /*Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
        // initialize email content
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(senderEmailET.getText().toString().trim()));

            message.setSubject(subjectET.getText().toString().trim());


            message.setText(messageET.getText().toString().trim());

            new SendMail().execute(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }*/
        String rec_emailid = senderEmailET.getText().toString();
        String esubject = subjectET.getText().toString();
        String message = messageET.getText().toString();
        String time_to_email = Calendar.getInstance().getTime().toString();
        Email email = new Email();
        email.setSenderid(sender_email);
        email.setRecid(rec_emailid);
        email.setSubject(esubject);
        email.setBody(message);
        email.setTime(time_to_email);
        ProgressDialog progressDialog = new ProgressDialog(ComposeActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("emails").push().setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Email has been sent", Toast.LENGTH_SHORT).show();
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

    private void OpenMic1() {
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

    private void OpenMic2() {
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_compose_back:
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
        finish();
    }

    private class SendMail extends AsyncTask<Message, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ComposeActivity.this, "Please Wait",
                    "Sending Mail", true, false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                // when success
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                // when error
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // dismiss progressDialog
            progressDialog.dismiss();
            if (s.equals("Success")) {
                //when success
                String mailSent = "Mail Successfully send!";
                textToSpeech.speak(mailSent, TextToSpeech.QUEUE_ADD, null);
                Toast.makeText(ComposeActivity.this, mailSent, Toast.LENGTH_SHORT).show();

            } else {
                String mailNotSend = "email sending failed";
                textToSpeech.speak(mailNotSend, TextToSpeech.QUEUE_ADD, null);
                Toast.makeText(ComposeActivity.this, mailNotSend, Toast.LENGTH_SHORT).show();
            }
        }
    }
}