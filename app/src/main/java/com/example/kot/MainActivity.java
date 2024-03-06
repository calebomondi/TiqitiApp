package com.example.kot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button loginBtn;
    TextView registerTV;
    EditText usernameET, passET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn = findViewById(R.id.btnLogin);
        registerTV = findViewById(R.id.registerTV);
        usernameET = findViewById(R.id.usernameET);
        passET = findViewById(R.id.passET);

        //if user logged in, start profile activity
        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(MainActivity.this,MapsActivity.class));
        }

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString().trim();
                String pass = passET.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server_URLs.retrieve, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(obj.getBoolean("found")) {
                                Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                //get user from object
                                JSONObject userObj = obj.getJSONObject("user");
                                //create new User object
                                User user = new User(
                                        userObj.getString("username"),
                                        userObj.getString("fullname"),
                                        userObj.getString("email"),
                                        userObj.getString("phone")
                                );
                                //store the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                //start profile activity
                                finish();
                                startActivity(new Intent(MainActivity.this,MapsActivity.class));

                            } else {
                                Toast.makeText(MainActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String,String> Params = new HashMap<>();
                        Params.put("username",username);
                        Params.put("password",pass);
                        return Params;
                    }
                };
                //add to request queue
                MySingleton.getInstance(MainActivity.this).addTorequestque(stringRequest);
            }
        });
    }
}
