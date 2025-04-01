package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import config.ConfigSQLite;

import exceptions.BDException;
import models.Libro;

public class AccesoLibro {

	public static boolean añadirLibro(String isbn, String titulo, String escritor, int anio_publicacion,
			float puntuacion) throws BDException {

		PreparedStatement ps = null;
		Connection conexion = null;

		int filas = 0;

		try {
			// Conexi�n a la bd
			conexion = ConfigSQLite.abrirConexion();
			String query = "insert into empleado (isbn, titulo, escritor, anio_publicacion, puntuacion ) VALUES (?, ?, ?, ?, ?);";

			ps = conexion.prepareStatement(query);
			// Al primer interrogante le asigno (ps1)
			ps.setString(1, isbn);
			ps.setString(2, titulo);
			ps.setString(3, escritor);
			ps.setInt(4, anio_publicacion);
			ps.setFloat(5, puntuacion);

			filas = ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}

		return filas == 1;

	}

	public static boolean borrarLibroPorCodigo(int codigo) throws BDException {

		PreparedStatement ps = null;
		Connection conexion = null;

		int filas = 0;

		try {
			// Conexi�n a la bd
			conexion = ConfigSQLite.abrirConexion();
			String query = "delete from empleado where codigo = ?;";

			ps = conexion.prepareStatement(query);
			// Al primer interrogante le asigno  (ps1)
			ps.setInt(1, codigo);

			filas = ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}

		return filas == 1;

	}

	public static ArrayList<Libro> consultarLibros() throws BDException {
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();

		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			// Conexi�n a la bd
			conexion = ConfigSQLite.abrirConexion();
			String query = "select * from libro;";

			ps = conexion.prepareStatement(query);

			ResultSet resultados = ps.executeQuery();

			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String isbn = resultados.getString("isbn");
				String titulo = resultados.getString("titulo");
				String escritor = resultados.getString("escritor");
				int anio_publicacion = resultados.getInt("anio_publicacion");
				float puntuacion = resultados.getFloat("puntuacion");

				Libro libro = new Libro(codigo, isbn, titulo, escritor, anio_publicacion, puntuacion);

				listaLibros.add(libro);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
		return listaLibros;

	}
	
	public static ArrayList<Libro> consultarLibrosOrdenados() throws BDException {
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();

		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			// Conexi�n a la bd
			conexion = ConfigSQLite.abrirConexion();
			String query = "select * from libro order by puntuacion desc;";

			ps = conexion.prepareStatement(query);

			ResultSet resultados = ps.executeQuery();

			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String isbn = resultados.getString("isbn");
				String titulo = resultados.getString("titulo");
				String escritor = resultados.getString("escritor");
				int anio_publicacion = resultados.getInt("anio_publicacion");
				float puntuacion = resultados.getFloat("puntuacion");

				Libro libro = new Libro(codigo, isbn, titulo, escritor, anio_publicacion, puntuacion);

				listaLibros.add(libro);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
		return listaLibros;

	}
	
	

}
