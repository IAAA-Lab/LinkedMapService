package controladoresTest;

import com.lms.util.PeticionPost;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;


public class PuntoControllerTest {

    String urlTemplate = "http://localhost/cgi-bin/mapserv.exe?map=C:/Users/Elena/workspace/LinkedMap-v1/WMS/test/wms/mapfile.map&";
    String params = "SERVICE=WMS&VERSION=1.3.0&REQUEST=GetMap&FORMAT=image/png&TRANSPARENT=true&LAYERS=ngbe&WIDTH=256&HEIGHT=256&CRS=EPSG:3857&STYLES=&BBOX=0,5009377.085697312,626172.1357121639,5635549.22140947510";
    String paramsInfo = "SERVICE=WMS&VERSION=1.3.0&REQUEST=GetFeatureInfo&FORMAT=image%2Fpng&TRANSPARENT=true&QUERY_LAYERS=ngbe&LAYERS=ngbe&INFO_FORMAT=text%2Fplain&I=157&J=98&WIDTH=256&HEIGHT=256&CRS=EPSG%3A3857&STYLES=&BBOX=-782715.1696402058%2C4383204.9499851465%2C-626172.1357121648%2C4539747.983913188";

    @Test
    public void getMapTest() throws IOException {
        String url = urlTemplate + params;
        URL obj = new URL(url);
        BufferedImage img = null;
        BufferedImage imgFile = null;
        imgFile = ImageIO.read(PuntoControllerTest.class.getResourceAsStream("/getMapTest.png"));
        img = ImageIO.read(obj);
        //Genera archivo de la imagen
        //   ImageIO.write(img, "jpg",new File("C:/Users/Elena/DEsktop/out.jpg"));
        // Create a byte array output stream.
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        // Write to output stream
        ImageIO.write(img, "png", bao);
        byte[] imageData = bao.toByteArray();

        bao.close();

        //Campara la imagne obtenida por la URL con la imagen del fichero descargada
        //directamente del WMS a través del navegado
        if (img.getWidth() == imgFile.getWidth() && img.getHeight() == imgFile.getHeight()) {
            int width = img.getWidth();
            int height = img.getHeight();

            // Loop over every pixel.
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Compare the pixels for equality.
                    if (img.getRGB(x, y) != imgFile.getRGB(x, y)) {
                        System.out.println("mal-getMapTest");
                    }
                }
            }
        } else {
            System.out.println("mal-getMapTest");
        }

        System.out.println("ok-getMapTest");
    }


    @Test
    public void getInfoTest() {
        //Valor obtenido de hacer la petición directamente al WMS
        String valorEsperado = "GetFeatureInfo results:Layer 'ngbe'  Feature 2388042:id = '2388042'" +
                "nombre_extendido = '\"Arroyo de Majalberraque\"'identificador_geografico = '\"Arroyo de Majalberraque\"'" +
                "nombre_alternativo_2 = ''nombre_alternativo_3 = ''nombre_variante_1 = ''nombre_variante_2 = ''" +
                "nombre_variante_3 = ''fuente_nombre_extendido = '\"BTN25\"'fuente_identificador_geografico = '\"BTN25\"'" +
                "fuente_alternativo_2 = ''fuente_alternativo_3 = ''fuente_variante_1 = ''fuente_variante_2 = ''" +
                "fuente_variante_3 = ''idioma_nombre_extendido = ''idioma_identificador_geografico = ''idioma_alternativo_2 = ''" +
                "idioma_alternativo_3 = ''idioma_variante_1 = ''idioma_variante_2 = ''idioma_variante_3 = ''estatus_nombre_extendido = ''" +
                "longitud_etrs89_regcan95 = '-6.15000009536743'latitud_etrs89_regcan95 = '37.2799987792969'huso_etrs89_regcan95 = '29'" +
                "xutm_etrs89_regcan95 = '751963.6875'yutm_etrs89_regcan95 = '4130151.5'sistema_referencia_etrs89_regcan95 = '\"ETRS89\"'" +
                "hojamtn_25 = '\"1002c1\"'codigo_ine = '\"41016\"'codigo_ngbe = '\"5.1\"'crs = '3857'";

        String respuesta = "";
        try {
            PeticionPost post = new PeticionPost(urlTemplate + paramsInfo);
            respuesta = post.getRespueta();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Quitar espacios y tabuladores,para comparar los resultados
        valorEsperado = valorEsperado.replaceAll("\\s", "");
        respuesta = respuesta.replaceAll("\\s", "");
        Boolean result = valorEsperado.equals(respuesta);
        if (result) {
            System.out.println("OK-getInfoTest");
        } else {
            System.out.println("MAL-getInfoTest");
            System.out.println(valorEsperado);
            System.out.println(respuesta);
        }
    }

    @Test
    public void imagenWMSTest() {
        //Seria el mismo test que getMapTest porque comprueba la petición getMap
    }

    @Test
    public void actualizarBDTest() {

    }
}
