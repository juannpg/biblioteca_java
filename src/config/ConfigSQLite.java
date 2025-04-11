package config;

import exceptions.BDException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.sqlite.SQLiteConfig;

public class ConfigSQLite {

  private static final String DRIVER = "org.sqlite.JDBC";
  private static final String URLBD = "jdbc:sqlite:bd/biblioteca.db";

  public static Connection abrirConexion() throws BDException {
    Connection conexion = null;

    try {
      Class.forName(DRIVER);
      SQLiteConfig config = new SQLiteConfig();
      config.enforceForeignKeys(true);
      conexion = DriverManager.getConnection(URLBD, config.toProperties());
    } catch (ClassNotFoundException e) {
      throw new BDException(BDException.ERROR_CARGAR_DRIVER + e.getMessage());
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_ABRIR_CONEXION + e.getMessage());
    }

    return conexion;
  }

  public static void cerrarConexion(Connection conexion) throws BDException {
    try {
      conexion.close();
    } catch (SQLException e) {
      throw new BDException(BDException.ERROR_CERRAR_CONEXION + e.getMessage());
    }
  }
}
