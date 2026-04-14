package com.politecnicomalaga.appalmacen.view;

import com.politecnicomalaga.appalmacen.dataservice.ConexionBD;
import com.politecnicomalaga.appalmacen.model.Producto;
import com.politecnicomalaga.appalmacen.model.ProductoPerecedero;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/listar")
public class ListarProductosServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        List<Producto> productos = new ArrayList<>();
        List<ProductoPerecedero> productosPerecederos = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection()) {
            // Productos normales
            String sql = "SELECT * FROM Productos";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                productos.add(new Producto(
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
                productosPerecederos.add(new ProductoPerecedero(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getDate("fecha_caducidad").toString()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        out.println("<html><body>");
        out.println("<h1>Lista de Productos</h1>");
        out.println("<h2>Productos Normales</h2><ul>");
        for (Producto p : productos) {
            out.println("<li>" + p.getCodigoProducto() + ": " + p.getDescripcion() + " (Stock: " + p.getStock() + ")</li>");
        }
        out.println("</ul>");

        out.println("<h2>Productos Perecederos</h2><ul>");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (ProductoPerecedero pp : productosPerecederos) {
            out.println("<li>" + pp.getCodigoProducto() + ": " + pp.getDescripcion() +
                    " (Caduca: " + sdf.format(pp.getExpDate()) + ")</li>");
        }
        out.println("</ul>");
        out.println("</body></html>");
    }
}