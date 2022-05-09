package com.example.a19497931_phanhongha;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    Context context;
    ArrayList<Product> Products;
    FirebaseFirestore db;




    public ProductAdapter(Context context, ArrayList<Product> Products) {
        this.context = context;
        this.Products = Products;
    }

    public void updateData(int position){
        Product Product = Products.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("IdU", Product.getId());
        bundle.putString("TenU", Product.getTen());
        bundle.putString("GiaU", Product.getGia());
        bundle.putString("TrangThaiU", Product.getTrangThai());
        Intent intent = new Intent(context, CreateActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void deleteData(int position){
        Product Product = Products.get(position);
        db.collection("Products").document(Product.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            notifyRemoved(position);
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void notifyRemoved(int posotion){
        Products.remove(posotion);
        notifyRemoved(posotion);
        RecyclerViewActivity recyclerViewActivity = new RecyclerViewActivity();
        recyclerViewActivity.ReadData();
    }
    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);




        return new ProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {

        Product Product = Products.get(position);
        holder.ten.setText(Product.ten);
        holder.Gia.setText(Product.gia);
        holder.TrangThai.setText(Product.trangThai);
    }

    @Override
    public int getItemCount() {
        return Products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView ten, Gia, TrangThai;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ten = itemView.findViewById(R.id.txtTen_item);
            Gia = itemView.findViewById(R.id.txtGia_item);
            TrangThai = itemView.findViewById(R.id.txtTrangThai_item);
        }
    }

}