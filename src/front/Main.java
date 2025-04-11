package front;

import dao.AccesoLibro;
import dao.AccesoPrestamo;
import dao.AccesoSocio;
import entrada.Teclado;
import exceptions.BDException;
import exceptions.ExcepcionPrestamo;
import exceptions.LibroException;
import exceptions.SocioException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import models.Libro;
import models.Prestamo;
import models.PrestamoExtendido;
import models.Socio;
import regex.FuncionesRegex;

public class Main {

  public static void escribirMenuOpcionesLibro() {
    System.out.println();
    System.out.println("Elige una opción del menu de Libro");
    System.out.println("0) Salir del programa.");
    System.out.println("1) Insertar un libro en la base de datos");
    System.out.println("2) Eliminar un libro, por código, de la base de datos");
    System.out.println("3) Consultar todos los libros de la base de datos.");
    System.out.println(
      "4) Consultar varios libros, por escritor, de la base de datos, ordenados por puntuación decendente."
    );
    System.out.println("5) Consultar los libros no prestados de la base de datos.");
    System.out.println("6) Consultar los libros devueltos, en una fecha, de la base de datos.");
  }

  public static void escribirMenuOpcionesSocio() {
    System.out.println();
    System.out.println("Elige una opción del menu de Socio");

    System.out.println("0) Salir del programa.");
    System.out.println("1) Insertar un socio en la base de datos");
    System.out.println("2) Eliminar un socio, por código, de la base de datos");
    System.out.println("3) Consultar todos los socios de la base de datos.");
    System.out.println(
      "4) Consultar varios socios, por localidad, de la base de datos, ordenados por nombre ascendiente."
    );
    System.out.println("5) Consultar los socios sin prestamos de la base de datos.");
    System.out.println("6) Consultar los socios con prestamos, en una fecha, de la base de datos.");
  }

  public static void escribirMenuOpcionesPrestamo() {
    System.out.println();
    System.out.println("Elige una opción del menu de Prestamo");
    System.out.println("0) Salir del programa.");
    System.out.println("1) Insertar un prestamo en la base de datos");
    System.out.println("2) Actualizar un préstamo, por datos identificativos, de la base de datos");
    System.out.println("3) Eliminar un préstamo, por datos identificativos, de la base de datos.");
    System.out.println("4) Consultar todos los préstamos de la base de datos.");
    System.out.println("5) Consultar los préstamos no devueltos de la base de datos.");
    System.out.println(
      "6) Consultar DNI y nombre de socio, ISBN y título de libro y fecha de devolución de los\n" +
      "préstamos realizados en una fecha de la base de datos."
    );
  }

  public static void escribirMenuOpcionesAvanzadas() {
    System.out.println("0) Salir del programa");
    System.out.println(
      "1) Consultar el libro o los libros que ha/n sido prestado/s menos veces (y que como mínimo haya/n sido prestado/s una vez)."
    );
    System.out.println("2) Consultar el socio o los socios que ha/n realizado más préstamos.");
    System.out.println(
      "3) Consultar los libros que han sido prestados (incluyendo los libros no devueltos) una cantidad de veces inferior a la media."
    );
    System.out.println("4) Consultar los socios que han realizado una cantidad de préstamos superior a la media.");
    System.out.println(
      "5) Consultar el ISBN, el título y el número de veces de los libros que han sido prestados, ordenados por el número de préstamos descendente."
    );
    System.out.println(
      "6) Consultar el DNI, el nombre y el número de veces de los socios que han realizado préstamos, ordenados por el número de préstamos descendente."
    );
  }

