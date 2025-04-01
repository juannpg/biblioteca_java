package models;

public class Libro {
	
	private int codigo;
	private String isbn;
	private String titulo;
	private String escritor;
	private int anio_publicacion;
	private float puntuacion;
	
	
	public Libro(int codigo, String isbn, String titulo, String escritor, int anio_publicacion, float puntuacion) {
		super();
		this.codigo = codigo;
		this.isbn = isbn;
		this.titulo = titulo;
		this.escritor = escritor;
		this.anio_publicacion = anio_publicacion;
		this.puntuacion = puntuacion;
	}


	@Override
	public String toString() {
		return "Libro [codigo=" + codigo + ", isbn=" + isbn + ", titulo=" + titulo + ", escritor=" + escritor
				+ ", anio_publicacion=" + anio_publicacion + ", puntuacion=" + puntuacion + "]";
	}


	public int getCodigo() {
		return codigo;
	}


	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	public String getIsbn() {
		return isbn;
	}


	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public String getEscritor() {
		return escritor;
	}


	public void setEscritor(String escritor) {
		this.escritor = escritor;
	}


	public int getAnio_publicacion() {
		return anio_publicacion;
	}


	public void setAnio_publicacion(int anio_publicacion) {
		this.anio_publicacion = anio_publicacion;
	}


	public double getPuntuacion() {
		return puntuacion;
	}


	public void setPuntuacion(float puntuacion) {
		this.puntuacion = puntuacion;
	}
	
	
	
	
	
	
	
}

