package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import config.ConfigSQLite;

import exceptions.BDException;
import exceptions.LibroException;
import models.Libro;

public class AccesoLibro {

	/**
	 * Insertar un ibro dado un isbn, título, escritor, año de publicación y
	 * puntuación del libro.
	 * 
	 * @param isbn
	 * @param titulo
	 * @param escritor
	 * @param anyo_publicacion
	 * @param puntuacion
	 * @return si se ha insertado o no
	 * @throws BDException si la consulta sale mal
	 * @author xiomara ratto
	 */
	public static boolean anadirLibro(String isbn, String titulo, String escritor, int anyo_publicacion,
			float puntuacion) throws BDException {

		PreparedStatement ps = null;
		Connection conexion = null;

		int filas = 0;

		try {

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
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}

		return filas == 1;

	}

	/**
	 * Consultar si un libro está referenciado a un prestámo o no
	 * 
	 * @param codigo
	 * @return
	 * @throws BDException si la consulta sale mal
	 * @author xiomara ratto
	 */
	private static boolean esPrestatario(int codigo) throws BDException {
		PreparedStatement ps = null;
		Connection conexion = null;
		int resultados = 0;
		try {
			// Conexión a la base de datos
			conexion = ConfigSQLite.abrirConexion();
			String query = "SELECT * FROM libro JOIN prestamo ON (libro.codigo = prestamo.codigo_libro) WHERE libro.codigo = ?;";

			ps = conexion.prepareStatement(query);
			ps.setInt(1, codigo);

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
	 * Eliminar un libro de la base de datos dado un código como parámetro.
	 * 
	 * @param codigo
	 * @return si se elimina o no
	 * @throws BDException    si la consulta sale mal
	 * @throws LibroException si no existe libro con ese código
	 * @throws LibroException si ese libro está referenciado en un préstamo
	 * @autor xiomara ratto
	 */
	public static boolean borrarLibroPorCodigo(int codigo) throws BDException, LibroException {

		PreparedStatement ps = null;
		Connection conexion = null;

		int filas = 0;

		try {

			conexion = ConfigSQLite.abrirConexion();
			String query = "delete from libro where codigo = ?;";

			ps = conexion.prepareStatement(query);

			ps.setInt(1, codigo);

			filas = ps.executeUpdate();

			if (filas == 0) {
				throw new LibroException(LibroException.ERROR_NOLIBRO);
			}
			if (esPrestatario(codigo)) {
				throw new LibroException(LibroException.ERROR_LIBRO_PRESTAMO);
			}

		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}

		return filas == 1;

	}

	/**
	 * Consultar todos los libros de la base de datos
	 * 
	 * @return todos los libros consultados
	 * @throws BDException    si la consulta sale mal
	 * @throws LibroException si no se encuentra ningún libro
	 * @author xiomara ratto
	 */
	public static ArrayList<Libro> consultarLibros() throws BDException, LibroException {
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();

		PreparedStatement ps = null;
		Connection conexion = null;

		try {

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

				if (listaLibros.isEmpty()) {
					throw new LibroException(LibroException.ERROR_LIBRO_BDEmpty);
				}
			}
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
		return listaLibros;

	}

	/**
	 * Consultar libros dado un escritor como parámetro, ordenados por puntuación
	 * descendente
	 * 
	 * @param escritor
	 * @return todos los libros consultados con ese escritor
	 * @throws BDException    si la consulta sale mal
	 * @throws LibroException si no hay ningún libro en la base de datos con ese
	 *                        escritor.
	 * @author xiomara ratto
	 */
	public static ArrayList<Libro> consultarLibrosOrdenados(String escritor) throws BDException, LibroException {
		escritor = escritor.toLowerCase();

		ArrayList<Libro> listaLibros = new ArrayList<Libro>();

		PreparedStatement ps = null;
		Connection conexion = null;

		try {

			conexion = ConfigSQLite.abrirConexion();
			String query = "select * from libro where lower(escritor) like ? order by puntuacion desc;";

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

				if (listaLibros.isEmpty()) {
					throw new LibroException(LibroException.ERROR_LIBRO_NOESCRITOR);
				}
			}
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
		return listaLibros;

	}

	/**
	 * Consultar libros no prestados
	 * 
	 * @return todos los libros que no hayan sido prestados
	 * @throws BDException    si la consulta sale mal
	 * @throws LibroException si no hay ningún libro que no se haya prestado
	 * @author xiomara ratto
	 */
	public static ArrayList<Libro> consultarLibrosNoPrestados() throws BDException, LibroException {
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();

		PreparedStatement ps = null;
		Connection conexion = null;

		try {

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

				if (listaLibros.isEmpty()) {
					throw new LibroException(LibroException.ERROR_LIBRO_NOPRESTADO);
				}
			}
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
		return listaLibros;

	}

	/**
	 * Consultar libros devueltos en una fecha pasada como parámetro
	 * 
	 * @param fecha_devolucion
	 * @return los libros devueltos en esa fecha
	 * @throws BDException    si la consulta sale mal
	 * @throws LibroException si no hay ningún libro devuelto en esa fecha
	 * @author xiomara ratto
	 */
	public static ArrayList<Libro> consultarLibrosDevueltos(String fecha_devolucion)
			throws BDException, LibroException {
		ArrayList<Libro> listaLibros = new ArrayList<Libro>();

		PreparedStatement ps = null;
		Connection conexion = null;

		try {

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

				if (listaLibros.isEmpty()) {
					throw new LibroException(LibroException.ERROR_LIBRO_NODEVUELTO);
				}
			}
		} catch (SQLException e) {
			throw new BDException(BDException.ERROR_QUERY + e.getMessage());
		} finally {
			if (conexion != null) {
				ConfigSQLite.cerrarConexion(conexion);
			}
		}
		return listaLibros;

	}
}