  public static void menuLibros() {
    int opcion = -1;

    do {
      try {
        escribirMenuOpcionesLibro();
        System.out.println();
        opcion = Teclado.leerEntero("Opción: ");
        switch (opcion) {
          case 1:
            System.out.println("Insertar libro...");
            // Pedir datos al usuario
            String isbn = Teclado.leerCadena("Introduce el ISBN: ");
            while (!FuncionesRegex.isbnBien(isbn)) {
              isbn = Teclado.leerCadena("Introduce un ISBN valido (isbn-10 o isbn-13): ");
            }
            String titulo = Teclado.leerCadena("Introduce el título: ");
            String escritor = Teclado.leerCadena("Introduce el escritor: ");
            int anyoPublicacion = Teclado.leerEntero("Introduce el año de publicación: ");
            while (!FuncionesRegex.anyoBien(anyoPublicacion)) {
              anyoPublicacion = Teclado.leerEntero("Año válido por favor: ");
            }
            float puntuacion = (float) Teclado.leerReal("Introduce la puntuación: ");

            boolean anadiLibro = AccesoLibro.anadirLibro(isbn, titulo, escritor, anyoPublicacion, puntuacion);

            if (anadiLibro) {
              System.out.println("Libro añadido correctamente.");
            } else {
              System.out.println("No se pudo añadir el libro.");
            }

            break;
          case 2:
            System.out.println("Eliminar libro...");
            // pedir datos al usuario
            int codigo = Teclado.leerEntero("Introduce el código del libro: ");

            boolean borrarLibroPorCodigo = AccesoLibro.borrarLibroPorCodigo(codigo);

            if (borrarLibroPorCodigo) {
              System.out.println("Libro eliminado correctamente.");
            } else {
              System.out.println("No se pudo eliminar el libro.");
            }

            break;
          case 3:
            System.out.println("Consultar todos los libros...");

            ArrayList<Libro> consultarLibros = AccesoLibro.consultarLibros();

            if (consultarLibros.isEmpty()) {
              System.out.println("No hay ningun libro en la coleccion");
            } else {
              System.out.println("Lista de libros");
              for (Libro libro : consultarLibros) {
                System.out.println("- " + libro);
              }
            }

            break;
          case 4:
            System.out.println("Consultar libros por escritor...");
            // pedir datos al usuario
            String nombreEscritor = Teclado.leerCadena("Introduce el nombre del escritor: ");

            ArrayList<Libro> consultarLibrosOrdenados = AccesoLibro.consultarLibrosOrdenados(nombreEscritor);

            if (consultarLibrosOrdenados.isEmpty()) {
              System.out.println("No se encontro ningún libro");
            } else {
              System.out.println("Lista de libros: ");
              for (Libro libro : consultarLibrosOrdenados) {
                System.out.println("- " + libro);
              }
            }
            break;
          case 5:
            System.out.println("Consultar libros no prestados...");

            ArrayList<Libro> consultarLibrosNoPrestados = AccesoLibro.consultarLibrosNoPrestados();

            if (consultarLibrosNoPrestados.isEmpty()) {
              System.out.println("No se encontro ningún libro");
            } else {
              System.out.println("Lista de libros no prestados: ");
              for (Libro libro : consultarLibrosNoPrestados) {
                System.out.println("- " + libro);
              }
            }
            break;
          case 6:
            System.out.println("Consultar libros devueltos en una fecha...");
            // pedir datos al usuario
            String fechaDevolucion = Teclado.leerCadena("Introruce una fecha: ");
            while (!FuncionesRegex.fechaBien(fechaDevolucion)) {
              fechaDevolucion = Teclado.leerCadena("Introduce una fecha valida (yyyy-mm-dd): ");
            }

            ArrayList<Libro> consultarLibrosDevueltos = AccesoLibro.consultarLibrosDevueltos(fechaDevolucion);

            if (consultarLibrosDevueltos.isEmpty()) {
              System.out.println("No se encontro ningún libro");
            } else {
              System.out.println("Lista de libros devueltos en la fehca: " + fechaDevolucion);
              for (Libro libro : consultarLibrosDevueltos) {
                System.out.println("- " + libro);
              }
            }
            break;
          case 0:
            System.out.println("Regresando al menú principal...");
            break;
          default:
            System.out.println("Opción no válida. Intente de nuevo.");
        }
      } catch (BDException e) {
        System.out.println("Error en la consulta, mensaje de error: " + e.getMessage());
      } catch (LibroException e) {
        System.out.println(e.getMessage());
      }
    } while (opcion != 0);
  }

