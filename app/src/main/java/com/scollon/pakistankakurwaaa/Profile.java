package com.scollon.pakistankakurwaaa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class Profile extends AppCompatActivity {

    String receiverUserID ="", receiverUserImage = "", receiverUserName = "";
    private ImageView background_profile_view;
    TextView name_profile;
Button add_friend, decline_friend_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        receiverUserImage = getIntent().getExtras().get("profile_image").toString();
        receiverUserName = getIntent().getExtras().get("profile_name").toString();

        background_profile_view = findViewById(R.id.background_profile_view);
        name_profile = findViewById(R.id.name_profile);
        add_friend = findViewById(R.id.add_friend);
        decline_friend_request = findViewById(R.id.decline_friend_request);


        Picasso.get().load(receiverUserImage).into(background_profile_view);
        name_profile.setText(receiverUserName);


    }
}