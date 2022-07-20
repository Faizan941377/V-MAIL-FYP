package com.nextsuntech.vmail;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.Myviewholder> {

    List<Email> list;
    Context context;
    public CustomAdaptor(Context context,List<Email> list) {
        this.context = context;
        this.list =list;
    }
    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row,parent,false);
        Myviewholder myviewholder = new Myviewholder(view);
        return myviewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        Email email = list.get(position);
        holder.t1.setText(email.getRecid());
        holder.t2.setText(email.getSubject());
        holder.t3.setText(email.getTime());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(context,ReadActivity.class);
                t.putExtra("Email", email);
                t.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3;
        CardView cardView;
        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.txtsender);
            t2 = itemView.findViewById(R.id.txtsubject);
            t3 = itemView.findViewById(R.id.txttime);
            cardView = itemView.findViewById(R.id.cardview1);
        }
    }
}
