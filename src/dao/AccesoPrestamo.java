package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.ConfigSQLite;
import exceptions.BDException;
import exceptions.ExcepcionPrestamo;
import models.Prestamo;

public class AccesoPrestamo {
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
	
	public static boolean insertarPrestamo(int codigoLibro, int codigoSocio, String fechaInicio, String fechaFin) throws BDException, ExcepcionPrestamo {
		Connection conexion = null;
		int filasAfectadas = 0;
		
        try {
            conexion = ConfigSQLite.abrirConexion();

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
            
            // lo demás
            String queryInsert = "insert into prestamo (codigo_libro, codigo_socio, fecha_inicio, fecha_fin)"
            + " values (?, ?, ? ,?)";
            PreparedStatement psInsert = conexion.prepareStatement(queryInsert);
            
            psInsert.setInt(1, codigoLibro);
            psInsert.setInt(2, codigoSocio);
            psInsert.setString(3,  fechaInicio);
            psInsert.setString(4, fechaFin);
            
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
	
	public static boolean actualizarPrestamo(int codigoLibro, int codigoSocio, String fechaInicio, String fechaFin) throws BDException {
		Connection conexion = null;
		int filasAfectadas = 0;
		
        try {
            conexion = ConfigSQLite.abrirConexion();
            
            String queryUpdate = "update prestamo "
            + "set fecha_inicio = ?, fecha_fin = ? "
            + "where codigo_libro = ? and codigo_socio = ?";
            PreparedStatement psInsert = conexion.prepareStatement(queryUpdate);
            
            psInsert.setString(1,  fechaInicio);
            psInsert.setString(2, fechaFin);
            psInsert.setInt(3, codigoLibro);
            psInsert.setInt(4, codigoSocio);
            
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
}

