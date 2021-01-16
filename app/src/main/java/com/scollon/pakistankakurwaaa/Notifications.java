package com.scollon.pakistankakurwaaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Notifications extends AppCompatActivity {
    RecyclerView notifications_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notifications_list = findViewById(R.id.notifications_list);
        notifications_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
    public static class NotificationViewHolders extends RecyclerView.ViewHolder {

        TextView userNameTxt;
        Button acceptBtn, cancelBtn;
        ImageView profileImageView;
        RelativeLayout cardView;

        public NotificationViewHolders(@NonNull View v) {

            super(v);

            userNameTxt = v.findViewById(R.id.name_notification);
            acceptBtn = v.findViewById(R.id.request_accept_btn);
            cancelBtn = v.findViewById(R.id.request_decline_btn);
            profileImageView = v.findViewById(R.id.image_notification);
            cardView = v.findViewById(R.id.card_view);
        }
    }

}