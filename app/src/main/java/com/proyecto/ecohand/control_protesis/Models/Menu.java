package com.proyecto.ecohand.control_protesis.Models;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import com.proyecto.ecohand.control_protesis.Activities.BluetoothActivity;
import com.proyecto.ecohand.control_protesis.Adapters.MenuAdapter;
import com.proyecto.ecohand.control_protesis.R;

import java.util.ArrayList;

public class Menu {

    private static ArrayList<ItemMenu> arrayMenu = new ArrayList<>();
    private static MenuAdapter menuAdapter = new MenuAdapter();

    public static void SetMenu(Context context) {

        arrayMenu.clear();

        ItemMenu home = new ItemMenu(context.getResources().getString(R.string.titulo_home)
                , context.getResources().getString(R.string.subtitulo_home)
                , R.drawable.home);

        ItemMenu bluetooth = new ItemMenu(context.getResources().getString(R.string.titulo_bluetooth)
                , context.getResources().getString(R.string.subtitulo_bluetooth)
                , R.drawable.bluetooth);

//        ItemMenu voz = new ItemMenu(context.getResources().getString(R.string.titulo_voz)
//                , context.getResources().getString(R.string.subtitulo_voz)
//                , R.drawable.voice);

        ItemMenu versionEcoHand = new ItemMenu(context.getResources().getString(R.string.titulo_version_echohand)
                , context.getResources().getString(R.string.subtitulo_version_ecohand)
                , R.drawable.informacion);

        ItemMenu cerrarSesion = new ItemMenu(context.getResources().getString(R.string.titulo_cerrar_sesion)
                , context.getResources().getString(R.string.subtitulo_cerrar_sesion)
                , R.drawable.usuario);


        arrayMenu.add(home);
        arrayMenu.add(bluetooth);
//        arrayMenu.add(voz);
        arrayMenu.add(versionEcoHand);
        arrayMenu.add(cerrarSesion);

        menuAdapter.notifyDataSetChanged();
        menuAdapter.setArray(arrayMenu);
    }

    public static void setActivity(Activity activity) {
        menuAdapter.setActivity(activity);
    }

    public static MenuAdapter getAdapter() {
        return menuAdapter;
    }
}
