package com.politecnicomalaga.appalmacen.dataservice;

import android.os.Handler;
import android.os.Looper;

import com.politecnicomalaga.appalmacen.controller.Controlador;
import com.politecnicomalaga.appalmacen.model.Producto;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BBDDAccess {
    //Ahora ya no conectamos por JDBC:MYSQL a la BBDD directamente
    //Ahora hablamos con el servidor Tomcat
    //Para eso usamos HTTP, y la libreria Okhttp nos viene de perlas
    //Peroooooo.... La petición debe ser asíncrona, en otro hilo, para
    //No bloquear la pantalla del móvil. Cuando la respuesta
    //esté disponible, la mandamos al hilo de la GUI
    private final Controlador c;
    //private static final String IP = "192.168.1.138:8080";
    private static final String IP = "10.0.2.2:8080";

    public BBDDAccess(Controlador c) {
        this.c = c;
    }

    public void insertarProducto(String code, String desc, double precio, int stock) {
        //Cliente HTTP
        String URL = "http://" + IP + "/insertar"; //algún día, mi Tomcat estará en un VPS, y será libre :)
        // ... Mientras, a poner la IP de la LAN

        //Añadimos parámetros al GET
        URL += "?codigo=" + code + "&descripcion=" + desc + "&precio=" + precio + "&stock=" + stock;
        OkHttpClient clientHTTP = new OkHttpClient();

        //Petición a realizar al cliente HTTP. Patrón de diseño "Builder". Es decir "poco a poco"
        Request request = new Request.Builder()
                .url(URL)  //dirección web
                .get()     //función http a utilizar, puede ser post, put,...
                .addHeader("accept", "application/json")  //Qué formato queremos
                .build();   //a construir la request!!

        //realizamos la llamada al server, pero en otro thread (con enqueue)
        //Primero, una llamada al server
        Call llamada = clientHTTP.newCall(request);
        //Ponemos la llamada en cola para que salga por la tarjeta de red que tengamos en el móvil activa, y creamos un
        //objeto anónimo CallBack (llamada de vuelta cuando están los datos)
        //Un callback tiene override de onResponse (la petición se ha atendido perfectamente) y
        //override de onFailure (evento de "algo salió mal")
        llamada.enqueue(new Callback() {
            public void onResponse(Call call, Response respuestaServer)
                    throws IOException {
                //Got answer!!!!! cogemos los datos dentro del body del mensaje
                String respuesta = respuestaServer.body().string();

                // Create a handler that associated with Looper of the main thread
                //Un manejador es un "bucle" en esencia que ejecuta uno a uno todos los procesos de la App
                Handler manejador = new Handler(Looper.getMainLooper()); //pedimos el principal de la app
                // Send a task to the MessageQueue of the main thread
                manejador.post(new Runnable() {
                    @Override
                    public void run() {
                        // Code will be executed on the main thread
                        //Este es código que realmente se ejecuta cuando se recibe la respuesta.
                        c.setData(respuesta, false);
                    }
                });
            }

            public void onFailure(Call call, IOException e) {
                //Cuidado, puede que haya alguna vez un fallo en la respuesta. Entonces entra por aquí
                String respuesta = e.getMessage(); //Fijaros que nos pasan la excepción con el problema.
                //Lo típico es "sacar" el string mensaje de la exceptión y mandarla al sistema principal para
                //que se vea que ha pasado
                Handler manejador = new Handler(Looper.getMainLooper());

                // Send a task to the MessageQueue of the main thread
                manejador.post(new Runnable() {
                    @Override
                    public void run() {
                        // Code will be executed on the main thread

                        c.setData(respuesta, true);

                    }
                });
            }
        });
    }

    //Fijaros que ahora listarTodos devuelve... ¡void!. Por que los datos no los puede
    //conseguir "directamente". Lo que hacen estos métodos es pedir los datos, y poner un hilo
    // a funcionar como encargado de "escuchar" la posible respuesta
    public void listarTodos() {
        //Cliente HTTP
        String URL = "http://" + IP + "/listar"; //algún día, mi Tomcat estará en un VPS, y será libre :)
        // ... Mientras, a poner la IP de la LAN

        OkHttpClient clientHTTP = new OkHttpClient();

        //Petición a realizar al cliente HTTP. Patrón de diseño "Builder". Es decir "poco a poco"
        Request request = new Request.Builder()
                .url(URL)  //dirección web
                .get()     //función http a utilizar, puede ser post, put,...
                .addHeader("accept", "application/json")  //Qué formato queremos
                .build();   //a construir la request!!

        //realizamos la llamada al server, pero en otro thread (con enqueue)
        //Primero, una llamada al server
        Call llamada = clientHTTP.newCall(request);
        //Ponemos la llamada en cola para que salga por la tarjeta de red que tengamos en el móvil activa, y creamos un
        //objeto anónimo CallBack (llamada de vuelta cuando están los datos)
        //Un callback tiene override de onResponse (la petición se ha atendido perfectamente) y
        //override de onFailure (evento de "algo salió mal")
        llamada.enqueue(new Callback() {
            public void onResponse(Call call, Response respuestaServer)
                    throws IOException {
                //Got answer!!!!! cogemos los datos dentro del body del mensaje
                String respuesta = respuestaServer.body().string();

                // Create a handler that associated with Looper of the main thread
                //Un manejador es un "bucle" en esencia que ejecuta uno a uno todos los procesos de la App
                Handler manejador = new Handler(Looper.getMainLooper()); //pedimos el principal de la app
                // Send a task to the MessageQueue of the main thread
                manejador.post(new Runnable() {
                    @Override
                    public void run() {
                        // Code will be executed on the main thread
                        //Este es código que realmente se ejecuta cuando se recibe la respuesta.
                        c.setData(respuesta, false);
                    }
                });
            }

            public void onFailure(Call call, IOException e) {
                //Cuidado, puede que haya alguna vez un fallo en la respuesta. Entonces entra por aquí
                String respuesta = e.getMessage(); //Fijaros que nos pasan la excepción con el problema.
                //Lo típico es "sacar" el string mensaje de la exceptión y mandarla al sistema principal para
                //que se vea que ha pasado
                Handler manejador = new Handler(Looper.getMainLooper());

                // Send a task to the MessageQueue of the main thread
                manejador.post(new Runnable() {
                    @Override
                    public void run() {
                        // Code will be executed on the main thread

                        c.setData(respuesta, true);

                    }
                });
            }
        });
    }
}