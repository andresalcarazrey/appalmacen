package com.politecnicomalaga.appalmacen.dataservice;

import com.politecnicomalaga.appalmacen.model.Producto;

import java.util.List;

public interface DataService {
    public boolean addProducto(Producto p);
    public List<Producto> listAll();
    // y pesha más...
}