  public static void menuSocios() {
    int opcion = -1;
    do {
      try {
        escribirMenuOpcionesSocio();
        System.out.println();
        opcion = Teclado.leerEntero("Opción: ");

        switch (opcion) {
          case 1:
            System.out.println("Insertar socio...");
            String dni = Teclado.leerCadena("Introduce el DNI");
            while (!FuncionesRegex.dniBien(dni)) {
              dni = Teclado.leerCadena("Introduce un DNI váliodo: ");
            }
            String nombre = Teclado.leerCadena("Introduce el nombre");
            String domicilio = Teclado.leerCadena("Introducir el domicilio");
            String telefono = Teclado.leerCadena("Introduce el numero de telefono");
            while (!FuncionesRegex.telefonoBien(telefono)) {
              telefono = Teclado.leerCadena("Telefono valido: ");
            }
            String correo = Teclado.leerCadena("Introduce el correo");
            while (!FuncionesRegex.correoBien(correo)) {
              correo = Teclado.leerCadena("Correo valido: ");
            }
            Socio socio = new Socio(0, dni, nombre, domicilio, telefono, correo);
            boolean agregarSocio = AccesoSocio.agregarSocio(socio);
            if (!agregarSocio) {
              System.out.println("No se pudo agegar un nuevo socio");
            } else {
              System.out.println("Socio agregado con exito");
            }
            break;
          case 2:
            System.out.println("Eliminar socio...");
            // pedir datos al usuario
            int codigoSocio = Teclado.leerEntero("Introduce el código del Socio: ");

            boolean eliminarSocio = AccesoSocio.eliminarSocio(codigoSocio);

            if (eliminarSocio) {
              System.out.println("Socio eliminado correctamente.");
            } else {
              System.out.println("No se pudo eliminar el Socio. No existe ese socio");
            }

            break;
          case 3:
            System.out.println("Consultar todos los socios...");

            ArrayList<Socio> consultarTodosSocios = AccesoSocio.consultarTodosSocios();

            if (consultarTodosSocios.isEmpty()) {
              System.out.println("No hay ningun socio");
            } else {
              System.out.println("Lista de socios encontrados: ");
              for (Socio socio1 : consultarTodosSocios) {
                System.out.println("- " + socio1);
              }
            }

            break;
          case 4:
            System.out.println("Consultar socios por localidad...");
            // pedir datos al usuario
            String localidad = Teclado.leerCadena("Introduce una localidad");

            ArrayList<Socio> consultarSociosPorLocalidadOrdenadosPorNombre =
              AccesoSocio.consultarSociosPorLocalidadOrdenadosPorNombre(localidad);

            if (consultarSociosPorLocalidadOrdenadosPorNombre.isEmpty()) {
              System.out.println("No se encontro nigun Socio en esta localidad");
            } else {
              System.out.println("Lista de socios encontrados en la localidad: " + localidad);
              for (Socio socio2 : consultarSociosPorLocalidadOrdenadosPorNombre) {
                System.out.println("- " + socio2);
              }
            }

            break;
          case 5:
            System.out.println("Consultar socios sin préstamos...");

            ArrayList<Socio> consultarSociosNoPrestatario = AccesoSocio.consultarSociosNoPrestatario();

            if (consultarSociosNoPrestatario.isEmpty()) {
              System.out.println("No se encontro ningun socio");
            } else {
              System.out.println("Lista de socios sin préstamos: ");

              for (Socio socio3 : consultarSociosNoPrestatario) {
                System.out.println("- " + socio3);
              }
            }

            break;
          case 6:
            System.out.println("Consultar socios con préstamos en una fecha...");
            // pedir datos al usuario
            String fecha = Teclado.leerCadena("Introduce una fecha (yyyy-mm-dd)");
            while (!FuncionesRegex.fechaBien(fecha)) {
              fecha = Teclado.leerCadena("Fecha valida (yyyy-mm-dd): ");
            }

            ArrayList<Socio> consultarSociosPrestatario = AccesoSocio.consultarSociosPrestatario(fecha);

            if (consultarSociosPrestatario.isEmpty()) {
              System.out.println("No se encontro ningun socio");
            } else {
              System.out.println("Lista de socios encontrados en la fecha: " + fecha);

              for (Socio socio4 : consultarSociosPrestatario) {
                System.out.println("- " + socio4);
              }
            }

            break;
          case 0:
            System.out.println("Regresando al menú principal...");

            break;
          default:
            System.out.println("Opción no válida. Intente de nuevo.");
        }
      } catch (BDException e) {
        System.out.println("Error en la consulta, mensaje de error:  " + e.getMessage());
      } catch (SocioException e) {
        System.out.println(e.getMessage());
      }
    } while (opcion != 0);
  }

