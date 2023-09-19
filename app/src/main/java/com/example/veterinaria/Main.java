package com.example.veterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends AppCompatActivity {
    Button btRegistrarCliente, btRegistrarMascota,btBuscarCliente,btDatosMascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        loadui();
        btRegistrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarCliente();
            }
        });

    }

    private void registrarCliente() {
        Intent intent = new Intent(getApplicationContext(),RegistrarCliente.class);
        startActivity(intent);
    }

    private void loadui(){
        btRegistrarCliente = findViewById(R.id.btRegistrarCliente);
        btRegistrarMascota = findViewById(R.id.btRegistrarMascota);
        btBuscarCliente = findViewById(R.id.btBuscarCliente);
        btDatosMascota = findViewById(R.id.btDatosMascota);
    }
}