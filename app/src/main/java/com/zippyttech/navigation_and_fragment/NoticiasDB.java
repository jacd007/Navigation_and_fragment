package com.zippyttech.navigation_and_fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zippyttech.navigation_and_fragment.Models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zippyttech on 03/04/18.
 */

public class NoticiasDB {

    private Context context;
    private SQLiteDatabase db;
    private NoticiasSQLiteHelper helper;
    private NoticiasSQLiteHelper nsdbh;

    public NoticiasDB(Context context){
        this.context=context;
        helper =
                new NoticiasSQLiteHelper(context, "DBNoticias", null, 1);
        nsdbh =helper;
    }

    public NoticiasDB(ListFragment listFragment) {

    }

    public void fillDB(){
        /**Base de datos
         * */

        SQLiteDatabase db = nsdbh.getWritableDatabase();

        //Si hemos abierto correctamente la base de datos
        if(db != null)
        {

            //Cerramos la base de datos
            db.close();
        }
    }

    /**
     * Update NoticiasDB */
    public void UpdateNoticiasDB( ){
            try {
                    //Establecemos los campos-valores a actualizar
                    ContentValues valores = new ContentValues();
                    valores.put("titulo", "EL_Update");

                    //Actualizamos el registro en la base de datos
                    db.update("Noticia", valores, "codigo='28507'", null);
            }
            catch (Exception e){
                e.printStackTrace();
            }

        return;
    }
    /**
     * end update*/
    /**
     * Delete NoticiasDB*/
    public void DeleteNoticiasDB (){
        try {

           // db.execSQL("DROP TABLE IF EXISTS Noticia");
            db.execSQL("DELETE FROM Noticia");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return;
    }
    /**
     * end Delete*/

    /**
     * Insert NoticiasDB*/
    public void insertContenido (com.zippyttech.navigation_and_fragment.Models.Noticia noticia){
        db.execSQL("INSERT INTO Noticia (codigo, titulo, contenido, fecha, imagen) "
                + "VALUES (" + noticia.getCodigo() + ", '" + noticia.getTitulo() +"', '" + noticia.getContenido() +"', '" + noticia.getFecha() +"', '" + noticia.getImagen() +"')");
        //   return;
    }

    public void insertarNoticias( List<com.zippyttech.navigation_and_fragment.Models.Noticia> lista){
        try {
            if (lista != null) {

                for (com.zippyttech.navigation_and_fragment.Models.Noticia noti:
                     lista) {
                    insertContenido(noti);
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * end Insert*/

    /**
     * Get-Select NoticiasDB*/

    /**
     * end */
    public ArrayList<com.zippyttech.navigation_and_fragment.Models.Noticia> getList(){

        try {
            db = helper.getWritableDatabase();
            Cursor c = db.rawQuery(" SELECT * FROM Noticia ", null);

            ArrayList<com.zippyttech.navigation_and_fragment.Models.Noticia> listNoticia = new ArrayList<>();

            if (c.moveToFirst()) {

                do {

                    String codigo = c.getString(0);
                    String titulo = c.getString(1);
                    String contenido = c.getString(2);
                    String fecha = c.getString(3);
                    String imagen = c.getString(4);
                    com.zippyttech.navigation_and_fragment.Models.Noticia noticia = new com.zippyttech.navigation_and_fragment.Models.Noticia(0, codigo, titulo, contenido, fecha, imagen);
                    listNoticia.add(noticia);

                } while (c.moveToNext());
                return listNoticia;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
