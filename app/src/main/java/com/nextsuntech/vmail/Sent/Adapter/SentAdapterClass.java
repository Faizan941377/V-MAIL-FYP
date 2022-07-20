package com.nextsuntech.vmail.Sent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nextsuntech.vmail.Model.SendDataModel;
import com.nextsuntech.vmail.R;
import com.nextsuntech.vmail.Sent.ReadSentMailsActivity;

public class SentAdapterClass extends FirebaseRecyclerAdapter<SendDataModel, SentAdapterClass.VH> {


    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SentAdapterClass(@NonNull FirebaseRecyclerOptions<SendDataModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull SentAdapterClass.VH holder, int position, @NonNull SendDataModel model) {
        holder.toEmailTV.setText(model.getSenderid());
        holder.messageTV.setText(model.getBody());
        holder.subjectTV.setText(model.getSubject());
        holder.receiverEmailTV.setText(model.getRecid());
        holder.timeTV.setText(model.getTime());


        holder.readMailLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(context, ReadSentMailsActivity.class);
                t.putExtra("Sender_Email", model.getSenderid());
                t.putExtra("Receiver_Email", model.getRecid());
                t.putExtra("Subject", model.getSubject());
                t.putExtra("Message", model.getBody());
                t.putExtra("Time", model.getTime());
                t.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(t);
            }
        });
    }

    @NonNull
    @Override
    public SentAdapterClass.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sent_data,parent,false);
        return new VH(view);
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView toEmailTV;
        TextView subjectTV;
        TextView messageTV;
        TextView receiverEmailTV;
        TextView timeTV;
        LinearLayout readMailLL;

        public VH(@NonNull View itemView) {
            super(itemView);

            toEmailTV =itemView.findViewById(R.id.tv_rowSent_email);
            subjectTV =itemView.findViewById(R.id.tv_rowSent_subject);
            messageTV =itemView.findViewById(R.id.tv_rowSent_message);
            receiverEmailTV = itemView.findViewById(R.id.tv_rowSent_receiverEmail);
            timeTV = itemView.findViewById(R.id.tv_rowSent_time);
            readMailLL = itemView.findViewById(R.id.ll_rowSent);
        }
    }
}
