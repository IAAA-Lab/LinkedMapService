package com.lms.util;

/**
 * Created by javier on 06/06/16.
 */
public class Utils {
    // Función auxiliar para obtener valor de un parametro
    public static String getString(String procesar) {
        String[] campos;
        String parametro;
        campos = procesar.split(":");
        if (campos.length == 1) {
            parametro = "";
        } else {
            parametro = campos[1];
        }
        return parametro;
    }
}
