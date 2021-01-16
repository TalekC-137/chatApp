package com.scollon.pakistankakurwaaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FindPeople extends AppCompatActivity {
    RecyclerView findFriendList;
    EditText searchET;
    private String str= "";
private DatabaseReference usersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);
usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        searchET = findViewById(R.id.search_user);
        findFriendList = findViewById(R.id.find_friends_list);
        findFriendList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(searchET.getText().toString().equals("")){
                }else{
                    str = s.toString();
                            onStart();

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<contacts> options = null;

        if(str.equals("")){
            options = new FirebaseRecyclerOptions.Builder<contacts>().
                    setQuery(usersRef, contacts.class).
                    build();

        }else{
            options = new FirebaseRecyclerOptions.Builder<contacts>().
                    setQuery(usersRef.orderByChild("name").
                                    startAt(str).endAt(str + "\uf8ff"),
                            contacts.class).build();

        }

        FirebaseRecyclerAdapter<contacts, FindFriendsViewHolders> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<contacts, FindFriendsViewHolders>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendsViewHolders findFriendsViewHolders, final int position, @NonNull final contacts contacts) {

                        findFriendsViewHolders.userNameTxt.setText(contacts.getName());
                        Picasso.get().load(contacts.getImage()).into(findFriendsViewHolders.profileImageView);

                        findFriendsViewHolders.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String visit_user_id = getRef(position).getKey();

                                Intent i = new Intent(FindPeople.this, Profile.class);
                                i.putExtra("visit_user_id", visit_user_id);
                                i.putExtra("profile_image", contacts.getImage());
                                i.putExtra("profile_name", contacts.getName());
                                startActivity(i);

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public FindFriendsViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_design, parent, false);
                        FindFriendsViewHolders viewHolders  = new FindFriendsViewHolders(view);
                        return viewHolders;
                    }
                };
        findFriendList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();



    }





    public static class FindFriendsViewHolders extends RecyclerView.ViewHolder {

        TextView userNameTxt;
        Button callBtn;
        ImageView profileImageView;
        RelativeLayout cardView;

        public FindFriendsViewHolders(@NonNull View v) {

            super(v);

            userNameTxt = v.findViewById(R.id.name_contact);
            callBtn= v.findViewById(R.id.call_btn);
            profileImageView = v.findViewById(R.id.image_contact);
            cardView = v.findViewById(R.id.card_view2);

            callBtn.setVisibility(View.GONE);
        }
    }
}