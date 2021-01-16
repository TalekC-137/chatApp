package com.scollon.pakistankakurwaaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    BottomNavigationView navView;

    RecyclerView myContactsList;
    ImageView findPeopleBtn;

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findPeopleBtn = findViewById(R.id.find_people);
        myContactsList = findViewById(R.id.contact_list);
        navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        myContactsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        findPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =  new Intent(MainActivity.this, FindPeople.class);
                startActivity(i);
            }
        });

   /* button = findViewById(R.id.button);
    button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("zakończone", " sukces");
            }
        });

    }
});

*/



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch(menuItem.getItemId()){

                        case R.id.navigation_home:
                            Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            break;
                        case R.id.navigation_settings:
                            Intent SettingsIntent = new Intent(MainActivity.this, settings.class);
                            startActivity(SettingsIntent);
                            break;
                        case R.id.navigation_notifications:
                            Intent NotIntent = new Intent(MainActivity.this, Notifications.class);
                            startActivity(NotIntent);
                            break;
                        case R.id.navigation_logout:
                            FirebaseAuth.getInstance().signOut();
                            Intent LogOutIntent = new Intent(MainActivity.this, registration.class);
                            startActivity(LogOutIntent);
                            break;


                    }



                    return false;
                }
            };


}