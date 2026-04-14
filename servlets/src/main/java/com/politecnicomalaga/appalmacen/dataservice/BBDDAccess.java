package com.politecnicomalaga.appalmacen.dataservice;

import com.politecnicomalaga.appalmacen.model.Producto;
import com.politecnicomalaga.appalmacen.model.ProductoPerecedero;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class BBDDAccess {

    //método para obtener los productos. Se conecta y ejecuta el select
    //No sabe se "vive" en una aplicación Java tradicional, en un ExecuteService de Android,
    //en un servlet... Simplemente "pide" a la BBDD la ejecución de SQL y obtiene los datos
    //para convertirlo en objetos del modelo.
    public List<Producto> listarTodos() throws SQLException,ClassNotFoundException {

        Connection conn = null;
        List<Producto> listaResultado = new ArrayList<>();

        conn = ConexionBD.getConnection();
        // Productos normales
        String sql = "SELECT * FROM Productos";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            listaResultado.add(new Producto(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
            ));
        }

        // Productos perecederos
        sql = "SELECT * FROM ProductosPerecederos";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            listaResultado.add(new ProductoPerecedero(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getDate("fecha_caducidad").toString()
            ));
        }
        if (rs!=null) rs.close();
        if (stmt!=null) stmt.close();
        if (conn != null) conn.close();

        return listaResultado;
    }


    //Esta función es la que implementa realmente el acceso y el insert
    //Le pasa lo mismo que al método previo, donde se lleve, funcionará

    public void insertarProducto(Producto p) throws SQLException,ClassNotFoundException {

        PreparedStatement pstmt = null;
        Connection conn = ConexionBD.getConnection();

        String tabla = "Productos";
        String values = " (codigo, descripcion, precio, stock) VALUES (?, ?, ?, ?)";
        if (p instanceof ProductoPerecedero) {
            tabla = "ProductosPerecederos";
            values = " (codigo, descripcion, precio, stock, fecha) VALUES (?, ?, ?, ?, ?)";
        }
        String sql = "INSERT INTO " + tabla + values;


        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, p.getCodigoProducto());
        pstmt.setString(2, p.getDescripcion());
        pstmt.setDouble(3, p.getPrecio());
        pstmt.setInt(4, p.getStock());

        if (p instanceof ProductoPerecedero) {
            pstmt.setString(5,((ProductoPerecedero)p).getExpDate());
        }

        pstmt.executeUpdate();
    }

    //Igual que listarTodos, pero filtrando por código...

    public List<Producto> buscarPorCodigo(String codigo) throws SQLException,ClassNotFoundException {

        Connection conn = null;
        List<Producto> listaResultado = new ArrayList<>();

        conn = ConexionBD.getConnection();
        // Productos normales
        String sql = "SELECT * FROM Productos WHERE codigo LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,"%"+codigo+"%");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            listaResultado.add(new Producto(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
            ));
        }

        // Productos perecederos

        sql = "SELECT * FROM ProductosPerecederos WHERE codigo LIKE ?";
        stmt = conn.prepareStatement(sql);
        stmt.setString(1,"%"+codigo+"%");
        rs = stmt.executeQuery();
        while (rs.next()) {
            listaResultado.add(new ProductoPerecedero(
                    rs.getString("codigo"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("stock"),
                    rs.getDate("fecha_caducidad").toString()
            ));
        }
        if (rs!=null) rs.close();
        if (stmt!=null) stmt.close();
        if (conn != null) conn.close();

        return listaResultado;
    }
}