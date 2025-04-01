package exceptions;

public class ExcepcionPrestamo extends Exception {
	public final static String ESTA_PRESTADO = "Se ha prestado ese libro a un socio y éste aún no lo ha devuelto.";
	public final static String TIENE_PRESTADO = "Se ha prestado un libro a ese socio y éste aún no lo ha devuelto.";
	
    public ExcepcionPrestamo(String mensaje) {
        super("Error: " + mensaje);
    }

}