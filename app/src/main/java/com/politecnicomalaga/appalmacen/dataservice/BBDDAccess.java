package com.politecnicomalaga.appalmacen.dataservice;

import com.politecnicomalaga.appalmacen.model.Producto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class BBDDAccess {
    // Configuración de la base de datos
    private static final String URL = "jdbc:mysql://192.168.56.39:3306/almacen";
    private static final String USER = "usuario";
    private static final String PASS = "almacen123";
    private final ExecutorService executorService =

            Executors.newSingleThreadExecutor();

    // Interfaz para manejar el resultado en la Activity
// Esto lo sacaremos de esta clase para poder reutilizarlo...
    public interface OnBBDDCallback {
        void onSuccess(List<Producto> data);
        void onError(String error);
    }
    //método para obtener los productos. Se conecta y ejecuta el select
    public void listarTodos(final OnBBDDCallback callback) {

        executorService.execute(() -> {
            List<Producto> listaProductos = new ArrayList<>();
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
                String sql = "SELECT codigo, descripcion, precio, stock de productos";

                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Producto p = new Producto(rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getInt("stock") );

                    listaProductos.add(p);
                }
                if (callback != null)
                    callback.onSuccess(listaProductos);

            } catch (Exception e) {
                if (callback != null) callback.onError(e.getMessage());
            } finally {
                try {
                    if (rs != null) rs.close();
                } catch (SQLException ignored) {
                }
                try {
                    if (pstmt != null)
                        pstmt.close();
                } catch (SQLException ignored) {
                }
                try {
                    if (conn != null) conn.close();
                } catch (SQLException ignored) {
                }
            }
        });
    }
    //Esta función es la que implementa realmente el acceso y el insert
    public void insertarProducto(final String codigo, final String descripcion,
                                 final double precio, final int stock,
                                 final OnBBDDCallback callback) {
//El código a ejecutar, se lo pasamos al sistema con una Lambda
        executorService.execute(() -> {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
// Cargar el driver (necesario en algunas versiones de Android)
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
                String sql = "INSERT INTO productos"

                        + " (codigo, descripcion, precio, stock) VALUES (?, ?, ?, ?)";

                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, codigo);
                pstmt.setString(2, descripcion);
                pstmt.setDouble(3, precio);
                pstmt.setInt(4, stock);

                pstmt.executeUpdate();
// Si todo sale bien, notificamos al hilo principal
                if (callback != null) callback.onSuccess(null);
            } catch (Exception e) {
                if (callback != null) callback.onError(e.getMessage());
            } finally {
                try { if (pstmt != null) pstmt.close(); } catch (SQLException
                        ignored) {}
                try { if (conn != null) conn.close(); } catch (SQLException
                        ignored) {}
            }
        });
    }
}