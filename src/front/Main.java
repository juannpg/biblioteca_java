package front;

import java.util.ArrayList;

import dao.AccesoLibro;
import dao.AccesoPrestamo;
import dao.AccesoSocio;

import entrada.Teclado;
import exceptions.BDException;
import exceptions.ExcepcionPrestamo;
import exceptions.SocioException;

import models.Libro;
import models.Socio;

public class Main {

	public static void escribirMenuOpcionesLibro() {
		System.out.println();
		System.out.println("Elige una opción del menu de Libro");
		System.out.println("0) Salir del programa.");
		System.out.println("1) Insertar un libro en la base de datos");
		System.out.println("2) Eliminar un libro, por código, de la base de datos");
		System.out.println("3) Consultar todos los libros de la base de datos.");
		System.out.println(
				"4) Consultar varios libros, por escritor, de la base de datos, ordenados por puntuación decendente.");
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
		System.out.println(
				"4) Consultar varios socios, por localidad, de la base de datos, ordenados por nombre ascendiente.");
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
		int opcion = -1;

		do {
			try {
				escribirMenuOpcionesLibro();
				System.out.println();
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

					boolean anadiLibro = AccesoLibro.anadirLibro(isbn, titulo, escritor, anyoPublicacion, puntuacion);

					if (anadiLibro) {
						System.out.println("Libro añadido correctamente.");
					} else {
						System.out.println("No se pudo añadir el libro.");
					}

					break;

				case 2:
					System.out.println("Eliminar libro...");
					// pedir datos al usuario
					int codigo = Teclado.leerEntero("Introduce el código del libro: ");

					boolean borrarLibroPorCodigo = AccesoLibro.borrarLibroPorCodigo(codigo);

					if (borrarLibroPorCodigo) {
						System.out.println("Libro eliminado correctamente.");

					} else {
						System.out.println("No se pudo eliminar el libro.");

					}

					break;

				case 3:
					System.out.println("Consultar todos los libros...");
					ArrayList<Libro> consultarLibros = AccesoLibro.consultarLibros();

					if (consultarLibros.isEmpty()) {
						System.out.println("No hay ningun libro en la coleccion");
					} else {
						System.out.println(consultarLibros);
					}

					break;

				case 4:
					System.out.println("Consultar libros por escritor...");
					String nombreEscritor = Teclado.leerCadena("Introduce el nombre del escritor: ");
					ArrayList<Libro> consultarLibrosOrdenados = AccesoLibro.consultarLibrosOrdenados(nombreEscritor);
					if (consultarLibrosOrdenados.isEmpty()) {
						System.out.println("No se encontro ningún libro");
					} else {
						System.out.println(consultarLibrosOrdenados);
					}
					break;

				case 5:
					System.out.println("Consultar libros no prestados...");
					ArrayList<Libro> consultarLibrosNoPrestados = AccesoLibro.consultarLibrosNoPrestados();

					if (consultarLibrosNoPrestados.isEmpty()) {
						System.out.println("No se encontro ningún libro");
					} else {
						System.out.println(consultarLibrosNoPrestados);
					}
					break;

				case 6:
					System.out.println("Consultar libros devueltos en una fecha...");
					String fechaDevolucion = Teclado.leerCadena("Introruce una fehca: ");
					ArrayList<Libro> consultarLibrosDevueltos = AccesoLibro.consultarLibrosDevueltos(fechaDevolucion);

					if (consultarLibrosDevueltos.isEmpty()) {
						System.out.println("No se encontro ningún libro");
					} else {
						System.out.println(consultarLibrosDevueltos);
					}
					break;

				case 0:
					System.out.println("Regresando al menú principal...");
					break;
				default:
					System.out.println("Opción no válida. Intente de nuevo.");
				}

			} catch (BDException e) {
				System.out.println("Error al ejecutar opcion del Menu Libro: " + e.getMessage());
			}

		} while (opcion != 0);

	}

	public static void menuSocios() {
		int opcion = -1;
		do {
			try {
				escribirMenuOpcionesSocio();
				opcion = Teclado.leerEntero("Opción: ");

				switch (opcion) {
				case 1:
					System.out.println("Insertar socio...");
//					String dni = Teclado.leerCadena("Introduce el DNI");
//					String nombre = Teclado.leerCadena("Introduce el nombre");
//					String domicilio = Teclado.leerCadena("Introducir el domicilio");
//					String telefono = Teclado.leerCadena("Introduce el numero de telefono");
//					String correo = Teclado.leerCadena("Introduce el correo");
//
//					Socio socio = new Socio(dni, nombre, domicilio, telefono, correo);
//					boolean añadirSocio = AccesoSocio.añadirSocio(socio);
					break;

				case 2:
					System.out.println("Eliminar socio...");
					// pedir datos al usuario
					int codigoSocio = Teclado.leerEntero("Introduce el código del Socio: ");

					boolean eliminarSocio = AccesoSocio.eliminarSocio(codigoSocio);

					if (eliminarSocio) {
						System.out.println("Socio eliminado correctamente.");

					} else {
						System.out.println("No se pudo eliminar el Socio.");

					}

					break;

				case 3:
					System.out.println("Consultar todos los socios...");
					ArrayList<Socio> consultarTodosSocios = AccesoSocio.consultarTodosSocios();

					if (consultarTodosSocios.isEmpty()) {
						System.out.println("No hay ningun socio");
					} else {
						System.out.println(consultarTodosSocios);
					}
					break;

				case 4:
					System.out.println("Consultar socios por localidad...");
					String localidad = Teclado.leerCadena("Introduce una localidad");
					ArrayList<Socio> consultarSociosLocalidad = AccesoSocio.consultarSociosLocalidad(localidad);
					
					if (consultarSociosLocalidad.isEmpty()) {
						System.out.println();
					}
					break;
					
				case 5:
					System.out.println("Consultar socios sin préstamos...");
					ArrayList<Socio> consultarSociosNoPrestatario = AccesoSocio.consultarSociosNoPrestatario();
					
					if (consultarSociosNoPrestatario.isEmpty()) {
						System.out.println("No se encontro ningun socio");
					} else {
						System.out.println(consultarSociosNoPrestatario);;
					}
					break;
					
				case 6:
					System.out.println("Consultar socios con préstamos en una fecha...");
					String fecha = Teclado.leerCadena("Introduce una fecha");
					ArrayList<Socio> consultarSociosPrestatario = AccesoSocio.consultarSociosPrestatario(fecha);
					
					if (consultarSociosPrestatario.isEmpty()) {
						System.out.println("No se encontro ningun socio");
					} else {
						System.out.println(consultarSociosPrestatario);
					}
					break;
				case 0:
					System.out.println("Regresando al menú principal...");
					break;
				default:
					System.out.println("Opción no válida. Intente de nuevo.");
				}
			} catch (BDException e) {
				System.out.println("Error al ejecutar opcion del Menu Socios: " + e.getMessage());
			} catch (SocioException e) {
				System.out.println("Error con socio: " + e.getMessage());

			}
		} while (opcion != 0);
	}

	public static void menuPrestamos() {
		int opcion = -1;
		do {
//			try {
//				escribirMenuOpcionesPrestamo();
//				opcion = Teclado.leerEntero("Opción: ");
//
//				switch (opcion) {
//				case 1:
//					System.out.println("Insertar préstamo...");
//					break;
//				case 2:
//					System.out.println("Actualizar préstamo...");
//					break;
//				case 3:
//					System.out.println("Eliminar préstamo...");
//					break;
//				case 4:
//					System.out.println("Consultar todos los préstamos...");
//					break;
//				case 5:
//					System.out.println("Consultar préstamos no devueltos...");
//					break;
//				case 6:
//					System.out.println("Consultar préstamos realizados en una fecha...");
//					break;
//				case 0:
//					System.out.println("Regresando al menú principal...");
//					break;
//				default:
//					System.out.println("Opción no válida. Intente de nuevo.");
//				}
//			} catch (BDException e) {
//				System.out.println("Error al ejecutar opcion del Menu Libro: " + e.getMessage());
//			} catch (ExcepcionPrestamo e) {
//				System.out.println("Error con prestamo: " + e.getMessage());
//			} 
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
