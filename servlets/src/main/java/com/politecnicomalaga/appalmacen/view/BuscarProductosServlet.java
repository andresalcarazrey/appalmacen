package com.politecnicomalaga.appalmacen.view;


import com.politecnicomalaga.appalmacen.controller.Controlador;
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


public class BuscarProductosServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String codigo = request.getParameter("codigo");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        out.println((new Controlador()).findProductsByCode(codigo));
    }
}