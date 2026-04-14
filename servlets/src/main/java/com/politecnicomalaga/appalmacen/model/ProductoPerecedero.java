package com.politecnicomalaga.appalmacen.model;
public class ProductoPerecedero extends Producto {
    private String ExpDate;
    public ProductoPerecedero(String codigoProducto, String descripcion, double precio, int stock, String ExpDate) {
        super(codigoProducto, descripcion, precio, stock);
        this.ExpDate = ExpDate;
    }
    
    public void setExpDate(String ExpDate){
        this.ExpDate = ExpDate;
    }
    public String getExpDate(){
        return ExpDate;
    }
    @Override
    public String toString() {
        return "ProductoPerecedero," +
                getCodigoProducto() + "," +
                getDescripcion() + "," +
                getPrecio() + "," +
                getStock() + "," +
                ExpDate;
    }
}