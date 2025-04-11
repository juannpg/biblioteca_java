package regex;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FuncionesRegex {

  private static boolean esDiaValido(int año, int mes, int día) {
    int[] diasPorMes = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    if (esAnyoBisiesto(año)) {
      diasPorMes[1] = 29;
    }

    return día >= 1 && día <= diasPorMes[mes - 1];
  }

  private static boolean esAnyoBisiesto(int año) {
    return (año % 4 == 0 && (año % 100 != 0 || año % 400 == 0));
  }

  public static boolean correoBien(String correo) {
    String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z]{2,}$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(correo);
    return matcher.matches();
  }

  public static boolean fechaBien(String fecha) {
    String regex = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(fecha);
    if (!matcher.matches()) {
      return false;
    }

    String[] partes = fecha.split("-");
    int año = Integer.parseInt(partes[0]);
    int mes = Integer.parseInt(partes[1]);
    int día = Integer.parseInt(partes[2]);

    if (!esDiaValido(año, mes, día)) {
      return false;
    }

    return true;
  }

  public static boolean anyoBien(int anyo) {
    int añoActual = LocalDate.now().getYear();

    return (anyo >= 1 && anyo <= añoActual);
  }

  public static boolean isbnBien(String isbn) {
    isbn = isbn.replace("-", "");

    if (isbn.length() == 13 && isbn.matches("\\d{13}")) {
      return isbn.startsWith("978") || isbn.startsWith("979");
    }

    if (isbn.length() == 10) {
      if (isbn.matches("\\d{9}[0-9X]")) {
        int suma = 0;
        for (int i = 0; i < 9; i++) {
          suma += (isbn.charAt(i) - '0') * (10 - i);
        }
        char ultimoCaracter = isbn.charAt(9);
        int ultimoDigito = (ultimoCaracter == 'X') ? 10 : (ultimoCaracter - '0');
        suma += ultimoDigito;

        return suma % 11 == 0;
      }
    }

    return false;
  }

  public static boolean dniBien(String dni) {
    if (dni.length() != 9 || !dni.substring(0, 8).matches("\\d{8}")) {
      return false;
    }

    String numeroDni = dni.substring(0, 8);
    char letraDni = dni.charAt(8);

    int numero = Integer.parseInt(numeroDni);
    int resto = numero % 23;

    String letras = "TRWAGMYFPDXBNJZSQVHLCKE";

    return letraDni == letras.charAt(resto);
  }

  public static boolean telefonoBien(String telefono) {
    if (telefono.length() != 9) {
      return false;
    }

    if (telefono.matches("^([6-7]\\d{8}|91\\d{7}|93\\d{7}|95\\d{7}|96\\d{7}|97\\d{7})$")) {
      return true;
    }

    return false;
  }
}
