package com.example.veterinaria;

public class Mascota {
    private String nombre;
    private String tipo;

    public Mascota(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }
}
