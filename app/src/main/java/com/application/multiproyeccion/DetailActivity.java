package com.application.multiproyeccion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView detailDesc, fechaI,fechaF;
    ImageView detailImage;
    FloatingActionButton btn_delete_img;
    String key="";
    String imageUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        fechaI = findViewById(R.id.detailFechaInicio);
        fechaF = findViewById(R.id.detailFechaFinal);
        detailImage = findViewById(R.id.detailImg);
        detailDesc = findViewById(R.id.detailDescripcion);
        btn_delete_img = findViewById(R.id.btn_delete);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            Glide.with(this).load(bundle.getString("dataImage")).into(detailImage);
            key=bundle.getString("key");
            imageUrl=bundle.getString("dataImage");
            detailDesc.setText(bundle.getString("descripcion"));
            fechaI.setText(bundle.getString("fechaInicial"));
            fechaF.setText(bundle.getString("fechaFinal"));

        }
        btn_delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseStorage storage = FirebaseStorage.getInstance();

                String collectionName = "eventos"; // Nombre de la colección
                String documentId = key; // Clave del documento

                DocumentReference documentRef = db.collection(collectionName).document(documentId);
                documentRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DetailActivity.this, "Documento Eliminado", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DetailActivity.this, HomeActivity.class));
                                StorageReference storageRef = storage.getReferenceFromUrl(imageUrl);
                                storageRef.delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(DetailActivity.this, "Delete", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(DetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }
}