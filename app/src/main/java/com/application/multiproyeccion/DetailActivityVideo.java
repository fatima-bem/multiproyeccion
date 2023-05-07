package com.application.multiproyeccion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivityVideo extends AppCompatActivity {

    TextView detailDesc, fechaI, fechaF;
    VideoView detailVideoV;
    FloatingActionButton delete_video;
    String key="";
    String videoUrl="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_video);

        fechaI = findViewById(R.id.detailFechaInicioVideo);
        fechaF = findViewById(R.id.detailFechaFinalVideo);
        detailVideoV = findViewById(R.id.detailVideo);
        detailDesc = findViewById(R.id.detailDescripcionVideo);
        delete_video=findViewById(R.id.delete_video);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Glide.with(this).load(bundle.getString("dataVideo")).into(detailVideoV);
            //detailVideoV.setVideoPath(bundle.getString("dataVideo"));
            String videoUrl = bundle.getString("dataVideo");
            key=bundle.getString("key");
            // Establece la URL del video como fuente del VideoView
            detailVideoV.setVideoURI(Uri.parse(videoUrl));
            // Comienza a reproducir el video
            detailVideoV.start();
            detailDesc.setText(bundle.getString("descripcion"));
            fechaI.setText(bundle.getString("fechaInicial"));
            fechaF.setText(bundle.getString("fechaFinal"));
        }
        delete_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseStorage storage = FirebaseStorage.getInstance();

                String collectionName = "EVENTOS VIDEOS"; // Nombre de la colección
                String documentId = key; // Clave del documento

                DocumentReference documentRef = db.collection(collectionName).document(documentId);
                documentRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(DetailActivityVideo.this, "Documento Eliminado", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(DetailActivityVideo.this, HomeActivity.class));

                                if (videoUrl != null && !videoUrl.isEmpty()) {
                                    StorageReference storageRef = storage.getReferenceFromUrl(videoUrl);
                                    storageRef.delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(DetailActivityVideo.this, "Delete", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(DetailActivityVideo.this, "Error", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    // Handle the case when imageUrl is null or empty
                                    Toast.makeText(DetailActivityVideo.this, "Invalid image URL", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DetailActivityVideo.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}