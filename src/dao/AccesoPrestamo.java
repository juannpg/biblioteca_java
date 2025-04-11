package dao;

import config.ConfigSQLite;
import exceptions.BDException;
import exceptions.ExcepcionPrestamo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import models.Libro;
import models.Prestamo;
import models.PrestamoExtendido;
import models.Socio;

public class AccesoPrestamo {

  /**
   * metodo abstraido de insertarPrestamo para simplificar el código. no incluir en el main
   * @param codigoLibro
   * @return booleano indicando si el libro está prestado
   * @throws BDException
   */
  private static boolean estaLibroPrestado(int codigoLibro) throws BDException {
    Connection conexion = null;

    try {
      conexion = ConfigSQLite.abrirConexion();
      String queryEstaPrestado = "select fecha_devolucion from prestamo where codigo_libro = ?";
      PreparedStatement psEstaPrestado = conexion.prepareStatement(queryEstaPrestado);

      psEstaPrestado.setInt(1, codigoLibro);
      ResultSet resultadoEstaPrestado = psEstaPrestado.executeQuery();

      String fechaDevolucionEstaPrestado = null;
      while (resultadoEstaPrestado.next()) {
        fechaDevolucionEstaPrestado = resultadoEstaPrestado.getString("fecha_devolucion");
        if (fechaDevolucionEstaPrestado == null) {
          return true;
        }
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }

    return false;
  }

  /**
   * metodo abstraido de insertarPrestamo para simplificar el código. no incluir en el main
   * @param codigoLibro
   * @return booleano indicando si el socio tiene un libro prestado
   * @throws BDException
   */
  private static boolean tieneLibroPrestado(int codigoSocio) throws BDException {
    Connection conexion = null;

    try {
      conexion = ConfigSQLite.abrirConexion();

      String queryTienePrestamo = "select fecha_devolucion from prestamo where codigo_socio = ?";
      PreparedStatement psTienePrestamo = conexion.prepareStatement(queryTienePrestamo);

      psTienePrestamo.setInt(1, codigoSocio);
      ResultSet resultadoTienePrestamo = psTienePrestamo.executeQuery();

      String fechaDevolucionTienePrestamo = null;
      while (resultadoTienePrestamo.next()) {
        fechaDevolucionTienePrestamo = resultadoTienePrestamo.getString("fecha_devolucion");
        if (fechaDevolucionTienePrestamo == null) {
          return true;
        }
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }
    return false;
  }

  public static boolean tienePrestamoHoy(int codigoSocio) throws BDException {
    Connection conexion = null;
    boolean tienePrestamo = false;

    try {
      conexion = ConfigSQLite.abrirConexion();

      String queryTienePrestamo = "select codigo_socio from prestamo where codigo_socio = ? and fecha_inicio = ?";
      PreparedStatement psTienePrestamo = conexion.prepareStatement(queryTienePrestamo);

      DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate fechaActual = LocalDate.now();
      String fechaActualParseada = fechaActual.format(formato);
      System.out.println(fechaActualParseada);

      psTienePrestamo.setInt(1, codigoSocio);
      psTienePrestamo.setString(2, fechaActualParseada);
      ResultSet resultadoTienePrestamo = psTienePrestamo.executeQuery();

      if (resultadoTienePrestamo.next()) {
        tienePrestamo = true;
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }
    return tienePrestamo;
  }

  /**
   * Inserta un préstamo dados un codigo de libro, un codigo de socio, la fecha de inicio y la de fin.
   * @param codigoLibro
   * @param codigoSocio
   * @param fechaInicio
   * @param fechaFin
   * @return si se ha insertado o no.
   * @throws BDException Si la consulta sale mal
   * @throws ExcepcionPrestamo Si ese libro ya está prestado (imprimir el mensaje de la excepcion)
   * @throws ExcepcionPrestamo Si ese socio ya tiene un libro prestado (imprimir el mensaje de la excepcion)
   */
  public static boolean insertarPrestamo(int codigoLibro, int codigoSocio, String fechaFin)
    throws BDException, ExcepcionPrestamo {
    Connection conexion = null;
    int filasAfectadas = 0;

    try {
      conexion = ConfigSQLite.abrirConexion();

      boolean tienePrestamoHoy = tienePrestamoHoy(codigoSocio);
      if (tienePrestamoHoy) {
        throw new ExcepcionPrestamo(ExcepcionPrestamo.YA_PRESTAMO_HOY);
      }

      // comprobamos si el libro está prestado
      boolean estaPrestado = estaLibroPrestado(codigoLibro);
      if (estaPrestado) {
        throw new ExcepcionPrestamo(ExcepcionPrestamo.ESTA_PRESTADO);
      }

      // comprobamos si el socio tiene algún préstamo activo
      boolean tieneLibroPrestado = tieneLibroPrestado(codigoSocio);
      if (tieneLibroPrestado) {
        throw new ExcepcionPrestamo(ExcepcionPrestamo.TIENE_PRESTADO);
      }

      // comprobamos si la fecha de fin es menor a la fecha
      DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate fechaActual = LocalDate.now();
      LocalDate fechaFinParseada = LocalDate.parse(fechaFin, formato);

      if (fechaFinParseada.isBefore(fechaActual.plus(Period.ofDays(1)))) {
        throw new ExcepcionPrestamo(ExcepcionPrestamo.FECHA_MENOR_HOY);
      }

      // lo demás
      String queryInsert =
        "insert into prestamo (codigo_libro, codigo_socio, fecha_inicio, fecha_fin)" + " values (?, ?, ? ,?)";
      PreparedStatement psInsert = conexion.prepareStatement(queryInsert);

      String fechaFormateada = fechaActual.format(formato);

      psInsert.setInt(1, codigoLibro);
      psInsert.setInt(2, codigoSocio);
      psInsert.setString(3, fechaFormateada);
      psInsert.setString(4, fechaFin);

      filasAfectadas = psInsert.executeUpdate();
    } catch (SQLException e) {
      throw new ExcepcionPrestamo(ExcepcionPrestamo.NO_EXISTE_LIBRO_SOCIO);
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }

    return filasAfectadas == 1;
  }

  /**
   * actualiza la fecha de inicio y la de fin de un préstamo, utilizando su codigo de libro y de inicio como clave
   * @param codigoLibro
   * @param codigoSocio
   * @param fechaInicio
   * @param fechaFin
   * @return true si se ha actualizado (escribir en base al booleano)
   * @return false si no se ha actualizado (escribir en base al booleano)
   * @throws BDException
   */
  public static boolean actualizarPrestamo(int codigoLibro, int codigoSocio, String fechaInicio, String fechaFin)
    throws BDException, ExcepcionPrestamo {
    Connection conexion = null;
    int filasAfectadas = 0;

    try {
      conexion = ConfigSQLite.abrirConexion();

      // comprobamos si la fecha de fin es menor a la fecha
      DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate fechaInicioParseada = LocalDate.parse(fechaInicio, formato);
      LocalDate fechaFinParseada = LocalDate.parse(fechaFin, formato);

      if (fechaFinParseada.isBefore(fechaInicioParseada.plus(Period.ofDays(1)))) {
        throw new ExcepcionPrestamo(ExcepcionPrestamo.FECHA_MENOR_INICIO);
      }

      String queryUpdate =
        "update prestamo " + "set fecha_fin = ? " + "where codigo_libro = ? and codigo_socio = ? and fecha_inicio = ?";
      PreparedStatement psInsert = conexion.prepareStatement(queryUpdate);

      psInsert.setString(1, fechaFin);
      psInsert.setInt(2, codigoLibro);
      psInsert.setInt(3, codigoSocio);
      psInsert.setString(4, fechaInicio);

      filasAfectadas = psInsert.executeUpdate();
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }

    return filasAfectadas == 1;
  }

  /**
   * elimina un préstamo de la base de datos en base a su codigo de libro y codigo de socio
   * @param codigoLibro
   * @param codigoSocio
   * @return true si se ha eliminado (escribir en base al booleano)
   * @return false si no se ha eliminado (escribir en base al booleano)
   * @throws BDException
   */
  public static boolean eliminarPrestamo(int codigoLibro, int codigoSocio, String fechaInicio)
    throws BDException, ExcepcionPrestamo {
    Connection conexion = null;
    int filasAfectadas = 0;

    try {
      conexion = ConfigSQLite.abrirConexion();

      String queryDelete = "delete from prestamo " + "where codigo_libro = ? and codigo_socio = ? and fecha_inicio = ?";
      PreparedStatement psDelete = conexion.prepareStatement(queryDelete);

      psDelete.setInt(1, codigoLibro);
      psDelete.setInt(2, codigoSocio);
      psDelete.setString(3, fechaInicio);

      filasAfectadas = psDelete.executeUpdate();

      if (filasAfectadas == 0) {
        throw new ExcepcionPrestamo(ExcepcionPrestamo.NO_EXISTE_PRESTAMO);
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }

    return filasAfectadas == 1;
  }

  /**
   * consulta todos los prestamos de la base de datos. Si devuelve null es que está vacía
   * @return ArrayList con todos los prestamos
   * @throws BDException
   */
  public static ArrayList<Prestamo> consultarTodosPrestamos() throws BDException {
    ArrayList<Prestamo> prestamos = new ArrayList<>();

    Connection conexion = null;
    try {
      conexion = ConfigSQLite.abrirConexion();

      String query = "select * from prestamo";
      PreparedStatement ps = conexion.prepareStatement(query);

      ResultSet resultados = ps.executeQuery();

      while (resultados.next()) {
        prestamos.add(
          new Prestamo(
            AccesoLibro.consultarLibroPorCodigo(resultados.getInt("codigo_libro")),
            AccesoSocio.consultarSocioPorCodigo(resultados.getInt("codigo_socio")),
            resultados.getString("fecha_inicio"),
            resultados.getString("fecha_fin"),
            resultados.getString("fecha_devolucion")
          )
        );
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }

    return prestamos;
  }

  /**
   * consulta todos los prestamos que aún no han sido devueltos.
   * @return arraylist con los prestamos
   * @throws BDException
   */
  public static ArrayList<Prestamo> consultarLosPrestamosNoDevueltos() throws BDException {
    ArrayList<Prestamo> prestamos = new ArrayList<>();

    Connection conexion = null;
    try {
      conexion = ConfigSQLite.abrirConexion();

      String query = "select * from prestamo where fecha_devolucion is null";
      PreparedStatement ps = conexion.prepareStatement(query);

      ResultSet resultados = ps.executeQuery();

      while (resultados.next()) {
        prestamos.add(
          new Prestamo(
            AccesoLibro.consultarLibroPorCodigo(resultados.getInt("codigo_libro")),
            AccesoSocio.consultarSocioPorCodigo(resultados.getInt("codigo_socio")),
            resultados.getString("fecha_inicio"),
            resultados.getString("fecha_fin"),
            resultados.getString("fecha_devolucion")
          )
        );
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }

    return prestamos;
  }

  /**
   * consulta todos los prestamos cuya fecha de inicio sea la dada. Utiliza el modelo PrestamoExtendido
   * para mostrar los datos requeridos
   * @param fechaInicio
   * @return ArrayList de prestamoExtendido
   * @throws BDException
   */
  public static ArrayList<PrestamoExtendido> consultarPrestamosExtendidosConFechaDevolucion(String fechaInicio)
    throws BDException {
    ArrayList<PrestamoExtendido> prestamos = new ArrayList<>();

    Connection conexion = null;
    try {
      conexion = ConfigSQLite.abrirConexion();

      String query =
        "select s.dni, s.nombre, l.isbn, l.titulo, p.fecha_devolucion from prestamo p " +
        "join socio s on p.codigo_socio = s.codigo " +
        "join libro l on p.codigo_libro = l.codigo " +
        "where p.fecha_inicio = ?";
      PreparedStatement ps = conexion.prepareStatement(query);

      ps.setString(1, fechaInicio);

      ResultSet resultados = ps.executeQuery();

      while (resultados.next()) {
        prestamos.add(
          new PrestamoExtendido(
            resultados.getString("dni"),
            resultados.getString("nombre"),
            resultados.getString("isbn"),
            resultados.getString("titulo"),
            resultados.getString("fecha_devolucion")
          )
        );
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }

    return prestamos;
  }

  /**
   * consulta el numero de veces que ha sido prestado un libro, junto con su titulo e isbn
   *
   * EJEMPLO DE USO:
   * 		try {
   *   		LinkedHashMap<Libro, Integer> mapa = consultarNumeroDeVecesLibrosPrestados();
   * 			for (Libro l : mapa.keySet()) {
   *				System.out.println("ISBN: " + l.getIsbn() + ", Titulo: " + l.getTitulo() + ", numero de veces prestado: " + mapa.get(l));
   * 			}
   * 		} catch (BDException e) {
   * 			System.out.println(e.getMessage());
   * 		}
   *
   * @return un mapa de llave libro y objeto veces que se ha prestado
   * @throws BDException
   */
  public static LinkedHashMap<Libro, Integer> consultarNumeroDeVecesLibrosPrestados() throws BDException {
    LinkedHashMap<Libro, Integer> mapa = new LinkedHashMap<>();

    Connection conexion = null;
    try {
      conexion = ConfigSQLite.abrirConexion();

      String query =
        "select l.isbn, l.titulo, count(p.codigo_libro) as \"numero_de_veces_prestado\" from libro l " +
        "join prestamo p on p.codigo_libro = l.codigo " +
        "group by l.isbn, l.titulo " +
        "order by count(p.codigo_libro) desc";

      PreparedStatement ps = conexion.prepareStatement(query);

      ResultSet resultados = ps.executeQuery();

      while (resultados.next()) {
        mapa.put(
          new Libro(resultados.getString("isbn"), resultados.getString("titulo")),
          resultados.getInt("numero_de_veces_prestado")
        );
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }

    return mapa;
  }

  /**
   * consulta el numero de veces que un socio ha hecho prestamos, con su dni y nombre
   * @return un mapa de llave socio y objeto veces que ha prestado
   * @throws BDException
   */
  public static LinkedHashMap<Socio, Integer> consultarNumeroDeVecesPrestamosDeSocios() throws BDException {
    LinkedHashMap<Socio, Integer> mapa = new LinkedHashMap<>();

    Connection conexion = null;
    try {
      conexion = ConfigSQLite.abrirConexion();

      String query =
        "select s.dni, s.nombre, count(p.codigo_socio) as \"numero_de_prestamos\" from socio s " +
        "join prestamo p on p.codigo_socio = s.codigo " +
        "group by s.dni, s.nombre " +
        "order by count(p.codigo_socio) desc";

      PreparedStatement ps = conexion.prepareStatement(query);

      ResultSet resultados = ps.executeQuery();

      while (resultados.next()) {
        mapa.put(
          new Socio(resultados.getString("dni"), resultados.getString("nombre")),
          resultados.getInt("numero_de_prestamos")
        );
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }

    return mapa;
  }
}
