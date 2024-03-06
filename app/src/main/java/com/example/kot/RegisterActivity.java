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

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    Button registerBtn;
    TextView loginTV;
    EditText usernameET, fullET, emailET, phoneET, passET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginTV = findViewById(R.id.loginTV);
        usernameET = findViewById(R.id.usernameET);
        fullET = findViewById(R.id.fullnameET);
        emailET = findViewById(R.id.emailET);
        phoneET = findViewById(R.id.phoneET);
        passET = findViewById(R.id.passET);
        registerBtn = findViewById(R.id.btnRegister);

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameET.getText().toString().trim();
                final String fullname = fullET.getText().toString().trim();
                final String email = emailET.getText().toString().trim();
                final String phone = phoneET.getText().toString().trim();
                final String password = passET.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server_URLs.send, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                }){
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("username",username);
                        params.put("fullname",fullname);
                        params.put("email",email);
                        params.put("phone",phone);
                        params.put("password",password);
                        return params;
                    }
                };
                //Add to request queue
                MySingleton.getInstance(RegisterActivity.this).addTorequestque(stringRequest);
                //go to the login page
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            }
        });
    }
}
