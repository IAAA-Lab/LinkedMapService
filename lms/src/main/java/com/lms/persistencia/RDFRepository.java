package com.lms.persistencia;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.SKOSXL;

import static com.lms.util.Utils.getString;

public class RDFRepository {

	// Funcion seleccion informacion RDF
	public static String queryData(String id) {
		String idURI = "<http://localhost/collections/1/features/" + id + ">";
		String uri = "http://localhost:3030/ds/query";
		idURI = "<http://localhost/collections/1/features/2199131>";
		String qs = StrUtils
				.strjoinNL("SELECT   * WHERE {"
						+ idURI
						+ " <http://www.w3.org/2003/01/geo/wgs84_pos#geometry> ?geom"
						+ " OPTIONAL{?geom <http://www.opengis.net/ont/geosparql#asWKT> ?punto . "
						+ idURI
						+ "<http://www.w3.org/2008/05/skos-xl#prefLabel> ?pl "
						+ "  OPTIONAL{ ?pl <http://www.w3.org/2008/05/skos-xl#literalForm> ?name .}"
						+ " OPTIONAL{?pl <http://www.w3.org/2008/05/dc#source> ?source . }"
						+ " OPTIONAL{ ?pl <http://www.w3.org/2008/05/dc#language> ?language .}"
						+ idURI
						+ " <http://www.w3.org/2008/05/skos-xl#altLabel> ?al"
						+ " OPTIONAL{ ?al <http://www.w3.org/2008/05/skos-xl#literalForm> ?name_a .}"
						+ " OPTIONAL{?al <http://www.w3.org/2008/05/dc#source> ?source_a . }"
						+ " OPTIONAL{ ?al <http://www.w3.org/2008/05/dc#language> ?language_a .}}}");
		Query query = QueryFactory.create(qs);
		QueryExecution qExe = QueryExecutionFactory.sparqlService(uri, query);
		ResultSet results = qExe.execSelect();
		ResultSetFormatter.outputAsJSON(System.out, results);// formato JSON

		return results.toString();
	}

	// Funcion select nombre alternativo
	public static String queryAlternative(String id) {
		String idURI = "<http://localhost/collections/1/features/" + id + ">";
		String uri = "http://localhost:3030/ds/query";
		String qs = StrUtils
				.strjoinNL("SELECT DISTINCT ?punto ?name ?source ?language ?name_alt ?source_alt ?language_alt ?name_hidden ?source_hidden ?language_hidden  WHERE {"
						+ idURI
						+ "<http://www.w3.org/2008/05/skos-xl#altLabel> ?al"
						+ "OPTIONAL{ ?al <http://www.w3.org/2008/05/skos-xl#literalForm> ?name_alt .}"
						+ "OPTIONAL{?al <http://purl.org/dc/elements/1.1/source> ?source_alt . }"
						+ "OPTIONAL{ ?al <http://purl.org/dc/elements/1.1/language> ?language_alt .}}");
		Query query = QueryFactory.create(qs);
		QueryExecution qExe = QueryExecutionFactory.sparqlService(uri, query);
		ResultSet results = qExe.execSelect();
		ResultSetFormatter.outputAsJSON(System.out, results);// formato JSON
		return results.toString();

	}

	// Funcion select nombre hidden
	public static String queryHidden(String id) {
		String idURI = "<http://localhost/collections/1/features/" + id + ">";
		String uri = "http://localhost:3030/ds/query";
		String qs = StrUtils
				.strjoinNL("SELECT DISTINCT ?punto ?name ?source ?language ?name_alt ?source_alt ?language_alt ?name_hidden ?source_hidden ?language_hidden  WHERE {"
						+ idURI
						+ "<http://www.w3.org/2008/05/skos-xl#hiddenLabel> ?al"
						+ "OPTIONAL{ ?al <http://www.w3.org/2008/05/skos-xl#literalForm> ?name_hidden .}"
						+ "OPTIONAL{?al <http://purl.org/dc/elements/1.1/source> ?source_hidden . }"
						+ "OPTIONAL{ ?al <http://purl.org/dc/elements/1.1/language> ?language_hidden .}}");
		Query query = QueryFactory.create(qs);
		QueryExecution qExe = QueryExecutionFactory.sparqlService(uri, query);
		ResultSet results = qExe.execSelect();
		ResultSetFormatter.outputAsJSON(System.out, results);// formato JSON
		return results.toString();

	}

