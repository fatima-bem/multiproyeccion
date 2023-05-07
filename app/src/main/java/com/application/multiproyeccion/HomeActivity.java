package com.application.multiproyeccion;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    ImageButton goSalir;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ImageButton goInfo, UploadImage, UploadVideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        UploadVideo = findViewById(R.id.UploadVideo);
        UploadImage = findViewById(R.id.UploadImage);
        goSalir = findViewById(R.id.goSalir);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        goInfo = findViewById(R.id.goInfo);


        UploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,EventoVideoActivity.class));
            }
        });
        //Pasar a la Actividad de subir imagen
        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,
                        com.application.multiproyeccion.EventoActivity.class);
                startActivity(intent);
            }
        });

        //Cerrar la sesion (Llamar al metodo)
        goSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirAplicación();
            }
        });

        //ir a informacion del administrador
        goInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, Informacion.class);
                startActivity(i);
            }
        });


    }

    //desconectar de firebase
    private void SalirAplicación() {
        firebaseAuth.signOut();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class ));
        Toast.makeText(this, "Cerraste Sesión exitosamente", Toast.LENGTH_SHORT).show();

    }
}
