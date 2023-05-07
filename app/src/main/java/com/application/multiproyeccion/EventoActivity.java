package com.application.multiproyeccion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.application.multiproyeccion.Adaptador.FirestoreAdapter;
import com.application.multiproyeccion.Modelo.ImageModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EventoActivity extends AppCompatActivity {

    FloatingActionButton goUploadActivity;
    private RecyclerView mRecyclerView;
    private FirestoreAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        goUploadActivity = findViewById(R.id.guardarVideo);
        mRecyclerView=findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(EventoActivity.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(EventoActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("eventos")
                        .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    ArrayList<ImageModel>mDataList=new ArrayList<>();
                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                        ImageModel data = documentSnapshot.toObject(ImageModel.class);
                                        data.setKey(documentSnapshot.getId());
                                        mDataList.add(data);
                                    }
                                    mAdapter = new FirestoreAdapter(mDataList,this);
                                    mRecyclerView.setAdapter(mAdapter);
                                    dialog.dismiss();
                                })
                                        .addOnFailureListener(e -> Log.e("EventoActivity","Error"));


        goUploadActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventoActivity.this, UploadActivity.class));

            }
        });



    }

}