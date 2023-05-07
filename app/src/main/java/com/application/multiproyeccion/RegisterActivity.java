package com.application.multiproyeccion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private TextView tVlogin;
    private FirebaseAuth mAuth;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Obtener instancia de Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configurar botón de registro
        mEtEmail = findViewById(R.id.usuarioTextFieldR);
        mEtPassword = findViewById(R.id.contrasenaTextFieldR);
        tVlogin = findViewById(R.id.goLogin);
        mBtnRegister = findViewById(R.id.registerButtonR);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccountWithEmail();
            }
        });

        tVlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

        private void createAccountWithEmail() {
            String email = mEtEmail.getText().toString();
            String password = mEtPassword.getText().toString();

            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(this, "¡Campos Vacios!", Toast.LENGTH_SHORT).show();

            } else{

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(RegisterActivity.this, "Se ha enviado un correo electrónico de verificación", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, "Se produjo un error al enviar el correo electrónico de verificación", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                    mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                                        @Override
                                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            if (user != null) {
                                                if (user.isEmailVerified()) {
                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                } else {
                                                    Toast.makeText(RegisterActivity.this, "Por favor, verifique su dirección de correo electrónico", Toast.LENGTH_SHORT).show();
                                                    FirebaseAuth.getInstance().signOut();
                                                }
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(RegisterActivity.this, "Error al registrarse",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        }
    }
