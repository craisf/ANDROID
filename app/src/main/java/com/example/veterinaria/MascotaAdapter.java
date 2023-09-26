package com.example.veterinaria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MascotaAdapter extends ArrayAdapter<Mascota> {

    public MascotaAdapter(Context context, List<Mascota> data) {
        super(context, android.R.layout.simple_list_item_1, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);

        TextView nombreTextView = view.findViewById(android.R.id.text1);
        TextView tipoTextView = view.findViewById(android.R.id.text2);

        Mascota mascota = getItem(position);

        if (mascota != null) {
            nombreTextView.setText(mascota.getNombre());
            tipoTextView.setText(mascota.getTipo());
        }

        return view;
    }
}
