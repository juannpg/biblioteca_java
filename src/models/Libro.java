package models;

import java.util.Objects;

public class Libro {

  private int codigo;
  private String isbn;
  private String titulo;
  private String escritor;
  private int anyo_publicacion;
  private float puntuacion;

  public Libro(int codigo, String isbn, String titulo, String escritor, int anyo_publicacion, float puntuacion) {
    super();
    this.codigo = codigo;
    this.isbn = isbn;
    this.titulo = titulo;
    this.escritor = escritor;
    this.anyo_publicacion = anyo_publicacion;
    this.puntuacion = puntuacion;
  }

  public Libro(String isbn, String titulo) {
    this.isbn = isbn;
    this.titulo = titulo;
  }

  @Override
  public String toString() {
    return String.format(
      "Libro [Código: %d | ISBN: %s | Título: %s | Escritor: %s | Año: %d | Puntuación: %.1f]",
      codigo,
      isbn,
      titulo,
      escritor,
      anyo_publicacion,
      puntuacion
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(isbn);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Libro other = (Libro) obj;
    return Objects.equals(isbn, other.isbn);
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

  public int getAnyo_publicacion() {
    return anyo_publicacion;
  }

  public void setAnyo_publicacion(int anyo_publicacion) {
    this.anyo_publicacion = anyo_publicacion;
  }

  public double getPuntuacion() {
    return puntuacion;
  }

  public void setPuntuacion(float puntuacion) {
    this.puntuacion = puntuacion;
  }
}
