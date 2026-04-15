package com.politecnicomalaga.appalmacen;

import android.os.Bundle;
import android.service.controls.Control;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.politecnicomalaga.appalmacen.controller.Controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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

        Controlador.getSingleton(this).listarTodos();
    }


    //Acciones

    //El botón de añadir producto
    public void addProduct(View v) {
        //Revisar los edittext
        String descripcion = ((EditText)findViewById(R.id.etDescProducto)).getText().toString();
        String code = ((EditText)findViewById(R.id.etCodProducto)).getText().toString();
        String stock = ((EditText)findViewById(R.id.etStockProducto)).getText().toString();
        String precio = ((EditText)findViewById(R.id.etPrecio)).getText().toString();

        //comprobar formato

        Map<String,String> datos = new HashMap<>();
        datos.put("descripcion",descripcion);
        datos.put("code",code);
        datos.put("precio",precio);
        datos.put("stock",stock);


        //añadir el producto al modelo
        Controlador.getSingleton(this).addProduct(datos);
        this.reaccionar("");


    }

    public void reaccionar(String error) {

        if (error.isEmpty()) {
            List<Map<String, String>> datos = Controlador.getSingleton(this).getData();

            //Mostrarlos
            ListView miListaEnPantalla = findViewById(R.id.listaProductos);

            ArrayAdapter<String> miAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            for (Map<String, String> unProducto : datos) {
                String resultado = unProducto.get("d") + " - " + unProducto.get("c") + " - " + unProducto.get("p") + " - " + unProducto.get("s");
                miAdapter.add(resultado);
            }

            miListaEnPantalla.setAdapter(miAdapter);
        } else {
            ListView miListaEnPantalla = findViewById(R.id.listaProductos);

            ArrayAdapter<String> miAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            miAdapter.add(error);
            miListaEnPantalla.setAdapter(miAdapter);
        }
    }
}