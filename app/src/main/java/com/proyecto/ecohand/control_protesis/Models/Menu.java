package com.proyecto.ecohand.control_protesis.Models;

import android.app.Activity;

import com.proyecto.ecohand.control_protesis.Adapters.MenuAdapter;
import com.proyecto.ecohand.control_protesis.R;

import java.util.ArrayList;

public class Menu {

    private static ArrayList<ItemMenu> arrayMenu = new ArrayList<ItemMenu>();
    private static MenuAdapter menuAdapter = new MenuAdapter();

    public static void SetMenu(){
        ItemMenu bluetooth = new ItemMenu("Conexión Bluetooth","Configurar conexión con la prótesis", R.drawable.bluetooth);
        ItemMenu voz = new ItemMenu("Configuración de Voz","Configurar el reconocimiento de voz",R.drawable.voice);
        ItemMenu versionEcoHand = new ItemMenu("Acerca de EcoHand","Versión 1.0",R.drawable.informacion);
        ItemMenu cerrarSesion = new ItemMenu("Cerrar Sesión","",R.drawable.usuario);

        arrayMenu.add(bluetooth);
        arrayMenu.add(voz);
        arrayMenu.add(versionEcoHand);
        arrayMenu.add(cerrarSesion);

        menuAdapter.notifyDataSetChanged();
        menuAdapter.setArray(arrayMenu);
    }

    public static void setActivity(Activity activity){
        menuAdapter.setActivity(activity);
    }

    public static MenuAdapter getAdapter(){
        return menuAdapter;
    }
}
