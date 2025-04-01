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

	// Insertar Libro dentro de la base de datos
	public static boolean anadirLibro(String isbn, String titulo, String escritor, int anyo_publicacion,
			float puntuacion) throws BDException {

		PreparedStatement ps = null;
		Connection conexion = null;

		int filas = 0;

		try {
			// Conexi�n a la bd
			conexion = ConfigSQLite.abrirConexion();
			String query = "insert into libro (isbn, titulo, escritor, anyo_publicacion, puntuacion ) VALUES (?, ?, ?, ?, ?);";

			ps = conexion.prepareStatement(query);

			ps.setString(1, isbn);
			ps.setString(2, titulo);
			ps.setString(3, escritor);
			ps.setInt(4, anyo_publicacion);
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

	// Eliminar un Libro, por código, de la base de datos
	public static boolean borrarLibroPorCodigo(int codigo) throws BDException {

		PreparedStatement ps = null;
		Connection conexion = null;

		int filas = 0;

		try {
			// Conexi�n a la bd
			conexion = ConfigSQLite.abrirConexion();
			String query = "delete from libro where codigo = ?;";

			ps = conexion.prepareStatement(query);

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

	// Consultar todos los libros de la base de datos
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
				int anyo_publicacion = resultados.getInt("anyo_publicacion");
				float puntuacion = resultados.getFloat("puntuacion");

				Libro libro = new Libro(codigo, isbn, titulo, escritor, anyo_publicacion, puntuacion);

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

	// Consultar libros, por escritor, ordenados por puntuación descendente
	public static ArrayList<Libro> consultarLibrosOrdenados(String escritor) throws BDException {
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();

		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			// Conexi�n a la bd
			conexion = ConfigSQLite.abrirConexion();
			String query = "select * from libro where lower(escritor) like '%?%' order by puntuacion desc;";

			ps = conexion.prepareStatement(query);
			ps.setString(1, escritor);

			ResultSet resultados = ps.executeQuery();

			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String isbn = resultados.getString("isbn");
				String titulo = resultados.getString("titulo");
				String escritor_libro = resultados.getString("escritor");
				int anyo_publicacion = resultados.getInt("anyo_publicacion");
				float puntuacion = resultados.getFloat("puntuacion");

				Libro libro = new Libro(codigo, isbn, titulo, escritor_libro, anyo_publicacion, puntuacion);

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

	//Consultar libros no prestados
	public static ArrayList<Libro> consultarLibrosNoPrestados() throws BDException {
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();

		PreparedStatement ps = null;
		Connection conexion = null;

		try {
			// Conexi�n a la bd
			conexion = ConfigSQLite.abrirConexion();
			String query = "select l.codigo, l.isbn, l.titulo, l.escritor, l.anyo_publicacion, l.puntuacion from libro l left join prestamo p on l.codigo = p.codigo_libro where p.codigo_libro is null;";

			ps = conexion.prepareStatement(query);
			

			ResultSet resultados = ps.executeQuery();

			while (resultados.next()) {
				int codigo = resultados.getInt("codigo");
				String isbn = resultados.getString("isbn");
				String titulo = resultados.getString("titulo");
				String escritor_libro = resultados.getString("escritor");
				int anyo_publicacion = resultados.getInt("anyo_publicacion");
				float puntuacion = resultados.getFloat("puntuacion");

				Libro libro = new Libro(codigo, isbn, titulo, escritor_libro, anyo_publicacion, puntuacion);

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
	
	//Consultar libros devueltos en una fecha
		public static ArrayList<Libro> consultarLibrosDevueltos(String fecha_devolucion) throws BDException {
			ArrayList<Libro> listaLibros = new ArrayList<Libro>();

			PreparedStatement ps = null;
			Connection conexion = null;

			try {
				// Conexi�n a la bd
				conexion = ConfigSQLite.abrirConexion();
				String query = "select l.codigo, l.isbn, l.titulo, l.escritor, l.anyo_publicacion, l.puntuacion from libro l left join prestamo p on l.codigo = p.codigo_libro where p.fecha_devolucion like '?';";

				ps = conexion.prepareStatement(query);
				

				ResultSet resultados = ps.executeQuery();

				while (resultados.next()) {
					int codigo = resultados.getInt("codigo");
					String isbn = resultados.getString("isbn");
					String titulo = resultados.getString("titulo");
					String escritor_libro = resultados.getString("escritor");
					int anyo_publicacion = resultados.getInt("anyo_publicacion");
					float puntuacion = resultados.getFloat("puntuacion");

					Libro libro = new Libro(codigo, isbn, titulo, escritor_libro, anyo_publicacion, puntuacion);

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
