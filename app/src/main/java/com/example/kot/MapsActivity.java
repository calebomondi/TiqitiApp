package com.example.kot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    Button juja, thika, cbd, ngong, jkia, kam, bus1, bus2, bus3, bus4, bus5, bus6, bus7;
    
    private final int FINE_PERMISSION_CODE = 1;
    Location currentlocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;

    private MarkerOptions destination, origin;
    private Polyline currentPolyline;

    AlertDialog.Builder builder;

    public static String code,username,depart,dest,amount,used="n",busnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        
        juja = findViewById(R.id.juja);
        thika = findViewById(R.id.thika);
        cbd = findViewById(R.id.cbd);
        ngong = findViewById(R.id.ngong);
        jkia = findViewById(R.id.jkia);
        kam = findViewById(R.id.kamukunji);
        bus1 = findViewById(R.id.bus1);
        bus2 = findViewById(R.id.bus2);
        bus3 = findViewById(R.id.bus3);
        bus4 = findViewById(R.id.bus4);
        bus5 = findViewById(R.id.bus5);
        bus6 = findViewById(R.id.bus6);
        bus7 = findViewById(R.id.bus7);

        Details details = new Details();
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        //DESTINATIONS
        juja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setAmount(60);
                details.setDestination("Juja");
                details.setDeparture("kikuyu");
                details.setSitNumber(45);
                details.setUsername(user.getUsername());
                destination = new MarkerOptions().position(new LatLng(-1.1017998760995338, 37.01429034682189)).title("destination");
                new FetchURL(MapsActivity.this).execute(getUrl(origin.getPosition(), destination.getPosition(), "driving"), "driving");
            }
        });
        thika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setAmount(30);
                details.setDestination("Thika");
                details.setDeparture("cbd");
                details.setSitNumber(16);
                details.setUsername(user.getUsername());
                destination = new MarkerOptions().position(new LatLng(-1.0351257697924967, 37.07683801042632)).title("destination");
                new FetchURL(MapsActivity.this).execute(getUrl(origin.getPosition(), destination.getPosition(), "driving"), "driving");
            }
        });
        cbd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setAmount(80);
                details.setDestination("CBD");
                details.setDeparture("Ngong");
                details.setSitNumber(6);
                details.setUsername(user.getUsername());
                destination = new MarkerOptions().position(new LatLng(-1.2748164691897725, 36.78116743926104)).title("destination");
                new FetchURL(MapsActivity.this).execute(getUrl(origin.getPosition(), destination.getPosition(), "driving"), "driving");
            }
        });
        ngong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setAmount(70);
                details.setDestination("Ngong");
                details.setDeparture("Thika");
                details.setSitNumber(49);
                details.setUsername(user.getUsername());
                destination = new MarkerOptions().position(new LatLng(-1.360344943437977, 36.656647825768225)).title("destination");
                new FetchURL(MapsActivity.this).execute(getUrl(origin.getPosition(), destination.getPosition(), "driving"), "driving");
            }
        });
        jkia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setAmount(100);
                details.setDestination("JKIA");
                details.setDeparture("Juja");
                details.setSitNumber(37);
                details.setUsername(user.getUsername());
                destination = new MarkerOptions().position(new LatLng(-1.3026909117286918, 36.88787037413758)).title("destination");
                new FetchURL(MapsActivity.this).execute(getUrl(origin.getPosition(), destination.getPosition(), "driving"), "driving");
            }
        });
        //BUSES
        bus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setBusNum("SM021");
                AlertDialogBuilder(details.OutputConfirm());
            }
        });
        bus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setBusNum("KP091");
                code = details.generateRandomCode();
                username = details.getUsername();
                depart = details.getDeparture();
                dest = details.getDestination();
                amount = details.getAmount();
                busnum = details.getBusNum();
                AlertDialogBuilder(details.OutputConfirm());
            }
        });
        bus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setBusNum("LF014");
                code = details.generateRandomCode();
                username = details.getUsername();
                depart = details.getDeparture();
                dest = details.getDestination();
                amount = details.getAmount();
                busnum = details.getBusNum();
                AlertDialogBuilder(details.OutputConfirm());
            }
        });
        bus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setBusNum("RT321");
                code = details.generateRandomCode();
                username = details.getUsername();
                depart = details.getDeparture();
                dest = details.getDestination();
                amount = details.getAmount();
                busnum = details.getBusNum();
                AlertDialogBuilder(details.OutputConfirm());
            }
        });
        bus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setBusNum("SM066");
                code = details.generateRandomCode();
                username = details.getUsername();
                depart = details.getDeparture();
                dest = details.getDestination();
                amount = details.getAmount();
                busnum = details.getBusNum();
                AlertDialogBuilder(details.OutputConfirm());
            }
        });
        bus6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setBusNum("ES897");
                code = details.generateRandomCode();
                username = details.getUsername();
                depart = details.getDeparture();
                dest = details.getDestination();
                amount = details.getAmount();
                busnum = details.getBusNum();
                AlertDialogBuilder(details.OutputConfirm());
            }
        });
        bus7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                details.setBusNum("SM001");
                code = details.generateRandomCode();
                username = details.getUsername();
                depart = details.getDeparture();
                dest = details.getDestination();
                amount = details.getAmount();
                busnum = details.getBusNum();
                AlertDialogBuilder(details.OutputConfirm());
            }
        });
    }

    private void AlertDialogBuilder(String message) {
        builder = new AlertDialog.Builder(MapsActivity.this);

        builder.setTitle("CONFIRM TRIP");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Server_URLs.submit, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MapsActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    protected Map<String,String> getParams() throws AuthFailureError {
                        Map<String,String> Params = new HashMap<>();
                        Params.put("busnum",busnum);
                        Params.put("code",code);
                        Params.put("username",username);
                        Params.put("departure",depart);
                        Params.put("destination",dest);
                        Params.put("amount",amount);
                        Params.put("used",used);
                        return Params;
                    }
                };
                //add to request queue
                MySingleton.getInstance(MapsActivity.this).addTorequestque(stringRequest);
                startActivity(new Intent(MapsActivity.this,GenerateqrActivity.class));
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //code
                Toast.makeText(MapsActivity.this, "Trip Cancelled!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void getLastLocation() {
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_PERMISSION_CODE);
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    currentlocation = location;

                    origin = new MarkerOptions().position(new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude())).title("origin");

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        // Create a CameraPosition object that sets the map's height.
        LatLng myLocation = new LatLng(currentlocation.getLatitude(),currentlocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myLocation)
                .zoom(17)
                .tilt(45)
                .build();
        mMap.addMarker(new MarkerOptions().position(myLocation).title("sydney"));
        //mMap.addMarker(destination);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location Permission Denied\nAllow the Permission to use App!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.map_api_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
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
                startActivity(new Intent(MapsActivity.this, ProfileActivity.class));
                return true;
            case R.id.scanM:
                startActivity(new Intent(MapsActivity.this, ScanActivity.class));
                return true;
            case R.id.payM:
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
