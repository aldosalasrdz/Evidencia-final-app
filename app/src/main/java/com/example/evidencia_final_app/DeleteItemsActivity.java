package com.example.evidencia_final_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteItemsActivity extends AppCompatActivity {

    public static TextView resultDeleteView;
    private FirebaseAuth firebaseAuth;
    Button scanToDelete, deleteButton;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_items);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        resultDeleteView = findViewById(R.id.delete_product_code);
        scanToDelete = findViewById(R.id.scan_delete_button);
        deleteButton= findViewById(R.id.delete_button);

        scanToDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivityDel.class));
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromDatabase();
            }
        });
    }

    public void deleteFromDatabase()
    {
        String deleteBarcodeValue = resultDeleteView.getText().toString();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finalUser=users.getEmail();
        String resultEmail = finalUser.replace(".","");

        if(!TextUtils.isEmpty(deleteBarcodeValue)){
            databaseReference
                    .child(resultEmail)
                    .child("Items")
                    .child(deleteBarcodeValue)
                    .removeValue();
            Toast.makeText(DeleteItemsActivity.this,
                    "El producto se ha eliminado",
                    Toast.LENGTH_SHORT).show();
        } else{
            Toast.makeText(DeleteItemsActivity.this,
                    "Escanee el código de barras",
                    Toast.LENGTH_SHORT).show();
        }
    }
}