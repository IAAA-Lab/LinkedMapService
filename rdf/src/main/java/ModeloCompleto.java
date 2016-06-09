import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.SKOSXL;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModeloCompleto {

    public static void procesarModelo(ArrayList<String> fila)
            throws IOException, SQLException {
        // Variables procesar datos
        Integer id = null;
        String nombre_extendido = "";
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
        float longitud_etrs89_regcan95 = 0;
        float latitud_etrs89_regcan95 = 0;
        Integer huso_etrs89_regcan95;
        float xutm_etrs89_regcan95;
        float yutm_etrs89_regcan95;
        String sistema_referencia_etrs89_regcan95;
        String hojamtn_25;
        String codigo_ine;
        String codigo_ngbe;

        // CREAR MODELO VACIO
        Model model = ModelFactory.createDefaultModel();
        // PREFIX for model
        model.setNsPrefix("ogc", "http://www.w3.org/2003/01/geo/wgs84_pos#");
        model.setNsPrefix("geo", "http://www.opengis.net/ont/geosparql#");
        model.setNsPrefix("skosxl", "http://www.w3.org/2008/05/skos-xl#");
        model.setNsPrefix("dc", "http://purl.org/dc/elements/1.1/");
        // DEFINICION PROPIEDADES DEL MODELO VACIAS
        String puntoURI = ""; // URI del punto (@id)
        String name = ""; // nombre del punto
        String punto = ""; // Punto

        String aux, aux2;

        // BUCLE PARA CARGAR x NUMERO DE PUNTOS
        int cont = 0;
        
        while (fila.size() > cont) {
            String[] procesar = fila.get(cont).split(";");
            aux = procesar[0];
            aux2 = aux.replace(",", ".");
            id = (int) Float.parseFloat(aux2);
            nombre_extendido = procesar[1];
            iditneficador_geografico = procesar[2];
            nombre_alternativo_2 = procesar[3];
            nombre_alternativo_3 = procesar[4];
            nombre_variante_1 = procesar[5];
            nombre_variante_2 = procesar[6];
            nombre_variante_3 = procesar[7];
            fuente_nombre_extendido = procesar[8];
            fuente_identificador_geografico = procesar[9];
            fuente_alternativo_2 = procesar[10];
            fuente_alternativo_3 = procesar[11];
            fuente_variante_1 = procesar[12];
            fuente_variante_2 = procesar[13];
            fuente_variante_3 = procesar[14];
            idioma_nombre_extendido = procesar[15];
            idioma_identificador_geografico = procesar[16];
            idioma_alternativo_2 = procesar[17];
            idioma_alternativo_3 = procesar[18];
            idioma_variante_1 = procesar[19];
            idioma_variante_2 = procesar[20];
            idioma_variante_3 = procesar[21];
            estatus_nombre_extendido = procesar[22];
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
            huso_etrs89_regcan95 = Integer.parseInt(procesar[25]);
            // XUTM
            if (procesar[26].startsWith("-")) {
                aux = procesar[26].substring(1);
                aux2 = aux.replace(",", ".");
                xutm_etrs89_regcan95 = Float.parseFloat(aux2);
                xutm_etrs89_regcan95 = -xutm_etrs89_regcan95;
            } else {
                aux = procesar[26];
                aux2 = aux.replace(",", ".");
                xutm_etrs89_regcan95 = Float.parseFloat(aux2);
            }
            // YUTM
            if (procesar[27].startsWith("-")) {
                aux = procesar[27].substring(1);
                aux2 = aux.replace(",", ".");
                yutm_etrs89_regcan95 = Float.parseFloat(aux2);
                yutm_etrs89_regcan95 = -yutm_etrs89_regcan95;
            } else {
                aux = procesar[27];
                aux2 = aux.replace(",", ".");
                yutm_etrs89_regcan95 = Float.parseFloat(aux2);
            }

            sistema_referencia_etrs89_regcan95 = procesar[28];

            hojamtn_25 = procesar[29];
            codigo_ine = procesar[30];
            codigo_ngbe = procesar[31];
            
            // ASIGNAR VALOR A LAS PROPIEDADES
            puntoURI = "http://localhost:3030/ds/" + id;
            name = nombre_extendido;
            punto = "POINT(" + longitud_etrs89_regcan95 + " "
                    + latitud_etrs89_regcan95 + ")";// ^^<http://www.opengis.net/ont/geosparql#wktLiteral>";

            // CREAR RECURSO Y A�ADIR PROPIEDADES
            EntityDefinition entDef = new EntityDefinition("entityField",
                    "geoField");
            Resource r1 = null;
            // CREAR RECURSO PREFLABEL
            if (!nombre_extendido.isEmpty()) {
                if (!fuente_nombre_extendido.isEmpty()) {
                    if (!idioma_nombre_extendido.isEmpty()) {
                        r1 = crearRecursoCompleto(model, nombre_extendido,
                                fuente_nombre_extendido,
                                idioma_nombre_extendido);
                    } else {
                        r1 = crearRecursoSinIdioma(model, nombre_extendido,
                                fuente_nombre_extendido);

                    }
                } else {
                    if (!idioma_nombre_extendido.isEmpty()) {
                        r1 = crearRecursoSinFuente(model, nombre_extendido,
                                idioma_nombre_extendido);
                    } else {
                        r1 = crearRecursoSoloNombre(model, nombre_extendido);

                    }
                }
            }
            // Añadir al modelo
            Literal p = model.createTypedLiteral(punto,
                    "http://www.opengis.net/ont/geosparql#wktLiteral");
            Resource rLabel = model.createResource();
            model.createResource(puntoURI)
                    // Modelo completo
                    .addProperty(
                            entDef.geo_geometry,
                            model.createResource().addLiteral(
                                    entDef.geosparql_asWKT, p))
                    // PREFERLABEL
                    .addProperty(SKOSXL.labelRelation, rLabel)
                    .addProperty(SKOSXL.literalForm,
                            codigo_ine + "," + codigo_ngbe + "," + hojamtn_25);

            rLabel.addProperty(SKOSXL.prefLabel, r1);

            Resource r2 = null;
            int r2Vacio = 0;
            // CREAR RECURSO ALTLABEL alternativo_2
            if (!nombre_alternativo_2.isEmpty()) {
                if (!fuente_alternativo_2.isEmpty()) {
                    if (!idioma_alternativo_2.isEmpty()) {
                        r2 = crearRecursoCompleto(model, nombre_alternativo_2,
                                fuente_alternativo_2, idioma_alternativo_2);
                    } else {
                        r2 = crearRecursoSinIdioma(model, nombre_alternativo_2,
                                fuente_alternativo_2);

                    }
                } else {
                    if (!idioma_alternativo_2.isEmpty()) {
                        r2 = crearRecursoSinFuente(model, nombre_alternativo_2,
                                idioma_alternativo_2);
                    } else {
                        r2 = crearRecursoSoloNombre(model, nombre_alternativo_2);

                    }
                }
            } else {
                r2Vacio = 1;
            }
            // Añadir al modelo
            if (r2Vacio != 1)
                rLabel.addProperty(SKOSXL.altLabel, r2);
            Resource r3 = null;
            int r3Vacio = 0;
            // CREAR RECURSO ALTLABEL alternativo_3
            if (!nombre_alternativo_3.isEmpty()) {
                if (!fuente_alternativo_3.isEmpty()) {
                    if (!idioma_alternativo_3.isEmpty()) {
                        r3 = crearRecursoCompleto(model, nombre_alternativo_3,
                                fuente_alternativo_3, idioma_alternativo_3);
                    } else {
                        r3 = crearRecursoSinIdioma(model, nombre_alternativo_3,
                                fuente_alternativo_3);

                    }
                } else {
                    if (!idioma_alternativo_3.isEmpty()) {
                        r3 = crearRecursoSinFuente(model, nombre_alternativo_3,
                                idioma_alternativo_3);
                    } else {
                        r3 = crearRecursoSoloNombre(model, nombre_alternativo_3);

                    }
                }
            } else {
                r3Vacio = 1;
            }
            // Añadir al modelo
            if (r3Vacio != 1)
                rLabel.addProperty(SKOSXL.altLabel, r3);
            Resource r4 = null;
            int r4Vacio = 0;
            // CREAR RECURSO ALTLABEL variante_1
            if (!nombre_variante_1.isEmpty()) {
                if (!fuente_variante_1.isEmpty()) {
                    if (!idioma_variante_1.isEmpty()) {
                        r4 = crearRecursoCompleto(model, nombre_variante_1,
                                fuente_variante_1, idioma_variante_1);
                    } else {
                        r4 = crearRecursoSinIdioma(model, nombre_variante_1,
                                fuente_variante_1);

                    }
                } else {
                    if (!idioma_variante_1.isEmpty()) {
                        r4 = crearRecursoSinFuente(model, nombre_variante_1,
                                idioma_variante_1);
                    } else {
                        r4 = crearRecursoSoloNombre(model, nombre_variante_1);

                    }
                }
            } else {
                r4Vacio = 1;
            }
            // Añadir al modelo
            if (r4Vacio != 1)
                rLabel.addProperty(SKOSXL.hiddenLabel, r4);
            Resource r5 = null;
            int r5Vacio = 0;
            // CREAR RECURSO ALTLABEL variante_2
            if (!nombre_variante_2.isEmpty()) {
                if (!fuente_variante_2.isEmpty()) {
                    if (!idioma_variante_2.isEmpty()) {
                        r5 = crearRecursoCompleto(model, nombre_variante_2,
                                fuente_variante_2, idioma_variante_2);
                    } else {
                        r5 = crearRecursoSinIdioma(model, nombre_variante_2,
                                fuente_variante_2);

                    }
                } else {
                    if (!idioma_variante_2.isEmpty()) {
                        r5 = crearRecursoSinFuente(model, nombre_variante_2,
                                idioma_variante_2);
                    } else {
                        r5 = crearRecursoSoloNombre(model, nombre_variante_2);

                    }
                }
            } else {
                r5Vacio = 1;
            }
            // Añadir al modelo
            if (r5Vacio != 1)
                rLabel.addProperty(SKOSXL.hiddenLabel, r5);
            Resource r6 = null;
            int r6Vacio = 0;
            // CREAR RECURSO ALTLABEL variante_3
            if (!nombre_variante_3.isEmpty()) {
                if (!fuente_variante_3.isEmpty()) {
                    if (!idioma_variante_3.isEmpty()) {
                        r6 = crearRecursoCompleto(model, nombre_variante_3,
                                fuente_variante_3, idioma_variante_3);
                    } else {
                        r6 = crearRecursoSinIdioma(model, nombre_variante_3,
                                fuente_variante_3);

                    }
                } else {
                    if (!idioma_variante_3.isEmpty()) {
                        r6 = crearRecursoSinFuente(model, nombre_variante_3,
                                idioma_variante_3);
                    } else {
                        r6 = crearRecursoSoloNombre(model, nombre_variante_3);

                    }
                }
            } else {
                r6Vacio = 1;
            }
            // Añadir al modelo
            if (r6Vacio != 1)
                rLabel.addProperty(SKOSXL.hiddenLabel, r6);
            Resource r7 = null;
            int r7Vacio = 0;
            // CREAR RECURSO ALTLABEL variante_3
            if (!iditneficador_geografico.isEmpty()) {
                if (!fuente_identificador_geografico.isEmpty()) {
                    if (!idioma_identificador_geografico.isEmpty()) {
                        r7 = crearRecursoCompleto(model,
                                iditneficador_geografico,
                                fuente_identificador_geografico,
                                idioma_identificador_geografico);
                    } else {
                        r7 = crearRecursoSinIdioma(model,
                                iditneficador_geografico,
                                fuente_identificador_geografico);

                    }
                } else {
                    if (!idioma_identificador_geografico.isEmpty()) {
                        r7 = crearRecursoSinFuente(model,
                                iditneficador_geografico,
                                idioma_identificador_geografico);
                    } else {
                        r7 = crearRecursoSoloNombre(model,
                                iditneficador_geografico);

                    }
                }
            } else {
                r7Vacio = 1;
            }
            // Añadir al modelo
            if (r7Vacio != 1)
                rLabel.addProperty(SKOSXL.altLabel, r7);
            cont++;
        }

        String fileName = "./src/resources/pruebaTTLfromBD.nt";
        Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileName), "UTF-8"));

        try {
            model.write(out, "NTRIPLES");
            System.out.println("FIN");
        } finally {
            try {
                out.close();
            } catch (IOException closeException) {
                // ignore
            }
        }

    }

    public static Resource crearRecursoCompleto(Model model, String nombre,
                                                String fuente, String idioma) {
        return model.createResource()
                .addProperty(SKOSXL.literalForm, nombre)
                .addProperty(DC.language, "ESP").addProperty(DC.source, "NGBE");

    }

    public static Resource crearRecursoSinFuente(Model model, String nombre,
                                                 String idioma) {
        return model.createResource()
                .addProperty(SKOSXL.literalForm, nombre)
                .addProperty(DC.language, "ESP");

    }

    public static Resource crearRecursoSinIdioma(Model model, String nombre,
                                                 String fuente) {
        return model.createResource()
                .addProperty(SKOSXL.literalForm, nombre)
                .addProperty(DC.source, "NGBE");

    }

    public static Resource crearRecursoSoloNombre(Model model, String nombre) {
        return model.createResource().addProperty(SKOSXL.literalForm,
                nombre);

    }
}
