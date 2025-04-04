package dao;

import java.nio.channels.SelectableChannel;
import java.security.PublicKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.SQLiteConfig;

import config.ConfigSQLite;
import entrada.Teclado;
import exceptions.BDException;
import exceptions.SocioException;
import models.Socio;

public class AccesoSocio {
	
	/**
	 * Metodo que permite agregar un Socio
	 * @param socio
	 * @return booleano indicando si se ha agregado o no se ha agregado
	 * @throws BDException
	 */
	public static boolean agregarSocio(Socio socio) throws BDException {
	    PreparedStatement ps = null;
	    Connection conexion = null;
	    int resultados = 0;
	    try {
	        // Conexión a la base de datos
	        conexion = ConfigSQLite.abrirConexion();
	        String query = "INSERT INTO socio (dni, nombre, domicilio, telefono, correo) "
	                     + "VALUES ( ?, ?, ?, ?)";

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
	        String query = "DELETE FROM socio WHERE codigo=?";

	        ps = conexion.prepareStatement(query);
	        ps.setInt(1,codigoSocio);

	        resultados = ps.executeUpdate();
	        
	        if (resultados == 0) {
	        	throw new SocioException(SocioException.ERROR_NOSOCIO);
	        }
	        if (esPrestatario(codigoSocio)) {
	        	throw new SocioException(SocioException.ERROR_SOCIO_PRESTAMO);
	        }
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
	public static ArrayList<Socio> consultarTodosSocios() throws BDException, SocioException{
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
	    }catch (SQLException e) {
	        throw new BDException(BDException.ERROR_QUERY + e.getMessage());
	    } finally {
	        if (conexion != null) {
	            ConfigSQLite.cerrarConexion(conexion);
	        }
	    }
	    return todosSocios;
	}

	/**
	 * Metodo para consultar los Socios que no han hecho prestamo
	 * @return ArrayList de los Socios correspondientes
	 * @throws BDException
	 * @throws SocioException
	 */
	public static ArrayList<Socio> consultarSociosNoPrestatario() throws BDException, SocioException{
		ArrayList<Socio> todosSociosNP = new ArrayList<Socio>();
	    PreparedStatement ps = null;
	    Connection conexion = null;
	    try {
	    	conexion = ConfigSQLite.abrirConexion();
	    	 String queryString = "SELECT * FROM socio JOIN prestamo ON (socio.codigo = prestamo.codigo_socio) WHERE prestamo.fecha_devolucion IS NULL;";
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
	    }catch (SQLException e) {
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
	public static ArrayList<Socio> consultarSociosPrestatario(String fecha) throws BDException, SocioException{
		ArrayList<Socio> todosSocioSP = new ArrayList<Socio>();
	    PreparedStatement ps = null;
	    Connection conexion = null;
	    try {
	    	conexion = ConfigSQLite.abrirConexion();
	    	 String queryString = "SELECT * FROM socio JOIN prestamo ON (socio.codigo = prestamo.codigo_socio) WHERE prestamo.fecha_inicio = ?;";
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
	    }catch (SQLException e) {
	        throw new BDException(BDException.ERROR_QUERY + e.getMessage());
	    } finally {
	        if (conexion != null) {
	            ConfigSQLite.cerrarConexion(conexion);
	        }
	    }
	    return todosSocioSP;
	}
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
	    int resultados = 0;
	    try {
	        // Conexión a la base de datos
	        conexion = ConfigSQLite.abrirConexion();
	        String query = "SELECT * FROM socio JOIN prestamo ON (socio.codigo = prestamos.codigo_socio) WHERE socio.codigo = ?";

	        ps = conexion.prepareStatement(query);
	        ps.setInt(1,codigoSocio);

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
	       String query = "SELECT * FROM socio "
                    + "WHERE lower(domicilio) LIKE lower(?) "
                    + "AND lower(domicilio) LIKE lower(?) "
                    + "ORDER BY nombre";
	       
	       ps = conexion.prepareStatement(query);
	
	       ps.setString(1, "%," + localidad + "%");
        ps.setString(2, "%, " + localidad + "%");

	        ResultSet resultados = ps.executeQuery();
	        
	        while (resultados.next()) {
	        	socios.add(new Socio(
        			resultados.getInt("codigo"),
	        		resultados.getString("dni"),
	        		resultados.getString("nombre"),
	        		resultados.getString("domicilio"),
	        		resultados.getString("telefono"),
	        		resultados.getString("correo")
    			));
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
	public static void main(String[] args) {
		try {
			eliminarSocio(12);
		} catch (BDException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocioException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

