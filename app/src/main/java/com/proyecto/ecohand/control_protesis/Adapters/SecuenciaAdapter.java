package com.proyecto.ecohand.control_protesis.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.proyecto.ecohand.control_protesis.R;

import java.util.ArrayList;

import com.proyecto.ecohand.control_protesis.Models.Secuencia;

public class SecuenciaAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Secuencia> items;

    public SecuenciaAdapter(Activity activity, ArrayList<Secuencia> items) {
        this.activity = activity;
        this.items = items;
    }

    public void addSecuencia(Secuencia sec){
        items.add(sec);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Secuencia> secuencias) {
        for (int i = 0 ; i < secuencias.size(); i++) {
            items.add(secuencias.get(i));
        }
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_secuencia, null);
        }

        Secuencia dir = items.get(position);

        TextView title = v.findViewById(R.id.SecuenciaID);
        title.setText(dir.getNombre());

        return v;
    }
}
