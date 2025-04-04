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

  @Override
  public String toString() {
    return (
      "Prestamo [libro=" +
      libro +
      " socio=" +
      socio +
      ", fechaInicio=" +
      fechaInicio +
      ", fechaFin=" +
      fechaFin +
      ", fechaDevolucion=" +
      fechaDevolucion +
      "]"
    );
  }

  public Libro getLibro() {
    return libro;
  }

  public void setLibro(Libro libro) {
    this.libro = libro;
  }

  public Socio getSocio() {
    return socio;
  }

  public void setSocio(Socio socio) {
    this.socio = socio;
  }

  public String getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(String fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public String getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(String fechaFin) {
    this.fechaFin = fechaFin;
  }

  public String getFechaDevolucion() {
    return fechaDevolucion;
  }

  public void setFechaDevolucion(String fechaDevolucion) {
    this.fechaDevolucion = fechaDevolucion;
  }
}
