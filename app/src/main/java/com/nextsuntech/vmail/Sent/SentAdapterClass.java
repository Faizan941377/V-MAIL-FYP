package com.nextsuntech.vmail.Sent;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nextsuntech.vmail.R;
import com.nextsuntech.vmail.SentMailData;

import java.util.List;

public class SentAdapterClass extends RecyclerView.Adapter<SentAdapterClass.VH> {

    Context mContext;
    List<SentMailData> sentMailDataList;

    public SentAdapterClass(Context mContext, List<SentMailData> sentMailDataList) {
        this.mContext = mContext;
        this.sentMailDataList = sentMailDataList;
    }

    @NonNull
    @Override
    public SentAdapterClass.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_sent_data, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SentAdapterClass.VH holder, int position) {
        holder.toEmailTV.setText(sentMailDataList.get(position).getSendEmail());
        holder.subjectTV.setText(sentMailDataList.get(position).getSubject());
        holder.messageTV.setText(sentMailDataList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return sentMailDataList.size();
    }

    public class VH extends RecyclerView.ViewHolder {

        TextView toEmailTV;
        TextView subjectTV;
        TextView messageTV;

        public VH(@NonNull View itemView) {
            super(itemView);

            toEmailTV = itemView.findViewById(R.id.tv_rowSent_email);
            subjectTV = itemView.findViewById(R.id.tv_rowSent_subject);
            messageTV = itemView.findViewById(R.id.tv_rowSent_message);
        }
    }
}
