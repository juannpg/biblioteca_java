package dao;

import config.ConfigSQLite;
import entrada.Teclado;
import exceptions.BDException;
import exceptions.SocioException;
import java.nio.channels.SelectableChannel;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import models.Libro;
import models.Socio;
import org.sqlite.SQLiteConfig;

public class AccesoSocio {

  /**
   * Metodo creado para una excepcion, indica si un socio es prestatario
   * @param codigoSocio
   * @return
   * @throws BDException
   * @throws SocioException
   */
  private static boolean esPrestatario(int codigoSocio) throws BDException, SocioException {
    PreparedStatement ps = null;
    Connection conexion = null;
    boolean esPrestatario = false;
    try {
      // Conexión a la base de datos
      conexion = ConfigSQLite.abrirConexion();
      String query =
        "SELECT * FROM socio " +
        "JOIN prestamo ON (socio.codigo = prestamo.codigo_socio) " +
        "WHERE socio.codigo = ? and prestamo.fecha_devolucion is null";

      ps = conexion.prepareStatement(query);
      ps.setInt(1, codigoSocio);

      ResultSet resultados = ps.executeQuery();
      if (resultados.next()) {
        esPrestatario = true;
      }
    } catch (SQLException e) {
      throw new BDException("ERROR ES PRESTATARIO" + BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }
    return esPrestatario;
  }

  private static boolean existeSocio(Socio socio) throws BDException {
    PreparedStatement ps = null;
    Connection conexion = null;
    boolean existe = false;

    try {
      // Conexión a la base de datos
      conexion = ConfigSQLite.abrirConexion();
      String query = "select * from socio where dni like ?;";

      ps = conexion.prepareStatement(query);
      ps.setString(1, socio.getDni());

      ResultSet resultados = ps.executeQuery();

      if (resultados.next()) {
        existe = true;
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }
    return existe;
  }

  /**
   * Metodo que permite agregar un Socio
   * @param socio
   * @return booleano indicando si se ha agregado o no se ha agregado
   * @throws BDException
   * @throws SocioException
   */
  public static boolean agregarSocio(Socio socio) throws BDException, SocioException {
    PreparedStatement ps = null;
    Connection conexion = null;
    int resultados = 0;
    try {
      // Conexión a la base de datos
      conexion = ConfigSQLite.abrirConexion();
      if (existeSocio(socio)) {
        throw new SocioException(SocioException.USUARIO_EXISTE);
      }
      String query = "INSERT INTO socio (dni, nombre, domicilio, telefono, correo) " + "VALUES (?, ?, ?, ?, ?)";

      ps = conexion.prepareStatement(query);
      ps.setString(1, socio.getDni());
      ps.setString(2, socio.getNombre());
      ps.setString(3, socio.getDomicilio());

      ps.setString(4, socio.getTelefono());
      ps.setString(5, socio.getCorreo());
      resultados = ps.executeUpdate();
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }
    return resultados == 1;
  }

  /**
   * Metodo que elimina un socio
   * @param codigoSocio
   * @return booleano indicando si se ha eliminado o no se ha eliminado
   * @throws BDException
   * @throws SocioException
   */
  public static boolean eliminarSocio(int codigoSocio) throws BDException, SocioException {
    PreparedStatement ps = null;
    Connection conexion = null;
    int resultados = 0;
    try {
      // Conexión a la base de datos
      conexion = ConfigSQLite.abrirConexion();
      String query = "delete from socio where codigo = ?";

      ps = conexion.prepareStatement(query);
      ps.setInt(1, codigoSocio);

      if (esPrestatario(codigoSocio)) {
        throw new SocioException(SocioException.ERROR_SOCIO_PRESTAMO);
      }

      resultados = ps.executeUpdate();
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }
    return resultados == 1;
  }

  /**
   * Metodo que consulta todos los Socios de la BD
   * @return ArrayList de Socios
   * @throws BDException
   * @throws SocioException
   */
  public static ArrayList<Socio> consultarTodosSocios() throws BDException, SocioException {
    ArrayList<Socio> todosSocios = new ArrayList<Socio>();
    PreparedStatement ps = null;
    Connection conexion = null;
    try {
      conexion = ConfigSQLite.abrirConexion();
      String queryString = "SELECT * FROM socio";
      ps = conexion.prepareStatement(queryString);
      ResultSet resultados = ps.executeQuery();

      while (resultados.next()) {
        int codigoSocio = resultados.getInt("codigo");
        String dniSocio = resultados.getString("dni");
        String nombreSocio = resultados.getString("nombre");
        String domicilioSocio = resultados.getString("domicilio");
        String telefonoSocio = resultados.getString("telefono");
        String correoSocio = resultados.getString("correo");

        Socio socio = new Socio(codigoSocio, dniSocio, nombreSocio, domicilioSocio, telefonoSocio, correoSocio);
        todosSocios.add(socio);
      }
      if (todosSocios.isEmpty()) {
        throw new SocioException(SocioException.ERROR_BDEmpty);
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }
    return todosSocios;
  }

  /**
   * Metodo para consultar los Socios que no han hecho prestamo, es decir que no aparecen en la tabla de prestamos
   * @return ArrayList de los Socios correspondientes
   * @throws BDException
   * @throws SocioException
   */
  public static ArrayList<Socio> consultarSociosNoPrestatario() throws BDException, SocioException {
    ArrayList<Socio> todosSociosNP = new ArrayList<Socio>();
    PreparedStatement ps = null;
    Connection conexion = null;
    try {
      conexion = ConfigSQLite.abrirConexion();
      String queryString =
        "SELECT distinct * FROM socio left JOIN prestamo ON (socio.codigo = prestamo.codigo_socio) WHERE prestamo.codigo_socio is null";
      ps = conexion.prepareStatement(queryString);

      ResultSet resultados = ps.executeQuery();

      while (resultados.next()) {
        int codigoSocio = resultados.getInt("codigo");
        String dniSocio = resultados.getString("dni");
        String nombreSocio = resultados.getString("nombre");
        String domicilioSocio = resultados.getString("domicilio");
        String telefonoSocio = resultados.getString("telefono");
        String correoSocio = resultados.getString("correo");

        Socio socio = new Socio(codigoSocio, dniSocio, nombreSocio, domicilioSocio, telefonoSocio, correoSocio);
        todosSociosNP.add(socio);
      }
      if (todosSociosNP.isEmpty()) {
        throw new SocioException(SocioException.ERROR_SOCIO_NOPRESTAMO);
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }
    return todosSociosNP;
  }

  /**
   * Metodo que consulta los socios con prestamos segun una fecha
   * @param fecha
   * @return Arraylist de Socios correspondientes
   * @throws BDException
   * @throws SocioException
   */
  public static ArrayList<Socio> consultarSociosPrestatario(String fecha) throws BDException, SocioException {
    ArrayList<Socio> todosSocioSP = new ArrayList<Socio>();
    PreparedStatement ps = null;
    Connection conexion = null;
    try {
      conexion = ConfigSQLite.abrirConexion();
      String queryString =
        "SELECT * FROM socio JOIN prestamo ON (socio.codigo = prestamo.codigo_socio) WHERE prestamo.fecha_inicio = ?;";
      ps = conexion.prepareStatement(queryString);
      ps.setString(1, fecha);
      ResultSet resultados = ps.executeQuery();

      while (resultados.next()) {
        int codigoSocio = resultados.getInt("codigo");
        String dniSocio = resultados.getString("dni");
        String nombreSocio = resultados.getString("nombre");
        String domicilioSocio = resultados.getString("domicilio");
        String telefonoSocio = resultados.getString("telefono");
        String correoSocio = resultados.getString("correo");

        Socio socio = new Socio(codigoSocio, dniSocio, nombreSocio, domicilioSocio, telefonoSocio, correoSocio);
        todosSocioSP.add(socio);
      }
      if (todosSocioSP.isEmpty()) {
        throw new SocioException(SocioException.ERROR_SOCIO_NOPRESTAMO_FECHA);
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }
    return todosSocioSP;
  }

  /**
   * Metodo que consulta Socios de la BD segun una localidad
   * @param localidad
   * @return ArrayList de los Socios correspondientes
   * @throws BDException
   * @throws SocioException
   */
  public static ArrayList<Socio> consultarSociosPorLocalidadOrdenadosPorNombre(String localidad) throws BDException {
    PreparedStatement ps = null;
    Connection conexion = null;
    ArrayList<Socio> socios = new ArrayList<>();

    try {
      conexion = ConfigSQLite.abrirConexion();
      String query =
        "SELECT * FROM socio " +
        "WHERE lower(domicilio) LIKE lower(?) " +
        "AND lower(domicilio) NOT LIKE lower(?) " +
        "AND lower(domicilio) NOT LIKE lower(?) " +
        "ORDER BY nombre";

      ps = conexion.prepareStatement(query);

      ps.setString(1, "%" + localidad + "%");
      ps.setString(2, "c/ " + localidad + "%");
      ps.setString(3, "calle " + localidad + "%");

      ResultSet resultados = ps.executeQuery();

      while (resultados.next()) {
        socios.add(
          new Socio(
            resultados.getInt("codigo"),
            resultados.getString("dni"),
            resultados.getString("nombre"),
            resultados.getString("domicilio"),
            resultados.getString("telefono"),
            resultados.getString("correo")
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
    return socios;
  }

  /**
   * metodo hecho por juan para un metodo de prestamo. No poner en el main si no se necesita
   * @param codigoSocio
   * @return
   * @throws BDException
   */
  public static Socio consultarSocioPorCodigo(int codigoSocio) throws BDException {
    Socio socio = null;

    Connection conexion = null;
    try {
      conexion = ConfigSQLite.abrirConexion();

      String query = "select * from socio where codigo = ?";
      PreparedStatement ps = conexion.prepareStatement(query);
      ps.setInt(1, codigoSocio);

      ResultSet resultados = ps.executeQuery();

      if (resultados.next()) {
        socio = new Socio(
          codigoSocio,
          resultados.getString("dni"),
          resultados.getString("nombre"),
          resultados.getString("domicilio"),
          resultados.getString("telefono"),
          resultados.getString("correo")
        );
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }

    return socio;
  }

  public static LinkedHashMap<Socio, Integer> consultarSociosMayorPrestamos() throws BDException {
    PreparedStatement ps = null;
    Connection conexion = null;
    LinkedHashMap<Socio, Integer> socios = new LinkedHashMap<Socio, Integer>();

    try {
      conexion = ConfigSQLite.abrirConexion();
      String query =
        "SELECT socio.*, total_prestamos FROM socio JOIN (" +
        "SELECT codigo_socio,COUNT(*) as total_prestamos FROM prestamo GROUP BY codigo_socio HAVING COUNT(*) = " +
        "(SELECT MAX(total_prestamos) FROM (" +
        "SELECT COUNT(*) AS total_prestamos FROM prestamo GROUP BY codigo_socio))" +
        ") prestamo ON socio.codigo = prestamo.codigo_socio";

      ps = conexion.prepareStatement(query);
      ResultSet resultados = ps.executeQuery();

      while (resultados.next()) {
        socios.put(
          new Socio(
            resultados.getInt("codigo"),
            resultados.getString("dni"),
            resultados.getString("nombre"),
            resultados.getString("domicilio"),
            resultados.getString("telefono"),
            resultados.getString("correo")
          ),
          resultados.getInt("total_prestamos")
        );
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }
    return socios;
  }

  public static LinkedHashMap<Socio, Integer> consultarSociosMayorMedia() throws BDException {
    PreparedStatement ps = null;
    Connection conexion = null;
    LinkedHashMap<Socio, Integer> socios = new LinkedHashMap<Socio, Integer>();

    try {
      conexion = ConfigSQLite.abrirConexion();
      String query =
        "SELECT socio.*, total_prestamos FROM socio JOIN (" +
        "SELECT codigo_socio,COUNT(*) as total_prestamos FROM prestamo GROUP BY codigo_socio HAVING COUNT(*) > " +
        "(SELECT avg(cantidad) FROM (" +
        "SELECT COUNT(*) AS cantidad FROM prestamo GROUP BY codigo_socio))" +
        ") prestamo ON socio.codigo = prestamo.codigo_socio";

      ps = conexion.prepareStatement(query);
      ResultSet resultados = ps.executeQuery();

      while (resultados.next()) {
        socios.put(
          new Socio(
            resultados.getInt("codigo"),
            resultados.getString("dni"),
            resultados.getString("nombre"),
            resultados.getString("domicilio"),
            resultados.getString("telefono"),
            resultados.getString("correo")
          ),
          resultados.getInt("total_prestamos")
        );
      }
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_QUERY + e.getMessage());
    } finally {
      if (conexion != null) {
        ConfigSQLite.cerrarConexion(conexion);
      }
    }
    return socios;
  }
}
