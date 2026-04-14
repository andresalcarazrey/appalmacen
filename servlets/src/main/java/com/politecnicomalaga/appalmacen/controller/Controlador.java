package com.politecnicomalaga.appalmacen.controller;



import com.politecnicomalaga.appalmacen.dataservice.BBDDAccess;
import com.politecnicomalaga.appalmacen.model.*;
import java.util.*;


public class Controlador {
    // instance variables
    private List<Producto> products;
    private List<Producto> expproducts;
    final private List<Map<String,String>> dataResult = new ArrayList<>();

    
    //Singleton poner aquí
    private static Controlador singleton;
    
    private Controlador() {
        //BBDDAccess miBBDD = new BBDDAccess();

        products = new ArrayList<>();//DataAccess_Old.loadData();
        expproducts = new ArrayList<>();
        //Poner código aquí para que la lista inicial de productos esté
        //siempre disponible cuando se arranca el programa.
    }
    public List<Producto> getProducts(){
        return products;
    }
    public List<Producto> getExpProducts(){
        return expproducts;
    }
    public static Controlador getSingleton() {
        // put your code here
        if (singleton == null) singleton = new Controlador();
        return singleton;
    }
    public void addProduct(Map<String,String> datos) {

        boolean resultado = true;

        Producto p = new Producto(datos.get("code"),datos.get("descripcion"),Double.parseDouble(datos.get("precio")),Integer.parseInt(datos.get("stock")));

        resultado = products.add(p);
        if (resultado) {
            BBDDAccess miBBDD = new BBDDAccess();
            miBBDD.insertarProducto(datos.get("code"), datos.get("descripcion"), Double.parseDouble(datos.get("precio")), Integer.parseInt(datos.get("stock")), new BBDDAccess.OnBBDDCallback() {
                @Override
                public void onSuccess(List<Producto> data) {


                }

                @Override
                public void onError(String error) {

                }
            });
            if (!resultado) {
                products.remove(p);
            }
        }
    }

    public void listarTodos() {

        dataResult.clear();

        BBDDAccess miBBDD = new BBDDAccess();
        miBBDD.listarTodos(new BBDDAccess.OnBBDDCallback() {
                @Override
                public void onSuccess(List<Producto> data) {
                    //Actualizamos el modelo
                    products = data;
                    //Convertir una lista de productos en una lista de maps
                    for(Producto p: data) {
                        Map<String,String> productoMapeado = new HashMap<>();
                        productoMapeado.put("d",p.getDescripcion());
                        productoMapeado.put("p",String.valueOf(p.getPrecio()));
                        productoMapeado.put("s",""+p.getDescripcion());
                        productoMapeado.put("c",p.getCodigoProducto());
                        dataResult.add(productoMapeado);
                        //Avisar AHORA (estoy en el futuro!!!!!)
                        // al controlador y a la activity (Vista)

                    }


                }

                @Override
                public void onError(String error) {

                }
            });


    }

    public List<Map<String,String>> getData() {
        return dataResult;
    }

}