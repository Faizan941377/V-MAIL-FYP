package com.nextsuntech.vmail.Sent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;

import com.nextsuntech.vmail.AppDatabase;
import com.nextsuntech.vmail.R;
import com.nextsuntech.vmail.SendMailData;
import com.nextsuntech.vmail.SentMailData;

import java.util.List;

public class SentActivity extends AppCompatActivity {

    RecyclerView sentRV;
    SentAdapterClass sentAdapterClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent);

        getRoomData();
    }

    private void getRoomData() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "v_mailDatabase").allowMainThreadQueries().build();
        SendMailData sendMailData = db.sendMailData();
        sentRV = findViewById(R.id.rv_sent_recyclerView);
        sentRV.setLayoutManager(new LinearLayoutManager(this));

        List<SentMailData> sentData = sendMailData.getAllMails();
        sentAdapterClass = new SentAdapterClass(getApplicationContext(), sentData);
        sentRV.setAdapter(sentAdapterClass);
    }
}