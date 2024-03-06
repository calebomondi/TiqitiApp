package com.example.kot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    TextView username, fullname, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //if user not logged in, start login activity
        //if(!SharedPrefManager.getInstance(this).isLoggedIn()) {
            //finish();
            //startActivity(new Intent(ProfileActivity.this,MainActivity.class));
        //}

        username = findViewById(R.id.TVuser);
        fullname = findViewById(R.id.TVfullname);
        email = findViewById(R.id.TVemail);
        phone = findViewById(R.id.TVphone);

        //get current user
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        //set values to the textviews
        username.setText("Username: " + user.getUsername());
        fullname.setText("Fullname: " + user.getFullname());
        email.setText("Email: " + user.getEmail());
        phone.setText("Phone: " + user.getPhone());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.three_dot_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profileM:
                return true;
            case R.id.scanM:
                startActivity(new Intent(ProfileActivity.this,ScanActivity.class));
                return true;
            case R.id.payM:
                startActivity(new Intent(ProfileActivity.this,MapsActivity.class));
                return true;
            case R.id.logoutM:
                finish();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
