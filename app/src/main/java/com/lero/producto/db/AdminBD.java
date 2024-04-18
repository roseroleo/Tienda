package com.lero.producto.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminBD extends SQLiteOpenHelper {

    //Atributos de la BD
    private static final int BD_VERSION = 1;  //Para el control de versiones de la BD
    private static final String BD_NOMBRE = "tienda.db";  //Nombre de la BD
    public static final String TB_NOMBRE = "productos";  //Nombre de la tabla o esquema

    //Constructor
    public AdminBD(@Nullable Context context) {
        super(context, BD_NOMBRE, null, BD_VERSION);
    }

    //Metodo para crear la tabla
    @Override
    public void onCreate(SQLiteDatabase tienda) {
        //Creamos la base de datos
        tienda.execSQL("CREATE TABLE productos (" +
                "codigo INTEGER PRIMARY KEY," +
                "descripcion TEXT NOT NULL," +
                "precio INT NOT NULL UNIQUE);");
    }

    //Método para modificar la tabla
    @Override
    public void onUpgrade(SQLiteDatabase tienda, int oldVersion, int newVersion) {

        //Eliminamos la tabla existente
        tienda.execSQL("DROP TABLE " + TB_NOMBRE);
        //LLamamos al método para crear nueva tabla
        onCreate(tienda);
    }
}
