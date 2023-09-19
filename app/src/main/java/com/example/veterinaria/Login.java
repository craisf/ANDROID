package com.example.veterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadui();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceder();
            }
        });
    }
    private void acceder(){
        Intent intent = new Intent(getApplicationContext(), Main.class);
        startActivity(intent);
    }

    private void loadui(){
        btnLogin = findViewById(R.id.btLogin);
    }
}