package com.example.qrcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    private EditText editTextInput;
    private ImageView imageViewOutput;
    private Button buttonGenerate, buttonScan;
    private TextView textViewQRResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setPrompt("QÁR Code Skanelesh Szelim");
                intentIntegrator.setBeepEnabled(false);
                intentIntegrator.initiateScan();
            }
        });

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String seged = textViewQRResult.getText().toString();
                if (seged.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Üres a mező", Toast.LENGTH_SHORT).show();
                } else {
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(seged, BarcodeFormat.QR_CODE, 500, 500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        imageViewOutput.setImageBitmap(bitmap);
                    } catch (WriterException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String seged = textViewQRResult.getText().toString();
            if (result.getContents() == null) {
                Toast.makeText(this, "Kiléptél a scannelésből", Toast.LENGTH_SHORT).show();
            } else {
                textViewQRResult.setText(result.getContents());
                try {
                    Uri uri = Uri.parse(seged);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    startActivity(intent);
                } catch (Exception exception) {
                    Log.d("URI ERROR", exception.toString());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void init() {
        editTextInput = findViewById(R.id.editTextInput);
        imageViewOutput = findViewById(R.id.imageViewoutput);
        buttonGenerate = findViewById(R.id.buttonQRGenerate);
        buttonScan = findViewById(R.id.buttonQRScan);
        textViewQRResult = findViewById(R.id.textViewQRResult);
    }
}