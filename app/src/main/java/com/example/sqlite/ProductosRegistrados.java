package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ProductosRegistrados extends AppCompatActivity {
    ListView Lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_registrados);
        Lista = findViewById(R.id.ListaProductos);
        CargaProducto();
    }
    public void CargaProducto() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "ControlInventario", null, 1);
        SQLiteDatabase BaseDatos = admin.getReadableDatabase();
        Cursor fila = BaseDatos.rawQuery("SELECT * FROM Inventario_Bodega", null);
        ArrayList<String> ListaProductos = new ArrayList<>();
        if (fila.moveToFirst()) {
            do {
                String ID = fila.getString(0);
                String Producto = fila.getString(1);
                String Categoria = fila.getString(2);
                String Cantidad = fila.getString(3);
                String Precio = fila.getString(4);
                String Ubicacion = fila.getString(5);
                String infoProducto = "ID: " + ID + ", Producto: " + Producto + ", Categoria: " + Categoria + ", Cantidad: "+ Cantidad + ", Precio: "+ Precio +
                        ", Ubicacion: "+ Ubicacion;
                ListaProductos.add(infoProducto);
            } while (fila.moveToNext());
        }
        BaseDatos.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ListaProductos);
        Lista.setAdapter(adapter);
    }

}