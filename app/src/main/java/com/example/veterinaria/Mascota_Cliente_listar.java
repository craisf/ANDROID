package com.example.veterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Mascota_Cliente_listar extends AppCompatActivity {
    EditText etDNIBuscar,dtDatosdueno;
    Button btBuscardueno;
    ListView lvMascotas;


    private List<Mascota> dataList = new ArrayList<>();
    private List<Integer> dataID = new ArrayList<>();
    private MascotaAdapter adapter;
    private String  dni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mascota_cliente_listar);
        loadui();

        btBuscardueno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarCliente();
            }
        });
        lvMascotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle extras = new Bundle();
                extras.putInt("idmascota", dataID.get(i));

                Intent intent = new Intent(getApplicationContext(),Mascota_Detalle.class);
                startActivity(intent);
            }
        });
    }


    private void obtenerMascota(String dni){
        dataID.clear();
        dataList.clear();
        adapter = new MascotaAdapter(this, dataList);
        lvMascotas.setAdapter(adapter);
        String URL = Utils.URL + "mascota.controller.php?operacion=searchPetOwner&dni=" + dni;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = new JSONObject(response.getString(i));
                            Mascota mascota = new Mascota(jsonObject.getString("nombre"), jsonObject.getString("tipo"));
                            dataList.add(mascota);
                            dataID.add(jsonObject.getInt("idmascota"));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "No se encontraron mascotas", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Problema con servidor", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
    private void buscarCliente(){
        dni = etDNIBuscar.getText().toString().trim();
        String URL = Utils.URL + "cliente.controller.php?operacion=search&dni=" + dni;
        if(dni.isEmpty()|| dni.length()<8){
            Toast.makeText(getApplicationContext(), "Escriba el dni", Toast.LENGTH_SHORT).show();
        }else{
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        if (jsonArray.length() > 0) {
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String nombreCompleto = jsonObject.getString("nombres") + " " + jsonObject.getString("apellidos");
                            dtDatosdueno.setText(nombreCompleto);
                            obtenerMascota(dni);
                        } else {
                            Toast.makeText(getApplicationContext(), "Cliente no encontrado", Toast.LENGTH_SHORT).show();
                            dtDatosdueno.setText(null);
                            dataID.clear();
                            dataList.clear();
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error en el formato de la respuesta JSON", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Problemas con el Servidor", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(this).add(jsonObjectRequest);
        }
    }
    private void loadui(){
        etDNIBuscar = findViewById(R.id.etDNIBuscar);
        dtDatosdueno = findViewById(R.id.dtDatosdueno);
        btBuscardueno = findViewById(R.id.btBuscardueno);
        lvMascotas = findViewById(R.id.lvMascotas);

    }
}