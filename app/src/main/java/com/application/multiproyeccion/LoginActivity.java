package com.application.multiproyeccion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextView nUser;
    private FirebaseAuth mAuth;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicializamos Firebase
        FirebaseApp.initializeApp(this);
        //Obtenemos la instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //Configuramos Boton y EditText
        nUser = findViewById(R.id.goLogin);
        mEtEmail = findViewById(R.id.usuarioTextField);
        mEtPassword = findViewById(R.id.contrasenaTextField);
        mBtnLogin = findViewById(R.id.inicioSesión);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Iniciar sesión con correo y contraseña
                signInWithEmail();
            }
        });



        nUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void signInWithEmail() {
        String email = mEtEmail.getText().toString();
        String pass = mEtPassword.getText().toString();

        if (email.isEmpty() && pass.isEmpty()) {
            Toast.makeText(this, "¡Campos Vacios!", Toast.LENGTH_SHORT).show();
        } else{

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
    }
    }
}