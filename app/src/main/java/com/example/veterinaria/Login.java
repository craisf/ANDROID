package com.example.veterinaria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText etUsuario, etContrasena;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadui();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar();
            }
        });
    }
    private void validar(){
        boolean usuario = etUsuario.getText().toString().trim().isEmpty();
        boolean contrasena = etContrasena.getText().toString().trim().isEmpty();
        if(usuario || contrasena){
            System.out.println("Usuario o contraseña incorrectos");
        }else{
            acceder();
        }
    }

    private void acceder(){
        String URL  = Utils.URL + "cliente.php";
        String usuario = etUsuario.getText().toString().trim();
        String contrasena = etContrasena.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean login = jsonObject.getBoolean("login");
                    String nombre = jsonObject.getString("nombre") + "." + jsonObject.getString("apellidos");
                    if (login) {
                        Toast.makeText(getApplicationContext(), "Bienvenido" + nombre + ".", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Main.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Problemas con el servidor", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("operacion", "login");
                parametros.put("username", usuario);
                parametros.put("password", contrasena);
                return parametros;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void loadui(){
        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnLogin = findViewById(R.id.btLogin);
    }
}