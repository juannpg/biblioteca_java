package models;

public class Prestamo {
	private Libro libro;
	private Socio socio;
	private String fechaInicio;
	private String fechaFin;
	private String fechaDevolucion;

  	public Prestamo(
  		Libro libro,
  		Socio socio,
  		String fechaInicio,
  		String fechaFin,
  		String fechaDevolucion
  	) {
  		super();
    	this.libro = libro;
    	this.socio = socio;
    	this.fechaInicio = fechaInicio;
    	this.fechaFin = fechaFin;
    	this.fechaDevolucion = fechaDevolucion;
  	}
}