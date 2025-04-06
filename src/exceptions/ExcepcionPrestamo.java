package exceptions;

public class ExcepcionPrestamo extends Exception {
	public final static String ESTA_PRESTADO = "Se ha prestado ese libro a un socio y éste aún no lo ha devuelto.";
	public final static String TIENE_PRESTADO = "Se ha prestado un libro a ese socio y éste aún no lo ha devuelto.";
	public final static String NO_EXISTE_LIBRO_SOCIO = "No existen libros o socios con esos datos identificativos.";
	public static final String FECHA_MENOR_HOY = "La fecha de fin no puede ser anterior a hoy.";
	public static final String YA_PRESTAMO_HOY = "Ese socio ya ha realizado un préstamo hoy.";
	
    public ExcepcionPrestamo(String mensaje) {
        super("Error: " + mensaje);
    }

}