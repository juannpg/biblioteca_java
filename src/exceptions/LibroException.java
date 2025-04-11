package exceptions;

public class LibroException extends Exception {

  public static final String ERROR_ABRIR_CONEXION = "Error al abrir conexion ";
  public static final String ERROR_QUERY = "Error en la consulta ";
  public static final String ERROR_CERRAR_CONEXION = "Error al cerrar conexion ";
  public static final String ERROR_CARGAR_DRIVER = "Error al cargar driver";
  public static final String ERROR_CANCELAR_SETCONTRASENA = "Se canceló el inicio de sesión";
  public static final String ERROR_NO_EXISTE = "No existe la tupla buscada";
  public static final String ERROR_NOLIBRO = "No existe ningún libro con ese código en la base de datos.";
  public static final String ERROR_LIBRO_ISBNEXISTE = "Ya existe un libro con ese ISBN en la base de datos.";
  public static final String ERROR_LIBRO_BDEmpty = "No se ha encontrado ningún libro en la base de datos.";
  public static final String ERROR_LIBRO_PRESTAMO = "El libro está referenciado en un préstamo de la base de datos.";
  public static final String ERROR_LIBRO_NOESCRITOR = "No existe ningún libro con ese escritor en la base de datos.";
  public static final String ERROR_LIBRO_NOPRESTADO = "No existe ningún libro no prestado en la base de datos.";
  public static final String ERROR_LIBRO_NODEVUELTO =
    "No existe ningún libro devuelto en esa fecha en la base de datos.";

  public LibroException(String mensaje) {
    super("Error: " + mensaje);
  }
}