	public static Resource crearRecursoCompleto(Model model, String nombre,
			String fuente, String idioma) {
		Resource r2 = model.createResource()
				.addProperty(SKOSXL.literalForm, nombre)
				.addProperty(DC.language, idioma)
				.addProperty(DC.source, fuente);
		return r2;

	}

	public static Resource crearRecursoSinFuente(Model model, String nombre,
			String idioma) {
		Resource r2 = model.createResource()
				.addProperty(SKOSXL.literalForm, nombre)
				.addProperty(DC.language, idioma);
		return r2;

	}

	public static Resource crearRecursoSinIdioma(Model model, String nombre,
			String fuente) {
		Resource r2 = model.createResource()
				.addProperty(SKOSXL.literalForm, nombre)
				.addProperty(DC.source, fuente);
		return r2;

	}

	public static Resource crearRecursoSoloNombre(Model model, String nombre) {
		Resource r2 = model.createResource().addProperty(SKOSXL.literalForm,
				nombre);
		return r2;

	}

	// Crear recurso
	public static void crearRecursoNew(Model model, String punto, Resource r) {

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

		// Asignar valores
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

		// ASIGNAR VALOR A LAS PROPIEDADES
		String puntoURI = "http://localhost:3030/ds/" + id;
		String name = nombre_extendido;
		String pto = "POINT(" + longitud_etrs89_regcan95 + " "
				+ latitud_etrs89_regcan95 + ")";

		// CREAR RECURSO Y AÑADIR PROPIEDADES
		EntityDefinition entDef = new EntityDefinition("entityField",
				"geoField");
		Resource r1 = null;
		// CREAR RECURSO PREFLABEL
		if (!nombre_extendido.isEmpty()) {
			if (!fuente_nombre_extendido.isEmpty()) {
				if (!idioma_nombre_extendido.isEmpty()) {
					r1 = crearRecursoCompleto(model, nombre_extendido,
							fuente_nombre_extendido, idioma_nombre_extendido);
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
		Literal p = model.createTypedLiteral(pto,
				"http://www.opengis.net/ont/geosparql#wktLiteral");
		Resource rLabel = model.createResource();

		// Modelo completo
		r.addProperty(
				EntityDefinition.geo_geometry,
				model.createResource().addLiteral(
						EntityDefinition.geosparql_asWKT, p))
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
					r7 = crearRecursoCompleto(model, iditneficador_geografico,
							fuente_identificador_geografico,
							idioma_identificador_geografico);
				} else {
					r7 = crearRecursoSinIdioma(model, iditneficador_geografico,
							fuente_identificador_geografico);

				}
			} else {
				if (!idioma_identificador_geografico.isEmpty()) {
					r7 = crearRecursoSinFuente(model, iditneficador_geografico,
							idioma_identificador_geografico);
				} else {
					r7 = crearRecursoSoloNombre(model, iditneficador_geografico);

				}
			}
		} else {
			r7Vacio = 1;
		}
		// Añadir al modelo
		if (r7Vacio != 1)
			rLabel.addProperty(SKOSXL.altLabel, r7);
	}

	public static void crearRecursoSpatial(Model m, String id,
			String iditneficador_geografico, Double longitud_etrs89_regcan95,
			Double latitud_etrs89_regcan95) {
		// Añadir al modelo
		Literal lo = m.createTypedLiteral(new Float(longitud_etrs89_regcan95));
		Literal la = m.createTypedLiteral(new Float(latitud_etrs89_regcan95));
		String puntoURI = "http://localhost/collections/1/features/" + id;
		m.createResource(puntoURI)
				// Propieades para spatial
				.addLiteral(EntityDefinition.geo_longitude, lo)
				.addLiteral(EntityDefinition.geo_latitude, la)
				.addProperty(SKOSXL.literalForm, iditneficador_geografico);
	}

}