  public static void menuPrestamos() {
    int opcion = -1;
    do {
      try {
        escribirMenuOpcionesPrestamo();
        System.out.println();
        opcion = Teclado.leerEntero("Opción: ");

        switch (opcion) {
          case 1:
            System.out.println("Insertar préstamo...");
            // pedir datos al usuario
            int codigoLibro = Teclado.leerEntero("Introduce el codigo del libro");
            int codigoSocio = Teclado.leerEntero("Introduce el codigo del socio");
            String fechaFin = Teclado.leerCadena("Introduce la fecha de fin");
            while (!FuncionesRegex.fechaBien(fechaFin)) {
              fechaFin = Teclado.leerCadena("Fecha valida (yyyy-mm-dd): ");
            }

            boolean insertarPrestamo = AccesoPrestamo.insertarPrestamo(codigoLibro, codigoSocio, fechaFin);

            if (!insertarPrestamo) {
              System.out.println("El prestamo no se pudo realizar");
            } else {
              System.out.println("Prestamo realizado con exito");
            }
            break;
          case 2:
            System.out.println("Actualizar préstamo...");
            // pedir datos al usuario
            codigoLibro = Teclado.leerEntero("Introduce el codigo del libro");
            codigoSocio = Teclado.leerEntero("Introduce el codigo del socio");
            String fechaInicio = Teclado.leerCadena("Introduce la fecha de inicio");
            while (!FuncionesRegex.fechaBien(fechaInicio)) {
              fechaInicio = Teclado.leerCadena("Fecha valida (yyyy-mm-dd): ");
            }
            fechaFin = Teclado.leerCadena("Introduce la fecha de fin");
            while (!FuncionesRegex.fechaBien(fechaFin)) {
              fechaFin = Teclado.leerCadena("Fecha valida (yyyy-mm-dd): ");
            }

            boolean actualizarPrestamo = AccesoPrestamo.actualizarPrestamo(
              codigoLibro,
              codigoSocio,
              fechaInicio,
              fechaFin
            );

            if (!actualizarPrestamo) {
              System.out.println(
                "No se pudo actualizar el prestamo. No existe un préstamo con esos datos identificativos."
              );
            } else {
              System.out.println("Prestamo actualizado con exito");
            }
            break;
          case 3:
            System.out.println("Eliminar préstamo...");
            // pedir datos al usuario
            codigoLibro = Teclado.leerEntero("Introduce el codigo del libro");
            codigoSocio = Teclado.leerEntero("Introduce el codigo del socio");
            fechaInicio = Teclado.leerCadena("Introduce la fecha de inicio");
            while (!FuncionesRegex.fechaBien(fechaInicio)) {
              fechaInicio = Teclado.leerCadena("Fecha valida (yyyy-mm-dd): ");
            }

            boolean eliminarPrestamo = AccesoPrestamo.eliminarPrestamo(codigoLibro, codigoSocio, fechaInicio);

            if (!eliminarPrestamo) {
              System.out.println("No se pudo eliminar el prestamo");
            } else {
              System.out.println("Prestamo eliminado con exito");
            }
            break;
          case 4:
            System.out.println("Consultar todos los préstamos...");

            ArrayList<Prestamo> consultarTodosPrestamos = AccesoPrestamo.consultarTodosPrestamos();

            if (consultarTodosPrestamos.isEmpty()) {
              System.out.println("No se encontro ningun prestamo");
            } else {
              System.out.println("Los prestamos encontrados: ");

              for (Prestamo prestamo : consultarTodosPrestamos) {
                System.out.println();
                System.out.println("- " + prestamo);
              }
            }
            break;
          case 5:
            System.out.println("Consultar préstamos no devueltos...");

            ArrayList<Prestamo> consultarLosPrestamosNoDevueltos = AccesoPrestamo.consultarLosPrestamosNoDevueltos();

            if (consultarLosPrestamosNoDevueltos.isEmpty()) {
              System.out.println("No se encontro ningun prestamo no devuelto");
            } else {
              System.out.println("Los prestamos no devueltos encontrados: ");

              for (Prestamo prestamo : consultarLosPrestamosNoDevueltos) {
                System.out.println("- " + prestamo);
              }
            }
            break;
          case 6:
            System.out.println("Consultar préstamos realizados en una fecha...");
            // pedir datos al usuario
            fechaInicio = Teclado.leerCadena("Introduce la fecha de inicio");
            while (!FuncionesRegex.fechaBien(fechaInicio)) {
              fechaInicio = Teclado.leerCadena("Fecha valida (yyyy-mm-dd): ");
            }

            ArrayList<PrestamoExtendido> consultarPrestamosExtendidosConFechaDevolucion =
              AccesoPrestamo.consultarPrestamosExtendidosConFechaDevolucion(fechaInicio);

            if (consultarPrestamosExtendidosConFechaDevolucion.isEmpty()) {
              System.out.println("No se encontro ninguna devolucion para esta fecha");
            } else {
              System.out.println("Los prestamos realizados en la fecha: " + fechaInicio);

              for (PrestamoExtendido prestamo : consultarPrestamosExtendidosConFechaDevolucion) {
                System.out.println("- " + prestamo);
              }
            }

            break;
          case 0:
            System.out.println("Regresando al menú principal...");

            break;
          default:
            System.out.println("Opción no válida. Intente de nuevo.");
        }
      } catch (BDException e) {
        System.out.println("Error al ejecutar opcion del Menu Libro: " + e.getMessage());
      } catch (ExcepcionPrestamo e) {
        System.out.println("Error con prestamo: " + e.getMessage());
      }
    } while (opcion != 0);
  }

