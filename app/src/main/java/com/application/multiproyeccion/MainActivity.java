package com.application.multiproyeccion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Agregar animaciones
        Animation animacion1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);

        TextView hotssonTextView = findViewById(R.id.hotssonTextView);
        TextView acapulcoTextView2 = findViewById(R.id.acapulcoTextView2);
        ImageView iconImageView = findViewById(R.id.iconImageView);

        hotssonTextView.setAnimation(animacion2);
        acapulcoTextView2.setAnimation(animacion1);
        iconImageView.setAnimation(animacion1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(iconImageView,"iconInageTrans");
                pairs[1]= new Pair<View, String>(hotssonTextView,"textTrans");


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions opcions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                } else {
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}