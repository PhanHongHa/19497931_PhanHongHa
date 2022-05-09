package com.example.a19497931_phanhongha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class CreateActivity extends AppCompatActivity {

    private EditText txtTenC , txtGiaC,txtTrangThaiC;
    private Button btnSave;
    private FirebaseFirestore db;
    private String TenU, GiaU, TrangThaiU, IdU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        txtTenC = findViewById(R.id.txtTen_C);
        txtGiaC = findViewById(R.id.txtGia_C);
        txtTrangThaiC = findViewById(R.id.txtTrangThai_C);
        btnSave = findViewById(R.id.btnSave);

        db= FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            btnSave.setText("Update");
            TenU = bundle.getString("TenU");
            GiaU = bundle.getString("GiaU");
            TrangThaiU = bundle.getString("TrangThaiU");
            IdU = bundle.getString("IdU");
            txtTenC.setText(TenU);
            txtGiaC.setText(GiaU);
            txtTrangThaiC.setText(TrangThaiU);

        }else {
            btnSave.setText("Save");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ten = txtTenC.getText().toString();
                String gia = txtGiaC.getText().toString();
                String trangThai = txtTrangThaiC.getText().toString();

                Bundle  bundle1 = getIntent().getExtras();
                if (bundle1 != null){
                    String id = IdU;
                    UpdateDate(id, ten, gia, trangThai);
                }else {

                    String id = UUID.randomUUID().toString();
                    SaveData(id,ten,gia,trangThai);
                }

            }
        });
    }

    private void UpdateDate(String id, String ten, String gia, String trangThai) {

        db.collection("Products").document(id).update("ten",ten,"gia", gia, "trangThai",trangThai)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(CreateActivity.this, "Update thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateActivity.this, RecyclerViewActivity.class));
                        }else {
                            Toast.makeText(CreateActivity.this, "Error:" +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SaveData(String id, String ten, String gia, String trangThai) {
        if(!ten.isEmpty() && !gia.isEmpty() &&!trangThai.isEmpty()){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",id);
            map.put("ten",ten);
            map.put("gia", gia);
            map.put("trangThai",trangThai);
            db.collection("Products").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(CreateActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateActivity.this, RecyclerViewActivity.class));
                    }
                }
            });
        }else
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
    }
}