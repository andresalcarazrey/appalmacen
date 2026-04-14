package com.politecnicomalaga.appalmacen.controller;



import com.google.gson.Gson;
import com.politecnicomalaga.appalmacen.dataservice.BBDDAccess;
import com.politecnicomalaga.appalmacen.model.*;

import java.sql.SQLException;
import java.util.*;


public class Controlador implements DataAccess{


    public Controlador() {

    }


    //Ahora, el controlador sólo se preocupa de ser el intermediario entre
    //la vista (servlet) y el acceso a datos, usando clases sencillas del modelo
    //Para que la vista desde el backend sea independiente de formato, usamos JSON
    @Override
    public String listAllProducts() {
        BBDDAccess bbdd = new BBDDAccess();

        try {
            List<Producto> listaProd = bbdd.listarTodos();

            return (new Gson()).toJson(listaProd);

        } catch (SQLException se) {
            return "List Products: " + se.getMessage();
        } catch (ClassNotFoundException c) {
            return "List Products: " + c.getMessage();
        }

    }

    @Override
    public String findProductsByCode(String code) {
        BBDDAccess bbdd = new BBDDAccess();

        try {
            List<Producto> listaProd = bbdd.buscarPorCodigo(code);

            return (new Gson()).toJson(listaProd);

        } catch (SQLException se) {
            return "Find Products: " + code + " - "  + se.getMessage();
        } catch (ClassNotFoundException c) {
            return "Find Products: " + code + " - "+ c.getMessage();
        }
    }

    @Override
    public String insertProduct(String jsonProducto, boolean perecedero) {
        Gson gson = new Gson();
        Producto producto;
        if (perecedero) {
            producto = gson.fromJson(jsonProducto, ProductoPerecedero.class);
        } else producto = gson.fromJson(jsonProducto, Producto.class);
        BBDDAccess bbdd = new BBDDAccess();

        try {
            bbdd.insertarProducto(producto);

        } catch (SQLException se) {
            String result = "Product: " + producto.getCodigoProducto() +  "Insert: " + jsonProducto + "Expired:" + perecedero + " - " + se.getMessage();;
            return result;
        } catch (ClassNotFoundException c) {
            return "Insert: " + jsonProducto + " - " + c.getMessage();
        }
        return "Inserción realizada OK!";
    }
}