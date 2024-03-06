package com.example.kot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateqrActivity extends AppCompatActivity {
    TextView code;
    ImageView qrview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generateqr);

        code = findViewById(R.id.codeView);
        qrview = findViewById(R.id.qrView);

        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        code.setText("@"+user.getUsername());
        generateQR(MapsActivity.code);
    }

    private void generateQR(String message) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(message, BarcodeFormat.QR_CODE,700,700);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrview.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //menu
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
                startActivity(new Intent(GenerateqrActivity.this, ProfileActivity.class));
                return true;
            case R.id.scanM:
                startActivity(new Intent(GenerateqrActivity.this, ScanActivity.class));
                return true;
            case R.id.payM:
                startActivity(new Intent(GenerateqrActivity.this, MapsActivity.class));
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
