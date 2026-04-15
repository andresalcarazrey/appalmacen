package com.politecnicomalaga.appalmacen.model;

//Clase producto

public class Producto {
    private String codigo;
    private String descripcion;
    private double precio;
    private int stock;

    // Constructor
    public Producto(String codigo, String descripcion, double precio, int stock) {
        setCodigoProducto(codigo); // Usamos el setter para validar
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigoProducto(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio>=0.0) this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void changeStock(int newStock) {
        if (this.stock + newStock < 0) return; //nunca tenemos stock negativo
        this.stock += newStock;  //si newStock es negativo, se quita al almacén unidades del producto
    }

    // Método para mostrar la información del producto. CSV Plus
    public String toString() {
        return "Producto," +
                codigo + "," +
                descripcion + "," +
                precio + "," +
                stock;
    }
    
    public static Producto cargarDesdeCSVPlus(String data) {
        String codigo = "";
        String descripcion = "";
        double precio = 0;
        int stock = 0;
        String expDate = null;
        
        //Poner aquí tu código
        String[] datos = data.split(";");

        
        for (int i = 0; i < datos.length; i++) {
            String[] subdatos = datos[i].split("=");
            if (subdatos.length < 2) continue;
            String key = subdatos[0];
            String value = subdatos[1];
        
            switch (key) {
                case "codigoProducto":
                    codigo = value;
                    break;
                case "descripcion":
                    descripcion = value;
                    break;
                case "precio":
                    precio = Double.parseDouble(value);
                    break;
                case "stock":
                    stock = Integer.parseInt(value);
                    break;
                case "ExpDate":
                    expDate = value;
                    break;
                }
            }  

        if (expDate != null) {
            return new ProductoPerecedero(codigo, descripcion, precio, stock, expDate);
        } else {
            return new Producto(codigo, descripcion, precio, stock);
        }
    }
}
