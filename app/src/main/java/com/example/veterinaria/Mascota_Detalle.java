package com.example.veterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Mascota_Detalle extends AppCompatActivity {
    TextView tvMascotaNombre,tvAnimal,tvRaza,tvColor,tvGenero ;
    ImageView ivfotoMascota;
    int idMascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascota_detalle);
        loadUI();

        Bundle parametros = this.getIntent().getExtras();
        if (parametros != null) {
            idMascota = parametros.getInt("idmascota");
            Obtenerdata(idMascota);
        }
    }
    private void Obtenerdata(int idmascota){
        String URL = Utils.URL + "mascota.controller.php?operacion=search&idmascota" +  idmascota;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                resetUI();
                try {
                    String photoName = response.getString("fotografia");
                    getImage(photoName);

                    tvMascotaNombre.setText(response.getString("nombre"));
                    tvAnimal.setText(response.getString("animal"));
                    tvRaza.setText(response.getString("raza"));
                    tvColor.setText(response.getString("color"));
                    tvGenero.setText(response.getString("genero").equals("M") ? "Macho" : "Hembra");
                    Log.d("datos", "nombremascota:" + tvMascotaNombre + "tvAnimal" + tvAnimal+ "tvRaza" + tvRaza+ "tvColor" + tvColor +  "tvGenero"+ tvGenero);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Problema en el servidor", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void getImage(String  nameFile){
        String URL = !nameFile.equals("null")? Utils.URLImages + nameFile : nameFile;
        ImageRequest imageRequest = new ImageRequest(URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ivfotoMascota.setImageBitmap(response);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        Volley.newRequestQueue(this).add(imageRequest);

    }
    private void resetUI() {
        tvMascotaNombre.setText("Nombre de la Mascota");
        ivfotoMascota.setImageBitmap(null);
        tvAnimal.setText("Animal");
        tvRaza.setText("Raza");
        tvColor.setText("Color");
        tvGenero.setText("Género");
    }
    private void loadUI(){
        tvMascotaNombre = findViewById(R.id.tvMascotaNombre);
        tvAnimal= findViewById(R.id.tvAnimal);
        tvRaza = findViewById(R.id.tvRaza);
        tvColor = findViewById(R.id.tvColor);
        tvGenero = findViewById(R.id.tvGenero);

        ivfotoMascota = findViewById(R.id.ivfotoMascota);
    }
}