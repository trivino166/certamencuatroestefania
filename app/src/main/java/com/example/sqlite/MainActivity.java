package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText ID, Nombre, Cantidad, Precio;
    Spinner Categoria, Ubicacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ID = findViewById(R.id.txtID);
        Nombre = findViewById(R.id.txtNombreProducto);
        Categoria = findViewById(R.id.spinnerCategoria);
        Cantidad = findViewById(R.id.txtCantidad);
        Precio = findViewById(R.id.txtPrecio);
        Ubicacion = findViewById(R.id.spinnerUbicacion);
        String[] CategoriaS = {"Seleccione:", "Motor", "Transmision", "Sistema electrico", "Electronica", "Chasis"};
        ArrayAdapter<String> AdapterCat = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CategoriaS);
        Categoria.setAdapter(AdapterCat);

        String[] UbicacionS = {"Seleccione:", "Estante A1", "Estante A2", "Estante A3", "Estante A4", "Estante A5"};
        ArrayAdapter<String> AdapterUbica = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, UbicacionS);
        Ubicacion.setAdapter(AdapterCat);

    }

    public void RegistrarProducto(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "ControlInventario", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String IDproducto = ID.getText().toString();
        String NombreProducto = Nombre.getText().toString();
        String CategoriaS = Categoria.getSelectedItem().toString();
        String cantidad = Cantidad.getText().toString();
        String precio = Precio.getText().toString();
        String ubicacion = Ubicacion.getSelectedItem().toString();
        if (!IDproducto.isEmpty() && !NombreProducto.isEmpty() && !CategoriaS.equals("Seleccione:") && !cantidad.equals("") && !precio.equals("") && !ubicacion.equals("Seleccione:")) {
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("ID", IDproducto);
            DatosUsuario.put("NombreProducto", NombreProducto);
            DatosUsuario.put("CategoriaProducto", CategoriaS);
            DatosUsuario.put("CantidadProducto", cantidad);
            DatosUsuario.put("PrecioProducto", precio);
            DatosUsuario.put("UbicacionProducto", ubicacion);
            BaseDatos.insert("Inventario_Bodega", null, DatosUsuario);
            BaseDatos.close();
            ID.setText("");
            Nombre.setText("");
            Categoria.setSelection(0);
            Cantidad.setText("");
            Precio.setText("");
            Ubicacion.setSelection(0);
            Toast.makeText(this, "Producto registrado exitosamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Debes completar toda la informacion", Toast.LENGTH_SHORT).show();
        }
    }

    public void BuscarProducto(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "ControlInventario", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String idProducto = ID.getText().toString();
        if (!idProducto.isEmpty()) {
            Cursor fila = BaseDatos.rawQuery("Select NombreProducto, CategoriaProducto, CantidadProducto, PrecioProducto," +
                    " UbicacionProducto from Inventario_Bodega where ID =" + idProducto, null);
            if (fila.moveToFirst()) {
                Nombre.setText(fila.getString(0));
                switch (fila.getString(1)) {
                    case "Motor":
                        Categoria.setSelection(1);
                        break;
                    case "Transmision":
                        Categoria.setSelection(2);
                        break;
                    case "Sistema electrico":
                        Categoria.setSelection(3);
                        break;
                    case "Electronica":
                        Categoria.setSelection(4);
                        break;
                    case "Chasis":
                        Categoria.setSelection(5);
                        break;
                }
                Cantidad.setText(fila.getString(2));
                Precio.setText(fila.getString(3));
                switch (fila.getString(4)) {
                    case "Estante A1":
                        Ubicacion.setSelection(1);
                        break;
                    case "Estante A2":
                        Ubicacion.setSelection(2);
                        break;
                    case "Estante A3":
                        Ubicacion.setSelection(3);
                        break;
                    case "Estante A4":
                        Ubicacion.setSelection(4);
                        break;
                    case "Estante A5":
                        Ubicacion.setSelection(5);
                        break;
                }
                BaseDatos.close();
            } else {
                Toast.makeText(this, "ID ingresado no existe", Toast.LENGTH_LONG).show();
                BaseDatos.close();
            }
        } else {
            Toast.makeText(this, "Campo ID producto no debe estar vacio", Toast.LENGTH_LONG).show();
        }
    }

    public void EliminarProducto(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "ControlInventario", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String IDProducto = ID.getText().toString();
        if (!IDProducto.isEmpty()) {
            int Eliminar = BaseDatos.delete("Inventario_Bodega", "ID=" + IDProducto, null);
            if (Eliminar == 1) {
                Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_LONG).show();
                BaseDatos.close();
                ID.setText("");
                Nombre.setText("");
                Categoria.setSelection(0);
                Cantidad.setText("");
                Precio.setText("");
                Ubicacion.setSelection(0);


            } else {
                Toast.makeText(this, "No se encontro el producto a eliminar", Toast.LENGTH_LONG).show();
                BaseDatos.close();
            }
        } else {
            Toast.makeText(this, "Campo ID no debe estar vacio", Toast.LENGTH_LONG).show();
            BaseDatos.close();
        }

    }

    public void ModificarProducto(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "ControlInventario", null, 1);
        SQLiteDatabase BaseDatos = admin.getWritableDatabase();
        String IDproducto = ID.getText().toString();
        String NombreProducto = Nombre.getText().toString();
        String CategoriaS = Categoria.getSelectedItem().toString();
        Integer cantidad = Integer.parseInt(Cantidad.getText().toString());
        Integer precio = Integer.parseInt(Precio.getText().toString());
        String ubicacion = Ubicacion.getSelectedItem().toString();
        if (!IDproducto.isEmpty() && !NombreProducto.isEmpty() && !CategoriaS.equals("Seleccione:") && !cantidad.equals("") && !precio.equals("") && !ubicacion.equals("Seleccione:")){
            ContentValues DatosUsuario = new ContentValues();
            DatosUsuario.put("ID", IDproducto);
            DatosUsuario.put("NombreProducto", NombreProducto);
            DatosUsuario.put("CategoriaProducto", CategoriaS);
            DatosUsuario.put("CantidadProducto", cantidad);
            DatosUsuario.put("PrecioProducto", precio);
            DatosUsuario.put("UbicacionProducto", ubicacion);
            int Update = BaseDatos.update("Inventario_Bodega", DatosUsuario, "ID="+ IDproducto, null);
            if (Update == 1){
                ID.setText("");
                Nombre.setText("");
                Categoria.setSelection(0);
                Cantidad.setText("");
                Precio.setText("");
                Ubicacion.setSelection(0);
                Toast.makeText(this, "el Producto  fue modificado  con exito", Toast.LENGTH_LONG).show();
                BaseDatos.close();
            } else {
                Toast.makeText(this, "No se encontro el producto", Toast.LENGTH_LONG).show();
                BaseDatos.close();
            }
        } else {
            Toast.makeText(this, "No pueden haber campos vacios", Toast.LENGTH_LONG).show();
            BaseDatos.close();
        }
    }


    public void AbrirListadoProductos(View view) {
        Intent intent = new Intent(this, ProductosRegistrados.class);
        startActivity(intent);
    }
}
