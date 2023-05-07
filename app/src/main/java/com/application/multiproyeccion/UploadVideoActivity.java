package com.application.multiproyeccion;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.application.multiproyeccion.Modelo.VideoModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadVideoActivity extends AppCompatActivity {

    VideoView uploadVideo;
    EditText fechaInicio, fechaFinal, descripcion;
    private Uri uri;
    Button guardar;
    String videoUrl;
    private ActivityResultLauncher<Intent> activityResultLauncherImage, activityResultLauncherVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        uploadVideo = findViewById(R.id.uploadVideo);
        fechaInicio = findViewById(R.id.edtFechaInicioVideo);
        fechaFinal = findViewById(R.id.edtFechaFinalVideo);
        descripcion = findViewById(R.id.descripcionVideo);
        guardar=findViewById(R.id.saved);

        // ActivityResultLauncher para seleccionar un video desde la galería
        activityResultLauncherVideo = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadVideo.setVideoURI(uri);
                        } else {
                            Toast.makeText(UploadVideoActivity.this, "Error al seleccionar el video",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Seleccionar video desde la galería
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoPicker = new Intent(Intent.ACTION_PICK);
                videoPicker.setType("video/*");
                activityResultLauncherVideo.launch(videoPicker);
            }
        });

        // Guardar datos en Firestore
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos(uri);
            }
        });
    }

    private void guardarDatos(Uri uri) {
        if (uri == null) {
            Toast.makeText(UploadVideoActivity.this, "Por favor, seleccione una video", Toast.LENGTH_SHORT).show();
            return;
        }
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Hottson Videos").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadVideoActivity.this);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete()) ;
                Uri urlVideo = uriTask.getResult();
                videoUrl = urlVideo.toString();
                subirDatos();
                dialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(UploadVideoActivity.this, "Error al subir video", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void subirDatos() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventosRef = db.collection("EVENTOS VIDEOS");

        String fechInicio = fechaInicio.getText().toString();
        String fechFin = fechaFinal.getText().toString();
        String descrip = descripcion.getText().toString();

        if (fechInicio.isEmpty() || fechFin.isEmpty() || descrip.isEmpty()) {
            Toast.makeText(UploadVideoActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        } else {

            VideoModel videoModel = new VideoModel(fechInicio, fechFin, descrip, videoUrl);

            eventosRef.add(videoModel)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(UploadVideoActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
                            fechaInicio.setText("");
                            fechaFinal.setText("");
                            descripcion.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadVideoActivity.this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}