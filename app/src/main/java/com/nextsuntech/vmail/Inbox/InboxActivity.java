package com.nextsuntech.vmail.Inbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nextsuntech.vmail.CustomAdaptor;
import com.nextsuntech.vmail.Dashboard.DashboardActivity;
import com.nextsuntech.vmail.Email;
import com.nextsuntech.vmail.R;
import com.nextsuntech.vmail.Sent.Adapter.SentAdapterClass;

import java.util.ArrayList;
import java.util.List;

public class InboxActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    SentAdapterClass sentAdapterClass;
    List<Email> list;
    DatabaseReference ref;
    int count=0;
    String user_email;
    TextView screentitle;
    ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        screentitle = findViewById(R.id.screentitle);
        backIV = findViewById(R.id.iv_inbox_back);

        list = new ArrayList<Email>();
        getRecEmails();
        user_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        backIV.setOnClickListener(this);

        //recyclerView = findViewById(R.id.inboxrecview);
        //CustomAdaptor customAdaptor = new CustomAdaptor(getApplicationContext(),list);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getRecEmails() {
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("emails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()) {
                    Email email = s.getValue(Email.class);
                    if(email.getRecid().equals(user_email))
                        list.add(email);

                }

                recyclerView = findViewById(R.id.inboxrecview);
                CustomAdaptor customAdaptor = new CustomAdaptor(getApplicationContext(),list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(customAdaptor);
                Toast.makeText(getApplicationContext(),list.size()+"",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_inbox_back:
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
                break;
        }
    }
}