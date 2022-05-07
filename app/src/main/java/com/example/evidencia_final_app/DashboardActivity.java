package com.example.evidencia_final_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        CardView addItems = (CardView) findViewById(R.id.add_items);
        CardView deleteItems = (CardView) findViewById(R.id.delete_items);
        CardView scanItems = (CardView) findViewById(R.id.scan_items);
        CardView viewInventory = (CardView) findViewById(R.id.view_inventory);

        addItems.setOnClickListener(this);
        deleteItems.setOnClickListener(this);
        scanItems.setOnClickListener(this);
        viewInventory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()){
            case R.id.add_items: {
                i = new Intent(this,AddItemActivity.class);
                startActivity(i);
                break;}
            case R.id.delete_items: {
                i = new Intent(this,DeleteItemsActivity.class);
                startActivity(i);
                break;
            }
            case R.id.scan_items: {
                i = new Intent(this,ScanItemsActivity.class);
                startActivity(i);
                break;
            }
            case R.id.view_inventory: {
                i = new Intent(this,ViewInventoryActivity.class);
                startActivity(i);
                break;
            }
            default: break;
        }
    }
}