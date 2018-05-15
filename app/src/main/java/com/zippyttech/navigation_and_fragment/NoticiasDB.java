package com.zippyttech.navigation_and_fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.zippyttech.navigation_and_fragment.Models.*;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by zippyttech on 03/04/18.
 */

public class NoticiasDB {

    private Context context;
    private SQLiteDatabase db;
    private NoticiasSQLiteHelper helper;
    private NoticiasSQLiteHelper nsdbh;
    private static final String SHARED_KEY = "shared_key";
    private static SharedPreferences settings;
    private static SharedPreferences.Editor editor;

    public NoticiasDB(Context context){
        this.context=context;
        helper =
                new NoticiasSQLiteHelper(context, "DBNoticias", null, 1);
        nsdbh =helper;
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

    public Cursor selectRows(String tableName, String[] projection, String where, String[] args, String sortOrder){

        db = helper.getReadableDatabase();

        Cursor c = db.query(
                tableName,                                  // The table to query
                projection,                                 // The columns to return
                where,                                      // The columns for the WHERE clause
                args,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );


        return c;

    }


    public static void consulta(Context context) {
      //  settings = context.getSharedPreferences(SHARED_KEY,0);
      //  editor = settings.edit();
     //   String sp= settings.getString("valor","33248");
      NoticiasSQLiteHelper  conn = new NoticiasSQLiteHelper(context, "DBNoticias", null, 1);
        String RES="";
      try{
            SQLiteDatabase db = conn.getReadableDatabase();
           // String [] parametros = {sp};
          String [] parametros = {"33250"};
            String [] campos = {"codigo","titulo"};
            Cursor c = db.query("Noticia",campos,"codigo"+"=? ",parametros,null,null,null);
            c.moveToFirst();
            String sp = c.getString(0);
            c.close();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"El documento no existe",Toast.LENGTH_LONG).show();

        }




    }


    /**
     * Insert NoticiasDB*/
    public void  insertContenido (com.zippyttech.navigation_and_fragment.Models.Noticia noticia){
        db = helper.getReadableDatabase();
      try {

          ContentValues reg = new ContentValues();
          reg.put("codigo",noticia.getCodigo());
          reg.put("titulo",noticia.getTitulo());
          reg.put("contenido",noticia.getContenido());
          reg.put("fecha",noticia.getFecha());
          reg.put("imagen",noticia.getImagen());

       db.insert("Noticia",null,reg);
          //   return;
      }
      catch (Exception e){
          e.printStackTrace();

      }
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
                    settings = context.getSharedPreferences(SHARED_KEY,0);
                    editor = settings.edit();

                    String codigo = c.getString(0);
                    editor.putString("valor", codigo);
                    editor.commit();
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
