package com.example.veterinaria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
public class ClienteRegistrar extends AppCompatActivity {
    EditText etApellidos, etNombres, etDNI, etContrasena;
    Button btRegistrarCliente;
    Context context = this;
    String apellidos, nombres, dni, claveacceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_registrar);
        loadUI();
        btRegistrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar();
            }
        });
    }
    private void validar(){
        apellidos  = etApellidos.getText().toString().trim();
        nombres = etNombres.getText().toString().trim();
        dni = etDNI.getText().toString().trim();
        claveacceso = etContrasena.getText().toString().trim();
        Log.d("Valores de solicitud", "Apellidos: " + apellidos + ", Nombres: " + nombres + ", DNI: " + dni + ", Clave: " + claveacceso);

        if (apellidos.isEmpty() || nombres.isEmpty() || dni.isEmpty() || claveacceso.isEmpty()){
            Toast.makeText(context, "Complete estos campos", Toast.LENGTH_SHORT).show();
        }else{
            registrar();
        }
    }
    private void resetInputs() {
        etApellidos.setText(null);
        etNombres.setText(null);
        etDNI.setText(null);
        etContrasena.setText(null);

        etApellidos.requestFocus();
    }

    private void registrar(){
        String URL = Utils.URL + "cliente.controller.php";
        int tiempoDeEsperaEnMilisegundos = 10000; // 10 segundos
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    Toast.makeText(context, "Problemas al registrar", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                    resetInputs();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String mensajeError = "Error desconocido";
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    mensajeError = new String(error.networkResponse.data);
                }
                Toast.makeText(context, "Problemas con el servidor: " + mensajeError, Toast.LENGTH_SHORT).show();
                Log.e("Error de Volley", error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("operacion", "add");
                parametros.put("apellidos", apellidos);
                parametros.put("nombres", nombres);
                parametros.put("dni", dni);
                parametros.put("claveacceso", claveacceso);
                return parametros;
            }
        };
        // Configurar el tiempo de espera
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                tiempoDeEsperaEnMilisegundos,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void loadUI(){
        etApellidos = findViewById(R.id.etApellidos);
        etNombres = findViewById(R.id.etNombres);
        etDNI = findViewById(R.id.etDNI);
        etContrasena = findViewById(R.id.etContrasena);

        btRegistrarCliente = findViewById(R.id.btRegistrarCliente);
    }
}