package com.example.kot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScanActivity extends AppCompatActivity {
    int CAMERA_REQUEST_CODE = 1;
    private CodeScanner codeScanner;
    private TextView textView;
    private ListView listView;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        setUpPermissions();
        codeScanner();
    }

    private void codeScanner() {
        //textView = findViewById(R.id.TView);
        listView = findViewById(R.id.TView);
        arrayList = new ArrayList<>();
        //initialize adapter of list view
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ScanActivity.this,android.R.layout.simple_list_item_1,arrayList);
        //set adapter of listview
        listView.setAdapter(arrayAdapter);

        CodeScannerView codeScannerView = findViewById(R.id.scanView);
        codeScanner = new CodeScanner(this,codeScannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(ScanActivity.this, result.getText(), Toast.LENGTH_SHORT).show()
                        String code = result.getText();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server_URLs.confirm, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if(obj.getBoolean("found")) {
                                        Toast.makeText(ScanActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                        //get values
                                        String username = obj.getString("user");
                                        String destination = obj.getString("dest");
                                        //set textview
                                        arrayList.add(username + " " + destination);
                                        arrayAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(ScanActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ScanActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                            protected Map<String,String> getParams() throws AuthFailureError {
                                Map<String,String> Params = new HashMap<>();
                                Params.put("code",code);
                                return Params;
                            }
                        };
                        //add to request queue
                        MySingleton.getInstance(ScanActivity.this).addTorequestque(stringRequest);

                    }
                });
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause(){
        codeScanner.releaseResources();
        super.onPause();
    }

    private void setUpPermissions(){
        int permission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            Toast.makeText(this, "Permission to camera granted.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "This app requires permission to the camera for it to work!", Toast.LENGTH_SHORT).show();
        }
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
                startActivity(new Intent(ScanActivity.this, ProfileActivity.class));
                return true;
            case R.id.scanM:
                return true;
            case R.id.payM:
                startActivity(new Intent(ScanActivity.this, MapsActivity.class));
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
