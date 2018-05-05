package com.zippyttech.navigation_and_fragment.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zippyttech on 02/04/18.
 */

public class NoticiasSQLiteHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE Noticia (codigo TEXT, titulo TEXT, contenido TEXT, fecha INTEGER, imagen TEXT)";


    public NoticiasSQLiteHelper(Context context, String tituloNoticia,
                                SQLiteDatabase.CursorFactory factory, int version) {
        super(context, tituloNoticia, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Noticias");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}
