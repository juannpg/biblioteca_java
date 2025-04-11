package models;

import java.util.Objects;

public class Socio {

  private int codigo;
  private String dni;
  private String nombre;
  private String domicilio;
  private String telefono;
  private String correo;

  public Socio(int codigo, String dni, String nombre, String domicilio, String telefono, String correo) {
    super();
    this.codigo = codigo;
    this.dni = dni;
    this.nombre = nombre;
    this.domicilio = domicilio;
    this.telefono = telefono;
    this.correo = correo;
  }

  @Override
  public String toString() {
    return String.format(
      "Socio [Código: %d | DNI: %s | Nombre: %s | Domicilio: %s | Teléfono: %s | Correo: %s]",
      codigo,
      dni,
      nombre,
      domicilio,
      telefono,
      correo
    );
  }

  @Override
  public int hashCode() {
    return Objects.hash(dni);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Socio other = (Socio) obj;
    return Objects.equals(dni, other.dni);
  }

  public Socio(String dni, String nombre) {
    this.dni = dni;
    this.nombre = nombre;
  }

  public int getCodigo() {
    return codigo;
  }

  public void setCodigo(int codigo) {
    this.codigo = codigo;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDomicilio() {
    return domicilio;
  }

  public void setDomicilio(String domicilio) {
    this.domicilio = domicilio;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }
}
