package com.politecnicomalaga.appalmacen.controller;

import com.politecnicomalaga.appalmacen.model.Producto;

public interface DataAccess {

    public String listAllProducts();
    public String findProductsByCode(String code);
    public String insertProduct(String jsonProduct, boolean expired);

}
