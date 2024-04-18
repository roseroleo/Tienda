package com.lero.producto;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.lero.producto.db.AdminBD;

public class MainActivity extends AppCompatActivity {

    //Definimos atributos de la tabla
    private EditText etCodigo, etDescripcion, etPrecio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Obtenemos los valores de los campos respectivos
        etCodigo = findViewById(R.id.etcodigo);
        etDescripcion = findViewById(R.id.etdescipcion);
        etPrecio = findViewById(R.id.etprecio);
    }

    //Implementamos los metodos CRUD
    public void Alta(View v) {
        //Establecemos conexion con la BD en modo escritura
        try {
            AdminBD admin = new AdminBD(this);
            SQLiteDatabase tienda = admin.getWritableDatabase();

            //Obtenemos los valores de los edit text
            String codigo = etCodigo.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String precio = etPrecio.getText().toString();

            //Verificamos que los datos sean válidos
            if (!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()) {

                //Insertamos una fila vacia en la tabla
                ContentValues nuevoRegistro = new ContentValues();

                //LLenamos con los valores ingresados por el usuario
                nuevoRegistro.put("codigo", codigo);
                nuevoRegistro.put("descripcion", descripcion);
                nuevoRegistro.put("precio", precio);

                //Persistimos en la BD
                tienda.insert("productos", null, nuevoRegistro);
            } else {
                Toast.makeText(this, "Ingrese datos válidos", Toast.LENGTH_SHORT).show();
            }

            //Cerramos la BD
            tienda.close();

        } catch (Exception e) {
            Toast.makeText(this, "ERROR " + e, Toast.LENGTH_SHORT).show();
        }
        //Borramos los valores que contenian los EditText
        etCodigo.setText("");
        etDescripcion.setText("");
        etPrecio.setText("");
        //Indicamos que la accion se llevo a cabo
        Toast.makeText(this, "Los datos se cargaron con éxito", Toast.LENGTH_SHORT).show();
    }

    //Consultar producto
    public void ConsultaPorCodigo(View v) {
        //Conectamos con la BD en modo consulta
        try {
            AdminBD admin = new AdminBD(this);
            SQLiteDatabase tienda = admin.getReadableDatabase();

            //Recuperamos el código a consultar
            String codigo = etCodigo.getText().toString();

            //Verificamos que el usuario haya ingresado un codigo
            if (!codigo.isEmpty() && TextUtils.isDigitsOnly(codigo)) {

                //Hacemos la consulta en la BD y la almacenamos en un objeto tipo Cursor
                Cursor c = tienda.rawQuery("SELECT descripcion, precio FROM productos WHERE codigo = " + codigo, null);

                //Recorremos el objeto Cursor para recuperar la información de la tabla
                if (c.moveToFirst()) {
                    etDescripcion.setText(c.getString(0));
                    etPrecio.setText(c.getString(1));
                    tienda.close();
                } else {
                    Toast.makeText(this, "El articulo no existe en la base de datos", Toast.LENGTH_SHORT).show();
                    tienda.close();
                }
                c.close();
            } else {
                Toast.makeText(this, "Debe introducir el código del producto", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "ERROR " + e, Toast.LENGTH_SHORT).show();
        }
    }

    //Modificar producto
    public void Modificar(View v){

        //Establecemos conexion con la BD en modo escritura
        try {
        AdminBD admin = new AdminBD(this);
        SQLiteDatabase tienda = admin.getWritableDatabase();

            //Recuperamos los datos del producto a modificar
            String codigo = etCodigo.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String precio = etPrecio.getText().toString();

            //Verificamos que los datos ingresados no esten vacios
            if (!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){

                //Agregamos una fila vacia y enseguida los nuevos datos a modificar
                ContentValues nuevoReg = new ContentValues();
                nuevoReg.put("codigo", codigo);
                nuevoReg.put("descripcion", descripcion);
                nuevoReg.put("precio", precio);

                //Persistimos en la BD y la cerramos
                tienda.update("productos", nuevoReg, "codigo="+codigo,null);
                tienda.close();
                } else {
                Toast.makeText(this, "Ingresa los datos completos", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "ERROR " + e, Toast.LENGTH_SHORT).show();
        }

    }

    //Eliminar producto
    public void baja(View v) {

        //Establecemos conexion con la BD en modo escritura
        try {
            AdminBD admin = new AdminBD(this);
            SQLiteDatabase tienda = admin.getWritableDatabase();

            //Recuperamos el codigo del producto a eliminar
            String codigo = etCodigo.getText().toString();

            //Verificamos que el código exista y borramos los datos si es verdadero
            if (!codigo.isEmpty() && TextUtils.isDigitsOnly(codigo)) {
                tienda.delete("productos", "codigo=" + codigo, null);
                tienda.close();
                //Borramos los datos del EditText
                etCodigo.setText("");
                etDescripcion.setText("");
                etPrecio.setText("");
            } else {
                Toast.makeText(this, "ERROR AL TRATAR DE ELIMINAR EL PRODUCTO", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "ERROR " + e, Toast.LENGTH_SHORT).show();
        }
    }
}

