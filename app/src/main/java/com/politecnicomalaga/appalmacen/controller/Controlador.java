package com.politecnicomalaga.appalmacen.controller;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.politecnicomalaga.appalmacen.MainActivity;
import com.politecnicomalaga.appalmacen.dataservice.BBDDAccess;
import com.politecnicomalaga.appalmacen.dataservice.DataAccess_Old;
import com.politecnicomalaga.appalmacen.model.*;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;


public class Controlador {
    // instance variables
    private MainActivity miPantalla;
    private List<Producto> products;
    
    //Singleton poner aquí
    private static Controlador singleton;
    
    private Controlador(MainActivity miPantalla) {
        this.miPantalla = miPantalla;
        products = new ArrayList<>();
    }
    public static Controlador getSingleton(MainActivity miPantalla) {
        // put your code here
        if (singleton == null) singleton = new Controlador(miPantalla);
        return singleton;
    }
    public void addProduct(Map<String,String> datos) {

        boolean resultado = true;

        Producto p = new Producto(datos.get("code"),datos.get("descripcion"),Double.parseDouble(datos.get("precio")),Integer.parseInt(datos.get("stock")));

        resultado = products.add(p);
        if (resultado) {
            BBDDAccess miBBDD = new BBDDAccess(this);
            miBBDD.insertarProducto(datos.get("code"), datos.get("descripcion"), Double.parseDouble(datos.get("precio")), Integer.parseInt(datos.get("stock")));
        }
    }

    public void listarTodos() {
        BBDDAccess miBBDD = new BBDDAccess(this);
        miBBDD.listarTodos();

    }

    public List<Map<String,String>> getData() {

        List<Map<String,String>> resultado = new ArrayList<>();

        //Cambiar del List<Producto> y List<ProductoPerecedero> a
        //List de maps

        for(Producto p: products) {
            Map<String,String> productoMapeado = new HashMap<>();
            productoMapeado.put("c",p.getCodigo());
            productoMapeado.put("d",p.getDescripcion());
            productoMapeado.put("p",""+p.getPrecio());
            productoMapeado.put("s",""+p.getStock());
            resultado.add(productoMapeado);
        }

        return resultado;
    }

    //Este método es llamado por OKhttp cuando se produce la respuesta a la
    // petición de datos a nuestro backend
    public void setData(String jsonData, boolean error) {

        try {
            JsonParser.parseString(jsonData);
            //si estamos aquí, es un json
        } catch (JsonSyntaxException e) {
            error = true; //se que no tengo un json
        }

        if (!error) {
            products.clear();
            Type tipoListaProductos = new TypeToken<List<Producto>>() {
            }.getType();
            products = (new Gson().fromJson(jsonData, tipoListaProductos));
            this.miPantalla.reaccionar("");
        } else {
            this.miPantalla.reaccionar("Error de acceso a Backend " + jsonData);
        }

    }

}