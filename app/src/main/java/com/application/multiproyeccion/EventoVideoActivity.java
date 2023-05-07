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
import com.application.multiproyeccion.Adaptador.FirestoreAdapterVideo;
import com.application.multiproyeccion.Modelo.ImageModel;
import com.application.multiproyeccion.Modelo.VideoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class EventoVideoActivity extends AppCompatActivity {

    FloatingActionButton goUploadVideo;
    private RecyclerView vRecyclerView;
    private FirestoreAdapterVideo vAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_video);

        goUploadVideo=findViewById(R.id.floatingUploadVideo);
        vRecyclerView=findViewById(R.id.recyclerViewVideo);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(EventoVideoActivity.this,1);
        vRecyclerView.setLayoutManager(gridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(EventoVideoActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("EVENTOS VIDEOS")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<VideoModel> vDataList=new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        VideoModel data = documentSnapshot.toObject(VideoModel.class);
                        data.setKey(documentSnapshot.getId());
                        vDataList.add(data);
                    }
                    vAdapter = new FirestoreAdapterVideo(vDataList,this);
                    vRecyclerView.setAdapter(vAdapter);
                    dialog.dismiss();
                })
                .addOnFailureListener(e -> Log.e("EventoVideoActivity","Error"));

        goUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventoVideoActivity.this,UploadVideoActivity.class));
            }
        });
    }
}