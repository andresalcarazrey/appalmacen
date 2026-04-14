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

@WebServlet("/buscar")
public class BuscarProductosServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String codigo = request.getParameter("codigo");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = ConexionBD.getConnection()) {
            // Buscar en Productos
            String sql = "SELECT * FROM Productos WHERE codigo = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Producto p = new Producto(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );
                out.println("<html><body>");
                out.println("<h1>Producto Encontrado</h1>");
                out.println("<p>Código: " + p.getCodigoProducto() + "</p>");
                out.println("<p>Descripción: " + p.getDescripcion() + "</p>");
                out.println("<p>Precio: " + p.getPrecio() + "</p>");
                out.println("<p>Stock: " + p.getStock() + "</p>");
                out.println("</body></html>");
                return;
            }

            // Buscar en ProductosPerecederos
            sql = "SELECT * FROM ProductosPerecederos WHERE codigo = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigo);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                ProductoPerecedero pp = new ProductoPerecedero(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("stock"),
                        rs.getDate("fecha_caducidad").toString()
                );
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                out.println("<html><body>");
                out.println("<h1>Producto Perecedero Encontrado</h1>");
                out.println("<p>Código: " + pp.getCodigoProducto() + "</p>");
                out.println("<p>Descripción: " + pp.getDescripcion() + "</p>");
                out.println("<p>Precio: " + pp.getPrecio() + "</p>");
                out.println("<p>Stock: " + pp.getStock() + "</p>");
                out.println("<p>Caduca: " + sdf.format(pp.getExpDate()) + "</p>");
                out.println("</body></html>");
                return;
            }

            out.println("<html><body><h1>Producto no encontrado</h1></body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<html><body><h1>Error al buscar el producto</h1></body></html>");
        }
    }
}