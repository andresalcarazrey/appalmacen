package com.politecnicomalaga.appalmacen.view;

import com.google.gson.Gson;
import com.politecnicomalaga.appalmacen.controller.Controlador;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class InsertarProductosServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String codigo = request.getParameter("codigo");
        String descripcion = request.getParameter("descripcion");
        String precio = request.getParameter("precio");
        String stock = request.getParameter("stock");
        String fecha = request.getParameter("fechaCad");
        boolean bExpired = (fecha!=null);

        Map<String,String> miJsonMap = new HashMap<>();

        miJsonMap.put("codigo",codigo);
        miJsonMap.put("descripcion",descripcion);
        miJsonMap.put("stock",stock);
        miJsonMap.put("precio",precio);
        if (bExpired) {
            miJsonMap.put("fechaCad",fecha);
        }

        Gson gson = new Gson();
        String jsonProducto = gson.toJson(miJsonMap);

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();


        out.println("{"+(new Controlador()).insertProduct(jsonProducto,bExpired)+"}");
    }
}