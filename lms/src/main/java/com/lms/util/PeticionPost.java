package com.lms.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class PeticionPost {
    String data;
    private URL url;

    public PeticionPost(String url) throws MalformedURLException {
        this.url = new URL(url);
        data = "";
    }

    public void add(String propiedad, String valor) throws UnsupportedEncodingException {
//codificamos cada uno de los valores
        if (data.length() > 0)
            data += "&" + URLEncoder.encode(propiedad, "UTF-8") + "=" + URLEncoder.encode(valor, "UTF-8");
        else
            data += URLEncoder.encode(propiedad, "UTF-8") + "=" + URLEncoder.encode(valor, "UTF-8");
    }

    public String getRespueta() throws IOException {
        String respuesta = "";
//abrimos la conexiÃ³n
        URLConnection conn = url.openConnection();
//especificamos que vamos a escribir
        conn.setDoOutput(true);
//obtenemos el flujo de escritura
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//escribimos
        wr.write(data);
//cerramos la conexiÃ³n
        wr.close();

        //obtenemos el flujo de lectura
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String linea;
        //procesamos al salida
        while ((linea = rd.readLine()) != null) {
            respuesta += linea;
        }
        return respuesta;
    }

}