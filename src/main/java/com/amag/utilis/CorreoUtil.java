package com.amag.utilis;

public class CorreoUtil {

	public String generarCorreo(String nombres, String apellidos) {
        String nombreUsuario = nombres.toLowerCase().substring(0, 1) + apellidos.toLowerCase();
        return nombreUsuario + "@mail.com";
    }
}
