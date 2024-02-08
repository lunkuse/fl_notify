package com.example.lastnotification.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lastnotification.R;

public class NotificationViewHolder extends RecyclerView.ViewHolder {

    public TextView contractNameTextView;
    public TextView logDetailsTextView;
    public TextView avatarTextView;


    public TextView logDateTextView;


    public NotificationViewHolder(View itemView) {
        super(itemView);
        contractNameTextView = itemView.findViewById(R.id.contract_name_text_view);
        logDetailsTextView = itemView.findViewById(R.id.log_details_text_view);
        logDateTextView = itemView.findViewById(R.id.log_date);

        avatarTextView = itemView.findViewById(R.id.avatar_text_view);


    }



}
