package com.example.veterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends AppCompatActivity {
    Button btRegistrarCliente, btRegistrarMascota,btBuscarCliente;

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
        btRegistrarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarMascota();
            }
        });
        btBuscarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarCliente();
            }
        })

    }

    private void buscarCliente(){
        Intent intent = new Intent(getApplicationContext(), Mascota_Cliente_listar.class);
        startActivity(intent);
    }
    private void registrarMascota() {
        Intent intent = new Intent(getApplicationContext(), RegistrarMascota.class);
        startActivity(intent);
    }


    private void registrarCliente() {
        Intent intent = new Intent(getApplicationContext(),ClienteRegistrar.class);
        startActivity(intent);
    }

    private void loadui(){
        btRegistrarCliente = findViewById(R.id.btRegistrarCliente);
        btRegistrarMascota = findViewById(R.id.btRegistrarMascota);
        btBuscarCliente = findViewById(R.id.btBuscarCliente);
    }
}