package com.nextsuntech.vmail.Sent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.nextsuntech.vmail.Dashboard.DashboardActivity;
import com.nextsuntech.vmail.Model.SendDataModel;
import com.nextsuntech.vmail.R;
import com.nextsuntech.vmail.Sent.Adapter.SentAdapterClass;

import java.util.List;

public class SentActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView sentRV;
    SentAdapterClass sentAdapterClass;
    ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent);


        sentRV = findViewById(R.id.rv_sent_recyclerView);
        backIV = findViewById(R.id.iv_sendmail_back);

        backIV.setOnClickListener(this);


        setAdapter();


    }

    private void setAdapter() {
        sentRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<SendDataModel> options =
                new FirebaseRecyclerOptions.Builder<SendDataModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("emails"),SendDataModel.class)
                        .build();


        sentAdapterClass = new SentAdapterClass(options,getApplicationContext());
        sentRV.setAdapter(sentAdapterClass);

    }

    @Override
    protected void onStart() {
        super.onStart();
        sentAdapterClass.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_sendmail_back:
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
}