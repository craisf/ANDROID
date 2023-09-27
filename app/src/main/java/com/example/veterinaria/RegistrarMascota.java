package com.example.veterinaria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrarMascota extends AppCompatActivity {
    EditText etDNIBuscar, etNombresDueno, etNombreMascota, etColor;
    Spinner spListaRazas;
    RadioButton rbMacho, rbHembra;
    ImageView ivFotografia;
    Button btCLienteBuscar, btSeleccionarFoto, btRegistrarMascota, btQuitarFoto;
    String idcliente, idraza, nombreMascota, fotografia, color, genero;
    List<String> listaRazas = new ArrayList<>();
    private List<String> idRazas = new ArrayList<>();

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_mascota);
        loadUI();
        obtenerRazas();

        btCLienteBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarcliente();
            }
        });
        spListaRazas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idraza = idRazas.get(position);
                int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                    ((TextView)parent.getChildAt(0)).setTextColor(Color.parseColor("#FFFFFF"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFoto();
            }
        });
        btQuitarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivFotografia.setImageBitmap(null);
            }
        });
        btRegistrarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarData();
            }
        });
    }
    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void seleccionarFoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione una foto"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivFotografia.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void buscarcliente(){
        String dni = etDNIBuscar.getText().toString().trim();
        String URL = Utils.URL + "cliente.controller.php?operacion=search&dni=" + dni;

        if (dni.isEmpty() || dni.length()<8){
            Toast.makeText(getApplicationContext(), "Escriba el DNI", Toast.LENGTH_SHORT).show();
        }else{
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            idcliente = jsonObject.getString("idcliente");
                            String nombreCompleto = jsonObject.getString("nombres") + " " + jsonObject.getString("apellidos");
                            etNombresDueno.setText(nombreCompleto);
                        } else {
                            Toast.makeText(getApplicationContext(), "Cliente no encontrado", Toast.LENGTH_SHORT).show();
                            etNombresDueno.setText(null);
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "Error en el formato de la respuesta JSON", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Problemas al buscar", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(this).add(jsonObjectRequest);
        }
    }

    private void obtenerRazas(){
        String URL = Utils.URL + "mascota.controller.php?operacion=listRace";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for(int i=0;i<response.length();i++){
                        JSONObject jsonObject = response.getJSONObject(i);
                        String idraza = jsonObject.getString("idraza");
                        String razaAnimal = jsonObject.getString("animalraza");
                        idRazas.add(idraza);
                        listaRazas.add(razaAnimal);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, listaRazas);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spListaRazas.setAdapter(adapter);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Problema al obtener razas", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }

    private void registrarMascota(){
        String URL =  Utils.URL + "mascota.controller.php";
        Log.d("dasd", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("dad", response);
                if (!response.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Problema al registrar", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Registrado correctamente", Toast.LENGTH_SHORT).show();
                    resetUI();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Problemas al registrar", Toast.LENGTH_SHORT).show();
                Log.d("sdas", error.getMessage());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("operacion", "add");
                parametros.put("idcliente", idcliente);
                parametros.put("idraza", idraza);
                parametros.put("nombreMascota", nombreMascota);
                parametros.put("fotografia", fotografia);
                parametros.put("color", color);
                parametros.put("genero", genero);
                return parametros;
            }
        };
            Volley.newRequestQueue(this).add(stringRequest);
    }

    private void validarData() {
        nombreMascota = etNombreMascota.getText().toString().trim();
        color = etColor.getText().toString().trim();
        genero = rbMacho.isChecked() ? "M" : "H";
        fotografia = ivFotografia.getDrawable() != null ? getStringImagen(bitmap) : "";
        Log.d("Valores de solicitud", "cliente: " + idcliente + ", raza: " + idraza + ", nombre: " + nombreMascota+  "color: "+ color + "genero: " + genero);

        if (idcliente.isEmpty() || idraza.isEmpty() || nombreMascota.isEmpty() || color.isEmpty() || genero.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Complete los campos", Toast.LENGTH_SHORT).show();
        } else {
            registrarMascota();
        }
    }

    private void resetUI() {
        spListaRazas.setSelection(0);
        etNombreMascota.setText(null);
        etColor.setText(null);
        rbMacho.setChecked(true);
        ivFotografia.setImageBitmap(null);

        etDNIBuscar.requestFocus();
    }

    private void loadUI() {
        etDNIBuscar = findViewById(R.id.etDNIBuscar);
        etNombresDueno = findViewById(R.id.etNombresDueno);
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etColor = findViewById(R.id.etColor);

        spListaRazas = findViewById(R.id.spListaRazas);

        rbMacho = findViewById(R.id.rbMacho);
        rbHembra = findViewById(R.id.rbHembra);

        ivFotografia = findViewById(R.id.ivFotografia);

        btCLienteBuscar = findViewById(R.id.btCLienteBuscar);
        btSeleccionarFoto = findViewById(R.id.btSeleccionarFoto);
        btQuitarFoto = findViewById(R.id.btQuitarFoto);
        btRegistrarMascota = findViewById(R.id.btMascotaRegistrar);
    }
}