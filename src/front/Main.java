package front;

import entrada.Teclado;

public class Main {

	public static void escribirMenuOpcionesLibro() {
		System.out.println();
		System.out.println("Elige una opción del menu de Libro");
		System.out.println("0) Salir del programa.");
		System.out.println("1) Insertar un libro en la base de datos");
		System.out.println("2) Eliminar un libro, por código, de la base de datos");
		System.out.println("3) Consultar todos los libros de la base de datos.");
		System.out.println("4) Consultar varios libros, por escritor, de la base de datos, ordenados por puntuación decendente.");
		System.out.println("5) Consultar los libros no prestados de la base de datos.");
		System.out.println("6) Consultar los libros devueltos, en una fecha, de la base de datos.");

	}

	public static void escribirMenuOpcionesSocio() {
		System.out.println();
		System.out.println("Elige una opción del menu de Socio");

		System.out.println("0) Salir del programa.");
		System.out.println("1) Insertar un socio en la base de datos");
		System.out.println("2) Eliminar un socio, por código, de la base de datos");
		System.out.println("3) Consultar todos los socios de la base de datos.");
		System.out.println("4) Consultar varios socios, por localidad, de la base de datos, ordenados por nombre ascendiente.");
		System.out.println("5) Consultar los socios sin prestamos de la base de datos.");
		System.out.println("6) Consultar los socios con prestamos, en una fecha, de la base de datos.");

	}

	public static void escribirMenuOpcionesPrestamo() {
		System.out.println();
		System.out.println("Elige una opción del menu de Prestamo");
		System.out.println("0) Salir del programa.");
		System.out.println("1) Insertar un prestamo en la base de datos");
		System.out.println("2) Actualizar un préstamo, por datos identificativos, de la base de datos");
		System.out.println("3) Eliminar un préstamo, por datos identificativos, de la base de datos.");
		System.out.println("4) Consultar todos los préstamos de la base de datos.");
		System.out.println("5) Consultar los préstamos no devueltos de la base de datos.");
		System.out.println("6) Consultar DNI y nombre de socio, ISBN y título de libro y fecha de devolución de los\n"
				+ "préstamos realizados en una fecha de la base de datos.");

	}

	public static void menuLibros() {
		int opcion;
		do {
			escribirMenuOpcionesLibro();
			opcion = Teclado.leerEntero("Opción: ");

			switch (opcion) {
			case 1:
				System.out.println("Insertar libro...");
		        // Pedir datos al usuario
		        String isbn = Teclado.leerCadena("Introduce el ISBN: ");
		        String titulo = Teclado.leerCadena("Introduce el título: ");
		        String escritor = Teclado.leerCadena("Introduce el escritor: ");
		        int anyoPublicacion = Teclado.leerEntero("Introduce el año de publicación: ");
		        float puntuacion = (float) Teclado.leerReal("Introduce la puntuación: ");
		        
		        try {
		            boolean resultado = anadirLibro(isbn, titulo, escritor, anyoPublicacion, puntuacion);

		            if (resultado) {
		                System.out.println("Libro añadido correctamente.");
		            } else {
		                System.out.println("No se pudo añadir el libro.");
		            }
		        } catch (BDException e) {
		            System.out.println("Error al añadir el libro: " + e.getMessage());
		        }

		        
		        
				break;
			case 2:
				System.out.println("Eliminar libro...");
				break;
			case 3:
				System.out.println("Consultar todos los libros...");
				break;
			case 4:
				System.out.println("Consultar libros por escritor...");
				break;
			case 5:
				System.out.println("Consultar libros no prestados...");
				break;
			case 6:
				System.out.println("Consultar libros devueltos en una fecha...");
				break;
			case 0:
				System.out.println("Regresando al menú principal...");
				break;
			default:
				System.out.println("Opción no válida. Intente de nuevo.");
			}
		} while (opcion != 0);
	}

	public static void menuSocios() {
		int opcion;
		do {
			escribirMenuOpcionesSocio();
			opcion = Teclado.leerEntero("Opción: ");

			switch (opcion) {
			case 1:
				System.out.println("Insertar socio...");
				break;
			case 2:
				System.out.println("Eliminar socio...");
				break;
			case 3:
				System.out.println("Consultar todos los socios...");
				break;
			case 4:
				System.out.println("Consultar socios por localidad...");
				break;
			case 5:
				System.out.println("Consultar socios sin préstamos...");
				break;
			case 6:
				System.out.println("Consultar socios con préstamos en una fecha...");
				break;
			case 0:
				System.out.println("Regresando al menú principal...");
				break;
			default:
				System.out.println("Opción no válida. Intente de nuevo.");
			}
		} while (opcion != 0);
	}

	public static void menuPrestamos() {
		int opcion;
		do {
			escribirMenuOpcionesPrestamo();
			opcion = Teclado.leerEntero("Opción: ");
			

			switch (opcion) {
			case 1:
				System.out.println("Insertar préstamo...");
				break;
			case 2:
				System.out.println("Actualizar préstamo...");
				break;
			case 3:
				System.out.println("Eliminar préstamo...");
				break;
			case 4:
				System.out.println("Consultar todos los préstamos...");
				break;
			case 5:
				System.out.println("Consultar préstamos no devueltos...");
				break;
			case 6:
				System.out.println("Consultar préstamos realizados en una fecha...");
				break;
			case 0:
				System.out.println("Regresando al menú principal...");
				break;
			default:
				System.out.println("Opción no válida. Intente de nuevo.");
			}
		} while (opcion != 0);
	}

	public static void main(String[] args) {

		int opcion;

		do {
			System.out.println();
			System.out.println("\nSeleccione una opción:");
			System.out.println("1) Menú Libros");
			System.out.println("2) Menú Socios");
			System.out.println("3) Menú Préstamos");
			System.out.println("0) Salir");

			System.out.print("Opción: ");
			opcion = Teclado.leerEntero("Opcion: ");

			 switch (opcion) {
             case 1:
                 menuLibros();
                 break;
             case 2:
                 menuSocios();
                 break;
             case 3:
                 menuPrestamos();
                 break;
             case 0:
                 System.out.println("Saliendo del programa...");
                 break;
             default:
                 System.out.println("Opción no válida. Intente de nuevo.");
         }
     } while (opcion != 0);
 }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
