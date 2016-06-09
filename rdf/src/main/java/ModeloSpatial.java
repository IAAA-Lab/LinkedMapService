import org.apache.jena.query.spatial.EntityDefinition;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.SKOSXL;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModeloSpatial {

    public static void procesarFilaSpatial(ArrayList<String> fila)
            throws IOException, SQLException {
        // Variables procesar datos
        Integer id;
        String nombre_extendido;
        float longitud_etrs89_regcan95;
        float latitud_etrs89_regcan95;

        // CREAR MODELO VACIO
        Model model = ModelFactory.createDefaultModel();
        // PREFIX for model
        model.setNsPrefix("ogc", "http://www.w3.org/2003/01/geo/wgs84_pos#");
        model.setNsPrefix("geo", "http://www.opengis.net/ont/geosparql#");
        model.setNsPrefix("skosxl", "http://www.w3.org/2008/05/skos-xl#");
        model.setNsPrefix("dc", "http://purl.org/dc/elements/1.1/");
        // DEFINICION PROPIEDADES DEL MODELO VACIAS
        String puntoURI; // URI del punto (@id)

        String aux, aux2;
        int cont = 0;

        // BUCLE PARA CARGAR x NUMERO DE PUNTOS
        while (fila.size() > cont) {

            String[] procesar = fila.get(cont).split(";");
            aux = procesar[0];
            aux2 = aux.replace(",", ".");
            id = (int) Float.parseFloat(aux2);
            nombre_extendido = procesar[1];

            // longitud
            if (procesar[23].startsWith("-")) {
                aux = procesar[23].substring(1);
                aux2 = aux.replace(",", ".");
                longitud_etrs89_regcan95 = Float.parseFloat(aux2);
                longitud_etrs89_regcan95 = -longitud_etrs89_regcan95;
            } else {
                aux = procesar[23];
                aux2 = aux.replace(",", ".");
                longitud_etrs89_regcan95 = Float.parseFloat(aux2);
            }
            // latitud
            if (procesar[24].startsWith("-")) {
                aux = procesar[24].substring(1);
                aux2 = aux.replace(",", ".");
                latitud_etrs89_regcan95 = Float.parseFloat(aux2);
                latitud_etrs89_regcan95 = -latitud_etrs89_regcan95;
            } else {
                aux = procesar[24];
                aux2 = aux.replace(",", ".");
                latitud_etrs89_regcan95 = Float.parseFloat(aux2);
            }

            // ASIGNAR VALOR A LAS PROPIEDADES
            puntoURI = "http://localhost/collections/1/features/" + id;

            // A�adir al modelo
            Literal lo = model.createTypedLiteral(new Float(
                    longitud_etrs89_regcan95));
            Literal la = model.createTypedLiteral(new Float(
                    latitud_etrs89_regcan95));

            model.createResource(puntoURI)
                    // Propieades para spatial
                    .addLiteral(EntityDefinition.geo_longitude, lo)
                    .addLiteral(EntityDefinition.geo_latitude, la)
                    .addProperty(SKOSXL.literalForm, nombre_extendido);
            cont++;
        }

        String fileName = "./src/resources/TTLfromBDSpatial.ttl";
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), "UTF-8"));

        try {

            System.out.println("FIN");
            model.write(out, "TTL");
        } finally {
            try {
                out.close();
            } catch (IOException closeException) {
                // ignore
            }
        }

    }

}
