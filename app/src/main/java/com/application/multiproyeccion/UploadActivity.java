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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.application.multiproyeccion.Modelo.ImageModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadActivity extends AppCompatActivity {

    ImageView uploadImage;
    EditText fechaInicio, fechaFinal, descripcion;
    private Uri uri;
    Button guardar;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        //relacionar las variables a xml
        uploadImage = findViewById(R.id.UploadImage);
        fechaInicio = findViewById(R.id.edtFechaInicioImage);
        fechaFinal = findViewById(R.id.edtFechaFinalImage);
        descripcion= findViewById(R.id.descripcionImage);
        guardar = findViewById(R.id.button);

        //
        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode()== Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri=data.getData();
                            uploadImage.setImageURI(uri);
                        }else{
                            Toast.makeText(UploadActivity.this, "Error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        //Seleccionar imagen desde la galeria
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        //
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarDatos();
            }
        });
    }
    public  void guardarDatos() {

        if (uri == null) {
            Toast.makeText(UploadActivity.this, "Por favor, seleccione una imagen", Toast.LENGTH_SHORT).show();
            return;
        }

        String fechInicio = fechaInicio.getText().toString();
        String fechFin = fechaFinal.getText().toString();
        String descrip = descripcion.getText().toString();

        if (fechInicio.isEmpty() || fechFin.isEmpty() || descrip.isEmpty()) {
            Toast.makeText(UploadActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().
                child("Hottson").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                subirDatos();
                dialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public  void subirDatos(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("eventos");

        String fechInicio = fechaInicio.getText().toString();
        String fechFin = fechaFinal.getText().toString();
        String descrip = descripcion.getText().toString();

        if (fechInicio.isEmpty() || fechFin.isEmpty() || descrip.isEmpty()) {
            Toast.makeText(UploadActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        }else{

            ImageModel imageModel = new ImageModel(fechInicio, fechFin,descrip,imageUrl);
            usersRef.add(imageModel)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(UploadActivity.this, "Guardado", Toast.LENGTH_SHORT).show();
                            fechaInicio.setText("");
                            fechaFinal.setText("");
                            descripcion.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}