  public static void menuAvanzado() {
    int opcion = -1;
    do {
      try {
        escribirMenuOpcionesAvanzadas();
        System.out.println();
        opcion = Teclado.leerEntero("Opción: ");

        switch (opcion) {
          case 1:
            System.out.println("Consulta libros...");

            LinkedHashMap<Libro, Integer> consultarMenorLibroPrestado = AccesoLibro.consultarMenorLibroPrestado();

            if (consultarMenorLibroPrestado.isEmpty()) {
              System.out.println("No se encontro ningun libro prestado");
            } else {
              for (Libro libro : consultarMenorLibroPrestado.keySet()) {
                System.out.println("- " + libro + " | veces prestado: " + consultarMenorLibroPrestado.get(libro));
              }
            }

            break;
          case 2:
            System.out.println("Consulta socio...");

            LinkedHashMap<Socio, Integer> consultarSociosMayorPrestamos = AccesoSocio.consultarSociosMayorPrestamos();

            if (consultarSociosMayorPrestamos.isEmpty()) {
              System.out.println("No se encontro ningun prestamo");
            } else {
              for (Socio socio : consultarSociosMayorPrestamos.keySet()) {
                System.out.println("- " + socio + " | total prestamos: " + consultarSociosMayorPrestamos.get(socio));
              }
            }

            break;
          case 3:
            System.out.println("Consulta libros...");

            LinkedHashMap<Libro, Integer> consultarLibroPrestadoInferiorMedia =
              AccesoLibro.consultarLibroPrestadoInferiorMedia();

            if (consultarLibroPrestadoInferiorMedia.isEmpty()) {
              System.out.println(" No se encontro ningun libro");
            } else {
              for (Libro libro : consultarLibroPrestadoInferiorMedia.keySet()) {
                System.out.println(
                  "- " + libro + " | veces prestado: " + consultarLibroPrestadoInferiorMedia.get(libro)
                );
              }
            }
            break;
          case 4:
            System.out.println("Consulta socio...");
            LinkedHashMap<Socio, Integer> consultarSociosMayorMedia = AccesoSocio.consultarSociosMayorMedia();

            if (consultarSociosMayorMedia.isEmpty()) {
              System.out.println("No se encontro ningun socio");
            } else {
              for (Socio socio : consultarSociosMayorMedia.keySet()) {
                System.out.println("- " + socio + " | total prestamos: " + consultarSociosMayorMedia.get(socio));
              }
            }

            break;
          case 5:
            System.out.println("Consultar prestamo...");
            LinkedHashMap<Libro, Integer> consultarNumeroDeVecesLibrosPrestados =
              AccesoPrestamo.consultarNumeroDeVecesLibrosPrestados();

            if (consultarNumeroDeVecesLibrosPrestados.isEmpty()) {
              System.out.println("No se encontro ningun prestamo");
            } else {
              for (Libro l : consultarNumeroDeVecesLibrosPrestados.keySet()) {
                System.out.println(
                  "ISBN: " +
                  l.getIsbn() +
                  ", Titulo: " +
                  l.getTitulo() +
                  ", numero de veces prestado: " +
                  consultarNumeroDeVecesLibrosPrestados.get(l)
                );
              }
            }

            break;
          case 6:
            System.out.println("Consultar prestamos...");
            LinkedHashMap<Socio, Integer> consultarNumeroDeVecesPrestamosDeSocios =
              AccesoPrestamo.consultarNumeroDeVecesPrestamosDeSocios();

            if (consultarNumeroDeVecesPrestamosDeSocios.isEmpty()) {
              System.out.println("No se encontro ningun prestamo");
            } else {
              for (Socio s : consultarNumeroDeVecesPrestamosDeSocios.keySet()) {
                System.out.println(
                  "DNI: " +
                  s.getDni() +
                  ", Nombre: " +
                  s.getNombre() +
                  ", numero de veces prestado: " +
                  consultarNumeroDeVecesPrestamosDeSocios.get(s)
                );
              }
            }

            break;
          case 0:
            System.out.println("Regresando al menú principal...");

            break;
          default:
            System.out.println("Opción no válida. Intente de nuevo.");
        }
      } catch (BDException e) {
        System.out.println("Error al ejecutar opcion del Menu Libro: " + e.getMessage());
      } catch (LibroException e) {
        System.out.println("Error al ejecutar opcion del Menu Libro: " + e.getMessage());
      }
    } while (opcion != 0);
  }

  public static void main(String[] args) {
    int opcion;

    do {
      System.out.println();
      System.out.println("\nSeleccione una opción:");
      System.out.println("1) Menú Libros");
      System.out.println("2) Menú Socios");
      System.out.println("3) Menú Préstamos");
      System.out.println("4) Menú Avanzado");
      System.out.println("0) Salir");

      opcion = Teclado.leerEntero("Opcion: ");

      switch (opcion) {
        case 1:
          // imprime el menu con las opciones de Libros
          menuLibros();
          break;
        case 2:
          // imprime el menu con las opciones de Socios
          menuSocios();
          break;
        case 3:
          menuPrestamos();
          // imprime el menu con las opciones de Prestamos
          break;
        case 4:
          menuAvanzado();
          // imprime el menu con las opciones de Prestamos
          break;
        case 0:
          System.out.println("Saliendo del programa...");

          break;
        default:
          System.out.println("Opción no válida. Intente de nuevo.");
      }
    } while (opcion != 0);
  }
}
