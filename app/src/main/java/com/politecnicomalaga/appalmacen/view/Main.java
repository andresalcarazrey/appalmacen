package com.politecnicomalaga.appalmacen.view;
import com.politecnicomalaga.appalmacen.controller.*;
import com.politecnicomalaga.appalmacen.model.*;
import java.util.Scanner;

/**
 * Descripción del examen:
 * 
 * Hay que terminar un prototipo para gestión del stock de productos de un almacén
 * El proyecto tiene una serie de clases incompletas añadidas, y tendrás que crear también 
 * una clase ProductoPerecedero que será un producto que tendrá fecha de caducidad (AAAAMMDD)
 * Tu objetivo: implementar el prototipo hasta hacer funcionales todas las opciones del menú, 
 * teniendo en cuenta que:
 *  - El controlador tendrá una colección List de productos, donde estarán todos los productos
 *  activos
 *  - El controlador tendrá otra colección List de productos con los productos retirados (por 
 *  obsoletos, caducados, o lo que sea)
 *  - Cuando añadimos un producto puede ser normal o perecedero
 *  - Cuando modificamos el stock de un producto, se solicita el código del producto y el stock
 *  - Cuando queremos retirar un producto, pedimos su código
 *  - Cuando queremos mostrar productos entre dos precios pedimos el mínimo y el máximo precio
 *  que usaremos para obtener de la lista de productos activos, los que cumplan con el filtro
 *  - En las demás opciones realizamos lo solicitado sin necesidad de obtener ningún dato por 
 *  teclado
 *  - El controlador es de tipo Singleton
 *  - La vista NO usa objetos ni clases del modelo. El proyecto es MVC con capas puras
 *  - Hay que implementar los listados como tablas, con una cabecera y los datos en forma de filas
 *  pero sin tener la información de los atributos. Ejemplo:
 *  Clase,Código Producto,Descripción,Precio,Stock,Caducidad
    Producto,TECL5678X,Teclado mecánico RGB con switches rojos,89.99,45
    Producto,RATN9012K,Ratón inalámbrico ergonómico con 5 botones,34.50,67
    Producto,AURC3456L,Auriculares inalámbricos con cancelación de ruido,129.99,23
    Producto,WEBC7890P,Webcam Full HD 1080p con micrófono integrado,59.90,32
    Producto,HUBB2345M,Hub USB 3.0 de 4 puertos con alimentación,24.75,56
    Producto,INK55665F,Toner b/w genérico HP 8750,79.99,18,20260713
    
    - En el ejemplo anterior, la última línea es una producto perecedero
    
 *  - Podéis reutilizar código del Taller mecánico, el que queráis
 *  - El examen dura unas 2 horas y media (se cerrará la entrega a las 10:55)
 *  
 *  - Antes de esa hora, hay que entregar el proyecto en ZIP en la classroom
 *  - Después, se puede seguir trabajando en el proyecto, que completo, se defenderá
 *  a la vuelta de semana blanca.
 *  
 *  Evaluación: 
 *  1ª Oportunidad: ZIP entregado en la classroom
 *  2ª Oportunidad: Repositorio depués de semana blanca
 *  
 *  Cada opción de menú: 1.25 puntos. Total: 10p. Aprobar: 5p.
 *  
 */
public class Main {
    // Clase principal de la vista
    public static void main(String[] args) {
        //Poner aquí el bucle para ejecutar mostrar menú, escoger opción y ejecutarla la opción
        //Hasta pulsar opción 0 que es salir
        byte option;
        Scanner sc = new Scanner(System.in);
        Controlador singleton = Controlador.getSingleton();
        do {
            mostrarMenu();
            option = sc.nextByte();
            sc.nextLine();
            switch (option) {
                case 0: break;
                case 1: 
                    sc.nextLine();
                    addProductMenu();
                    String code = sc.nextLine();
                    sc.nextLine();
                    String description = sc.nextLine();
                    double price = sc.nextDouble();
                    int stock = sc.nextInt();
                    sc.nextLine();
                    System.out.println("¿Es perecedero? (s/n): ");
                    String esPerecedero = sc.nextLine();
                    String expdate = null;
                    if (esPerecedero.equalsIgnoreCase("s")) {
                        System.out.print("Fecha de caducidad (AAAAMMDD): ");
                        expdate = sc.nextLine();
                    }
                    System.out.println(singleton.addProduct(code,description,price,stock,expdate));                    
                    break;
                case 2:
                    String choice;
                    int newstock;
                    System.out.println("Seleccione el producto del que desee actualizar el stock");
                    System.out.println(singleton.productsToString());
                        choice = sc.nextLine();
                    System.out.println("Ahora introduzca el nuevo stock");
                        newstock = sc.nextInt();
                    sc.nextLine();
                    
                    System.out.println(singleton.updateStock(choice, newstock));
                    break;
                case 3:
                    System.out.println(singleton.productsToStringSorted());
                    break;
                case 4:
                    System.out.println("Productos disponibles para retirar:");
                    System.out.println(singleton.productsToString());
                
                    System.out.print("Introduzca el código del producto a retirar: ");
                    String codigoRetirar = sc.nextLine();
                
                    System.out.println(singleton.delProduct(codigoRetirar));
                    break;
                case 5:
                    System.out.println("Productos con stock 0:");
                    System.out.println(singleton.prodNoStock());
                    break;
                case 6:
                    System.out.println("Productos caducados:");
                    System.out.println(singleton.expProducts());
                    break;
                case 7:
                    System.out.print("Introduzca el precio mínimo: ");
                    double minPrice = sc.nextDouble();
                    System.out.print("Introduzca el precio máximo: ");
                    double maxPrice = sc.nextDouble();
                
                    System.out.println("Productos entre " + minPrice + " y " + maxPrice + ":");
                    System.out.println(singleton.prodBetweenPrices(minPrice, maxPrice));
                    break;
                case 8:
                    System.out.println("Productos retirados:");
                    System.out.println(singleton.expProductsToString());
                    break;
            }
        }while (option != 0);
    }
    public static void mostrarMenu() {
        System.out.println("--------------------------------------------------------");
        System.out.println("-                                                      -");
        System.out.println("- Menú del proyecto: Almacén de productos              -");
        System.out.println("-                                                      -");
        System.out.println("- 0. Salir                                             -");
        System.out.println("- 1. Añadir nuevo producto                             -");
        System.out.println("- 2. Añadir/quitar stock a un producto                 -");
        System.out.println("- 3. Listar todo (productos ordenados por descripción) -");
        System.out.println("- 4. Retirar un producto y su stock                    -");
        System.out.println("- 5. Mostrar productos con stock 0                     -");
        System.out.println("- 6. Mostrar productos caducados                       -");
        System.out.println("- 7. Mostrar productos entre dos precios               -");
        System.out.println("- 8. Mostrar productos retirados                       -");
        System.out.println("-                                                      -");
        System.out.println("--------------------------------------------------------");
    }
    public static void addProductMenu() {
        System.out.println("Añada los datos del nuevo producto en el siguiente orden: ");
        System.out.println("Codigo del producto | Breve descripcion | Precio | Stock incial");
    }
}