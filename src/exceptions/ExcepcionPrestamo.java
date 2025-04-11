package exceptions;

public class ExcepcionPrestamo extends Exception {

  public static final String ESTA_PRESTADO = "Se ha prestado ese libro a un socio y éste aún no lo ha devuelto.";
  public static final String TIENE_PRESTADO = "Se ha prestado un libro a ese socio y éste aún no lo ha devuelto.";
  public static final String NO_EXISTE_LIBRO_SOCIO = "No existen libros o socios con esos datos identificativos.";
  public static final String FECHA_MENOR_HOY = "La fecha de fin no puede ser anterior a hoy.";
  public static final String YA_PRESTAMO_HOY = "Ese socio ya ha realizado un préstamo hoy.";
  public static final String NO_EXISTE_PRESTAMO = "No existe un préstamo con esos datos identificativos.";
  public static final String FECHA_MENOR_INICIO = "La fecha de fin no puede ser menor a la de inicio.";

  public ExcepcionPrestamo(String mensaje) {
    super("Error: " + mensaje);
  }
}
