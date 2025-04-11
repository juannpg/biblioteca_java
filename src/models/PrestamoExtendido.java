package models;

public class PrestamoExtendido {

  private String dni;
  private String nombreSocio;
  private String isbn;
  private String titulo;
  private String fechaDevolucion;

  public PrestamoExtendido(String dni, String nombreSocio, String isbn, String titulo, String fechaDevolucion) {
    super();
    this.dni = dni;
    this.nombreSocio = nombreSocio;
    this.isbn = isbn;
    this.titulo = titulo;
    this.fechaDevolucion = fechaDevolucion;
  }

  @Override
  public String toString() {
    return (
      "PrestamoExtendido [dni=" +
      dni +
      ", nombreSocio=" +
      nombreSocio +
      ", isbn=" +
      isbn +
      ", titulo=" +
      titulo +
      ", fechaDevolucion=" +
      fechaDevolucion +
      "]"
    );
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getNombreSocio() {
    return nombreSocio;
  }

  public void setNombreSocio(String nombreSocio) {
    this.nombreSocio = nombreSocio;
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

  public String getFechaDevolucion() {
    return fechaDevolucion;
  }

  public void setFechaDevolucion(String fechaDevolucion) {
    this.fechaDevolucion = fechaDevolucion;
  }
}
