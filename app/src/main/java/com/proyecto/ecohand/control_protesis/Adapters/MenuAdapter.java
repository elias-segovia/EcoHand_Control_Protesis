package com.proyecto.ecohand.control_protesis.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.proyecto.ecohand.control_protesis.Models.ItemMenu;
import com.proyecto.ecohand.control_protesis.Models.Secuencia;
import com.proyecto.ecohand.control_protesis.R;

import java.io.File;
import java.util.ArrayList;

import customfonts.MyTextView_SF_Pro_Display_Medium;
import customfonts.TextViewSFProDisplayRegular;

public class MenuAdapter extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<ItemMenu> items;

    @Override
    public int getCount() {
        return items.size();
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

        ItemMenu item = items.get(position);

        MyTextView_SF_Pro_Display_Medium titulo = v.findViewById(R.id.TextoID);
        TextViewSFProDisplayRegular subtitulo = v.findViewById(R.id.SubtituloID);
        ImageView icono = v.findViewById(R.id.IconoID);

        titulo.setText(item.getTitulo());
        subtitulo.setText(item.getSubTitulo());


        File imgFile = new File(item.getIconoRuta());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            icono.setImageBitmap(myBitmap);
        }

        return v;
    }
}
