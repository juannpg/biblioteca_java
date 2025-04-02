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
import models.Socio;

public class AccesoSocio {
	// 1.INSERTAR SOCIO
	public static boolean añadirSocio(Socio socio) throws BDException {
		PreparedStatement ps = null;
		Connection conexion = null;
		int resultados = 0;
		try {
			// Conexión a la base de datos
			conexion = ConfigSQLite.abrirConexion();
			String query = "INSERT INTO socio (dni, nombre, domicilio, telefono, correo) " + "VALUES ( ?, ?, ?, ?)";

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

	// 2. ELIMINAR SOCIO POR CODIGO
	public static boolean eliminarSocio(int codigoSocio) throws BDException {
		PreparedStatement ps = null;
		Connection conexion = null;
		int resultados = 0;
		try {
			// Conexión a la base de datos
			conexion = ConfigSQLite.abrirConexion();
			String query = "DELETE FROM socio WHERE codigo=?";

			ps = conexion.prepareStatement(query);
			ps.setInt(1, codigoSocio);

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

	// 3. CONSULTAR TODOS LOS SOCIOS
	public static ArrayList<Socio> consultarTodosSocios() throws BDException {
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
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
		return todosSocios;
	}

	// 4. CONSULTAR SOCIO POR LOCALIDAD(parametro) ORDENADOS POR NOMBRE ASC
	public static ArrayList<Socio> consultarSociosLocalidad(String localidad) throws BDException {
		ArrayList<Socio> todosSociosLocalidad = new ArrayList<Socio>();
		PreparedStatement ps = null;
		Connection conexion = null;
		try {
			conexion = ConfigSQLite.abrirConexion();
			String queryString = "SELECT * FROM socio WHERE localidad = ?";
			ps = conexion.prepareStatement(queryString);
			ps.setString(1, localidad);

			ResultSet resultados = ps.executeQuery();

			while (resultados.next()) {
				int codigoSocio = resultados.getInt("codigo");
				String dniSocio = resultados.getString("dni");
				String nombreSocio = resultados.getString("nombre");
				String domicilioSocio = resultados.getString("domicilio");
				String telefonoSocio = resultados.getString("telefono");
				String correoSocio = resultados.getString("correo");

				Socio socio = new Socio(codigoSocio, dniSocio, nombreSocio, domicilioSocio, telefonoSocio, correoSocio);
				todosSociosLocalidad.add(socio);

			}
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
		return todosSociosLocalidad;
	}
	// 5. CONSULTAR SOCIOS SIN PRESTAMOS
	// public static ArrayList<Socio> consultarSociosNoPrestatario(){

}
// 6. CONSULTAR SOCIOS CON PRESTAMOS

/*
 * public static void main(String[] args) { try { String localidadString =
 * Teclado.leerCadena("Localidad?"); ArrayList<Socio> arraySocios =
 * consultarSociosLocalidad(localidadString); if (arraySocios.isEmpty()) {
 * System.out.println("La lista de socios esta vacia"); }else { for (Socio socio
 * : arraySocios) { System.out.println(socio.toString()); } }
 * 
 * } catch (BDException e) { // TODO Auto-generated catch block
 * e.printStackTrace(); } }
 */
