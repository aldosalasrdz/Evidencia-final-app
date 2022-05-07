package com.example.evidencia_final_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ViewInventoryActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    RecyclerView myRecyclerview;
    DatabaseReference databaseReference;
    private TextView totalNoOfItems, totalNoOfSum;
    private int countTotalNoOfItems = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        totalNoOfItems= findViewById(R.id.total_no_item);
        totalNoOfSum = findViewById(R.id.totalsum);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        String finalUser = users.getEmail();
        String resultEmail = finalUser.replace(".","");
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(resultEmail)
                .child("Items");
        myRecyclerview = findViewById(R.id.recyclerViews);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        myRecyclerview.setLayoutManager(manager);
        myRecyclerview.setHasFixedSize(true);
        myRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    countTotalNoOfItems = (int) dataSnapshot.getChildrenCount();
                    totalNoOfItems.setText(Integer.toString(countTotalNoOfItems));
                }else{
                    totalNoOfItems.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int sum=0;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Map<String,Object> map = (Map<String, Object>) ds.getValue();
                    Object price = map.get("itemPrice");
                    int pValue = Integer.parseInt(String.valueOf(price));
                    sum += pValue;
                    totalNoOfSum.setText(String.valueOf(sum));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected  void  onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Items, ScanItemsActivity.UsersViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Items, ScanItemsActivity.UsersViewHolder>
                (  Items.class,
                        R.layout.list_layout,
                        ScanItemsActivity.UsersViewHolder.class,
                        databaseReference )
        {
            @Override
            protected void populateViewHolder(ScanItemsActivity.UsersViewHolder viewHolder,
                                              Items model, int position){

                viewHolder.setDetails(getApplicationContext(),
                        model.getItemBarcode(),
                        model.getItemCategory(),
                        model.getItemName(),
                        model.getItemPrice());
            }
        };

        myRecyclerview.setAdapter(firebaseRecyclerAdapter);
    }
}