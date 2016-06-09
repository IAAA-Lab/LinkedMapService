package com.lms.controlador;

import com.lms.persistencia.ConexionBD;
import com.lms.util.PeticionPost;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

@RestController
public class PuntoController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Value("lms.urltemplate")
    private String urlTemplate;

    @RequestMapping("test")
    public String test() {
        log.info("Test");
        return "OK";
    }

    /**
     * Redireccion WMS
     **/
    @RequestMapping(value = "/service/getMap", method = RequestMethod.GET, produces = "image/png")
    public synchronized
    @ResponseBody
    byte[] getMap(HttpServletRequest request)
            throws IOException {
        String url = "http://localhost/cgi-bin/mapserv.exe?map=C:/Users/Elena/Desktop/LinkedMap/wms/src/main/resources/wms//mapfile.map&";
        Enumeration<String> lista = request.getParameterNames();
        String param = "";
        int cont = 0;
        // Valor de los parametros d ela request
        while (cont < 10) {
            param = lista.nextElement();
            if (!param.equals("time")) {
                url = url + param + "=" + request.getParameterValues(param)[0]
                        + "&";
                cont++;
            }
        }

        URL obj = new URL(url);
        BufferedImage img = null;
        // Obtener la imagen de la url del WMS
        img = ImageIO.read(obj);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        // Escribir imagen de salida
        ImageIO.write(img, "png", bao);
        byte[] imageData = bao.toByteArray();
        bao.close();
        // Devuelve la tile
        return imageData;

    }

    /**
     * Funcion redireccion WMS PETICION:GetFeatureInfo
     **/
    @RequestMapping(path = "infoWMS", method = RequestMethod.POST)
    @ResponseBody
    public String getInfoWMS(@RequestParam(value = "txtId") String id,
                             @RequestParam(value = "longitud") String xmin,
                             @RequestParam(value = "latitud") String ymin,
                             @RequestParam(value = "infoURL") String url) {
        String respuesta = "";
        try {
            PeticionPost post = new PeticionPost(url);
            respuesta = post.getRespueta();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return respuesta;

    }

    /**
     * Funcion redireccion WMS PETICION: GetMap
     **/
    @RequestMapping(path = "imagenWMS", method = RequestMethod.POST)
    @ResponseBody
    public byte[] getImagenWMS(@RequestParam(value = "txtId") String id,
                               @RequestParam(value = "longitud") String xmin,
                               @RequestParam(value = "latitud") String ymin,
                               @RequestParam(value = "infoURL") String url) throws IOException {

        String urlTemplate = "http://localhost/cgi-bin/mapserv.exe?map=C:/Users/Elena/Desktop/LinkedMap/wms/src/main/resources/wms//mapfile.map&";

        String[] procesar = url.split("getMap?");

        String urlModificada = urlTemplate + procesar[1];
        System.out.println(urlModificada);
        URL obj = new URL(urlModificada);
        BufferedImage img = null;
        // Obtener la imagen de la url del WMS
        img = ImageIO.read(obj);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        // Escribir imagen de salida
        ImageIO.write(img, "png", bao);
        byte[] imageData = bao.toByteArray();
        bao.close();
        return imageData;
    }

    /**
     * Función actualizar BD
     **/
    @RequestMapping(path = "service/bd/resource/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String actualizar(@RequestBody String punto) {
        String respuesta;
        String id;
        String nombre_extendido;
        String iditneficador_geografico;
        String nombre_alternativo_2;
        String nombre_alternativo_3;
        String nombre_variante_1;
        String nombre_variante_2;
        String nombre_variante_3;
        String fuente_nombre_extendido;
        String fuente_identificador_geografico;
        String fuente_alternativo_2;
        String fuente_alternativo_3;
        String fuente_variante_1;
        String fuente_variante_2;
        String fuente_variante_3;
        String idioma_nombre_extendido;
        String idioma_identificador_geografico;
        String idioma_alternativo_2;
        String idioma_alternativo_3;
        String idioma_variante_1;
        String idioma_variante_2;
        String idioma_variante_3;
        String estatus_nombre_extendido;
        Double longitud_etrs89_regcan95;
        Double latitud_etrs89_regcan95;
        String huso_etrs89_regcan95;
        Double xutm_etrs89_regcan95;
        Double yutm_etrs89_regcan95;
        String sistema_referencia_etrs89_regcan95;
        String hojamtn_25;
        String codigo_ine;
        String codigo_ngbe;

        // Asignar valores de los diferentes campos
        String[] procesar = punto.split(",");
        System.out.println(procesar.length);
        String[] campos = procesar[0].split(":");
        id = campos[1];

        campos = procesar[1].split(":");
        nombre_extendido = campos[1];

        campos = procesar[2].split(":");
        huso_etrs89_regcan95 = campos[1];

        // procesar[3]=GEOMETRY //Actualizarlo en BD mediante crear POINT
        // POSTGRES FUNCTION

        iditneficador_geografico = getString(procesar[4]);
        nombre_alternativo_2 = getString(procesar[5]);
        nombre_alternativo_3 = getString(procesar[6]);
        nombre_variante_1 = getString(procesar[7]);
        nombre_variante_2 = getString(procesar[8]);
        nombre_variante_3 = getString(procesar[9]);
        fuente_nombre_extendido = getString(procesar[10]);
        fuente_identificador_geografico = getString(procesar[11]);
        fuente_alternativo_2 = getString(procesar[12]);
        fuente_alternativo_3 = getString(procesar[13]);
        fuente_variante_1 = getString(procesar[14]);
        fuente_variante_2 = getString(procesar[15]);
        fuente_variante_3 = getString(procesar[16]);
        idioma_nombre_extendido = getString(procesar[17]);
        idioma_identificador_geografico = getString(procesar[18]);
        idioma_alternativo_2 = getString(procesar[19]);
        idioma_alternativo_3 = getString(procesar[20]);
        idioma_variante_1 = getString(procesar[21]);
        idioma_variante_2 = getString(procesar[22]);
        idioma_variante_3 = getString(procesar[23]);
        estatus_nombre_extendido = getString(procesar[24]);
        sistema_referencia_etrs89_regcan95 = getString(procesar[25]);
        hojamtn_25 = getString(procesar[26]);
        codigo_ine = getString(procesar[27]);
        codigo_ngbe = getString(procesar[28]);
        String crs = getString(procesar[29]);

        campos = procesar[30].split(":");
        longitud_etrs89_regcan95 = Double.parseDouble(campos[1]);

        campos = procesar[31].split(":");
        latitud_etrs89_regcan95 = Double.parseDouble(campos[1]);

        campos = procesar[32].split(":");
        xutm_etrs89_regcan95 = Double.parseDouble(campos[1]);

        campos = procesar[33].split(":");
        String[] aux = campos[1].split("}");
        yutm_etrs89_regcan95 = Double.parseDouble(aux[0]);

        // Query que actualiza la feature en al Base de datos
        String query = "UPDATE random2 SET (id,nombre_extendido,identificador_geografico,nombre_alternativo_2,"
                + "nombre_alternativo_3,nombre_variante_1,nombre_variante_2,nombre_variante_3,fuente_nombre_extendido,"
                + "fuente_identificador_geografico,fuente_alternativo_2,fuente_alternativo_3,fuente_variante_1,"
                + "fuente_variante_2,fuente_variante_3,idioma_nombre_extendido,idioma_identificador_geografico,idioma_alternativo_2,"
                + "idioma_alternativo_3,idioma_variante_1,idioma_variante_2,idioma_variante_3,estatus_nombre_extendido,"
                + "longitud_etrs89_regcan95,latitud_etrs89_regcan95,huso_etrs89_regcan95,xutm_etrs89_regcan95,yutm_etrs89_regcan95,"
                + "sistema_referencia_etrs89_regcan95,hojamtn_25,codigo_ine,codigo_ngbe,geom,crs)=("
                + id
                + ",'"
                + nombre_extendido
                + "','"
                + iditneficador_geografico
                + "','"
                + nombre_alternativo_2
                + "','"
                + nombre_alternativo_3
                + "','"
                + nombre_variante_1
                + "','"
                + nombre_variante_2
                + "','"
                + nombre_variante_3
                + "','"
                + fuente_nombre_extendido
                + "','"
                + fuente_identificador_geografico
                + "','"
                + fuente_alternativo_2
                + "','"
                + fuente_alternativo_3
                + "','"
                + fuente_variante_1
                + "','"
                + fuente_variante_2
                + "','"
                + fuente_variante_3
                + "','"
                + idioma_nombre_extendido
                + "','"
                + idioma_identificador_geografico
                + "','"
                + idioma_alternativo_2
                + "','"
                + idioma_alternativo_3
                + "','"
                + idioma_variante_1
                + "','"
                + idioma_variante_2
                + "','"
                + idioma_variante_3
                + "','"
                + estatus_nombre_extendido
                + "',"
                + longitud_etrs89_regcan95
                + ","
                + latitud_etrs89_regcan95
                + ","
                + huso_etrs89_regcan95
                + ","
                + xutm_etrs89_regcan95
                + ","
                + yutm_etrs89_regcan95
                + ",'"
                + sistema_referencia_etrs89_regcan95
                + "','"
                + hojamtn_25
                + "','"
                + codigo_ine
                + "','"
                + codigo_ngbe
                + "',"
                + "ST_SetSRID(ST_MakePoint("
                + xutm_etrs89_regcan95
                + ","
                + yutm_etrs89_regcan95
                + "),'3857')"
                + ",'"
                + "3857"
                + "') WHERE id=" + id;

        // Actualizar BD
        ConexionBD.actualizrBD(query);
        // Devuelve x e y actualizadas y se devuelven al cliente para gestionar
        // la actualizacion
        respuesta = xutm_etrs89_regcan95 + "," + yutm_etrs89_regcan95;

        return respuesta;

    }

    /**
     * Metodo parse JSON de respuesta del WMS
     **/
    @RequestMapping(value = "parseGetInfo", method = RequestMethod.POST)
    public
    @ResponseBody
    ArrayList<String> parseJSON(
            @RequestParam(value = "infoURL") String url) {
        String respuesta = "";
        ArrayList<String> r = new ArrayList<String>();
        String jsonRaw = "";
        try {
            PeticionPost post = new PeticionPost(url);
            System.out.println(url);
            jsonRaw = post.getRespueta();
            // System.out.println("R"+jsonRaw);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String jsonParam = jsonRaw.substring(jsonRaw.indexOf("=") + 1);

        String[] procesar = jsonParam.split("\\'");
        for (int i = 0; i < procesar.length; i++) {
            if ((i % 2) == 0) {
                System.out.println(procesar[i]);
            } else {
                respuesta += procesar[i] + ",";
                r.add(procesar[i]);
                ;
            }
        }
        // Devuelve ArrayList que contiene los parametros del JSON
        return r;

    }

    // Función auxiliar para obtener valor de un parametro
    private String getString(String procesar) {
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
