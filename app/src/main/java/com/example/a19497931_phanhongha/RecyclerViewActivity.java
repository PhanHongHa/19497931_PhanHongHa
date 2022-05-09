package com.example.a19497931_phanhongha;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductAdapter ProductAdapter;
    ArrayList<Product> Products;
    private FirebaseFirestore db;
    ProgressDialog dialog;
    Button btnAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        recyclerView= findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        Products = new ArrayList<>();
        ProductAdapter = new ProductAdapter(RecyclerViewActivity.this, Products);
        recyclerView.setAdapter(ProductAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new TouchHelper(ProductAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        ReadData();

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((view) -> {
            Intent intent = new Intent(this, CreateActivity.class);
            startActivity(intent);
        });
    }

    public void ReadData() {

        db.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Products.clear();
                for (DocumentSnapshot snapshot : task.getResult()){
                    Product Product = new Product(snapshot.getString("id"), snapshot.getString("ten"), snapshot.getString("gia"), snapshot.getString("trang thai"));
                    Products.add(Product);
                }
                ProductAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RecyclerViewActivity.this, "Firebase error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}