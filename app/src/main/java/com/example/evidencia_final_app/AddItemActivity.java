package com.example.evidencia_final_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddItemActivity extends AppCompatActivity {

    private TextInputEditText itemName,itemCategory,itemPrice;
    private TextView itemBarcode;
    private FirebaseAuth firebaseAuth;
    public static TextView resultTextView;
    Button scanButton, addItemToDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReferenceCat = FirebaseDatabase.getInstance().getReference("Users");

        resultTextView = findViewById(R.id.add_product_number);
        addItemToDatabase = findViewById(R.id.add_product_button);
        scanButton = findViewById(R.id.scan_button);
        itemName = findViewById(R.id.add_product_name_input);
        itemCategory = findViewById(R.id.add_product_category_input);
        itemPrice = findViewById(R.id.add_product_price_input);
        itemBarcode = findViewById(R.id.add_product_number);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
            }
        });

        addItemToDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addItem(); }
        });
    }

    public void addItem() {
        String itemNameValue = itemName.getText().toString();
        String itemCategoryValue = itemCategory.getText().toString();
        String itemPriceValue = itemPrice.getText().toString();
        String itemBarcodeValue = itemBarcode.getText().toString();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finalUser = users.getEmail();
        String resultEmail = finalUser.replace(".","");

        if (itemBarcodeValue.isEmpty()) {
            itemBarcode.setError("Esta vacío");
            itemBarcode.requestFocus();
            return;
        }

        if (!TextUtils.isEmpty(itemNameValue)&&!TextUtils.isEmpty(itemCategoryValue)&&!TextUtils.isEmpty(itemPriceValue)) {
            Items items = new Items(
                    itemNameValue,
                    itemCategoryValue,
                    itemPriceValue,
                    itemBarcodeValue
            );

            databaseReference
                    .child(resultEmail)
                    .child("Items")
                    .child(itemBarcodeValue)
                    .setValue(items);

            databaseReferenceCat
                    .child(resultEmail)
                    .child("ItemByCategory")
                    .child(itemCategoryValue)
                    .child(itemBarcodeValue).setValue(items);

            itemName.setText("");
            itemBarcode.setText("");
            itemPrice.setText("");
            itemCategory.setText("");

            Toast.makeText(AddItemActivity.this,
                    itemNameValue + " añadido",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(AddItemActivity.this,
                    "Rellene todos los campos",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(AddItemActivity.this,LoginActivity.class));
        Toast.makeText(AddItemActivity.this,
                "CIERRE DE SESIÓN EXITOSO",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logoutMenu) {
            Logout();
        }
        return super.onOptionsItemSelected(item);
    }
}