package com.lms.persistencia;

import com.lms.util.Utils;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.query.spatial.EntityDefinition;
import org.apache.jena.query.spatial.SpatialDatasetFactory;
import org.apache.jena.query.spatial.SpatialIndex;
import org.apache.jena.query.spatial.SpatialIndexLucene;
import org.apache.jena.query.spatial.SpatialQuery;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.WebContent;
import org.apache.jena.riot.web.HttpOp;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.SKOSXL;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class RDFService {
	public static final String LUCENE_INDEX_PATH = "target/test/TDBDatasetWithLuceneSpatialIndex";
	public static final File LUCENE_INDEX_DIR = new File(LUCENE_INDEX_PATH);
	public static Dataset spatialDataset = DatasetFactory
			.assemble("spatial-config.ttl");

	public static void indexar() throws IOException {

		// In lucene, "entityField" stores the uri of the subject (e.g. a
		// place),
		// while "geoField" holds the indexed geo data (e.g.
		// latitude/longitude).
		EntityDefinition entDef = new EntityDefinition("entityField",
				"geoField");

		File indexDir = LUCENE_INDEX_DIR;
		// index in File system (or use an in-memory one)
		Directory dir = FSDirectory.open(indexDir);

		Dataset baseDataset = DatasetFactory.create();
		// The baseDataset can be an in-memory or TDB/SDB file based one which
		// contains the geo data. Join together into a dataset.
		spatialDataset = SpatialDatasetFactory.createLucene(baseDataset, dir,
				entDef);

		// custom geo predicates for 1) Latitude/Longitude Format
		Resource lat_1 = ResourceFactory
				.createResource("http://localhost/jena_example/#latitude_1");
		Resource long_1 = ResourceFactory
				.createResource("http://localhost/jena_example/#longitude_1");
		entDef.addSpatialPredicatePair(lat_1, long_1);

		// custom geo predicates for Well Known Text (WKT) Literal
		Resource wkt_1 = ResourceFactory
				.createResource("http://localhost/jena_example/#wkt_1");
		entDef.addWKTPredicate(wkt_1);

	}

	public static void crearDatasetSpatial(InputStream file, Lang lang) {
		// LOAD DATA IN SPATIAL DATASET
		spatialDataset.begin(ReadWrite.WRITE);
		try {
			Model m = spatialDataset.getDefaultModel();
			RDFDataMgr.read(m, file, lang);
			spatialDataset.commit();
		} finally {
			spatialDataset.end();
		}
	}

	public static ResultSet querySpatial(Dataset spatialDataset) {
		ResultSet results;

		System.out.println("withinBox");
		String pre = StrUtils
				.strjoinNL(
						"PREFIX : <http://example/>",
						"PREFIX spatial: <http://jena.apache.org/spatial#>",
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
						"PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>",
						"PREFIX geo:   <http://www.opengis.net/ont/geosparql#> ",
						"PREFIX ogc:   <http://www.w3.org/2003/01/geo/wgs84_pos#>",
						"PREFIX geof: <http://www.opengis.net/def/geosparql/function/>");

		String qs = StrUtils.strjoinNL("SELECT * ",
				" {?s spatial:withinBox (39.3 5 45 0 -1) ;",

				" skosxl:literalForm ?name ", "}");

		spatialDataset.begin(ReadWrite.READ);
		try {
			Query q = QueryFactory.create(pre + "\n" + qs);
			QueryExecution qexec = QueryExecutionFactory.create(q,
					spatialDataset);
			results = qexec.execSelect();
		} finally {
			spatialDataset.end();
		}
		return results;

	}

	public static ResultSet querySpatialBBOX(Dataset spatialDataset, String bbox) {

		String[] procesar = bbox.split(",");
		Float xmin = Float.parseFloat(procesar[0]);
		Float xmax = Float.parseFloat(procesar[2]);
		Float ymin = Float.parseFloat(procesar[1]);
		Float ymax = Float.parseFloat(procesar[3]);
		if (xmin < 1 && xmin > -1) {
			xmin = (float) 0;
		}
		if (ymin < 1 && ymin > -1) {
			ymin = (float) 0;
		}
		if (xmax < 1 && xmax > -1) {
			xmax = (float) 0;
		}
		if (ymax < 1 && ymax > -1) {
			ymax = (float) 0;
		}

		ResultSet results = null;

		String pre = StrUtils
				.strjoinNL(
						"PREFIX : <http://example/>",
						"PREFIX spatial: <http://jena.apache.org/spatial#>",
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
						"PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>",
						"PREFIX geo:   <http://www.opengis.net/ont/geosparql#> ",
						"PREFIX ogc:   <http://www.w3.org/2003/01/geo/wgs84_pos#>",
						"PREFIX geof: <http://www.opengis.net/def/geosparql/function/>");

		String qs = StrUtils.strjoinNL("SELECT * ", " {?s spatial:withinBox ("
				+ ymin + " " + xmin + " " + ymax + " " + xmax + ") ;",
				" skosxl:literalForm ?name ", "}");
		spatialDataset.begin(ReadWrite.READ);
		try {
			Query q = QueryFactory.create(pre + "\n" + qs);
			QueryExecution qexec = QueryExecutionFactory.create(q,
					spatialDataset);
			results = qexec.execSelect();
		} finally {
			spatialDataset.end();
		}

		return results;

	}

	public static ResultSet queryInfoCompleta(String id) {
		ResultSet results;

		String pre = StrUtils
				.strjoinNL(
						"PREFIX : <http://example/>",
						"PREFIX spatial: <http://jena.apache.org/spatial#>",
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
						"PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>",
						"PREFIX geo:   <http://www.opengis.net/ont/geosparql#> ",
						"PREFIX ogc:   <http://www.w3.org/2003/01/geo/wgs84_pos#>",
						"PREFIX geof: <http://www.opengis.net/def/geosparql/function/>",
						"PREFIX dc: <http://purl.org/dc/elements/1.1/>");
		String qs = StrUtils
				.strjoinNL("SELECT  ?punto ?prefLabel ?prefSource ?prefLenguaje ?altlabel"
						+ "?altSource ?altLenguaje2 ?hiddenLabel ?hiddenSource ?hiddenLenguaje "
						+ "WHERE {<http://localhost:3030/ds/"
						+ id
						+ "> geo:geometry  ?p ."
						+ "?p geo:asWKT ?punto ."
						+ "<http://localhost:3030/ds/"
						+ id
						+ "> skosxl:labelRelation  ?g ."
						+ "?g skosxl:prefLabel ?pl ."
						+ "?pl skosxl:literalForm ?prefLabel ."
						+ "optional {?pl dc:source ?prefSource .}"
						+ "optional{?pl dc:language ?prefLenguaje}"
						+ "optional{  ?g skosxl:altLabel  ?g2 ."
						+ "?g2 skosxl:literalForm ?altlabel ."
						+ "optional {?g2 dc:source ?altSource .}"
						+ "optional{?g2 dc:language ?altLenguaje2}}"
						+ "optional{  ?g2 skosxl:hiddenLabel  ?g3 ."
						+ "?g3 skosxl:literalForm ?hiddenLabel ."
						+ "optional {?g3 dc:source ?hiddenSource .}"
						+ "optional{?g3 dc:language ?hiddenLenguaje}}}");

		// Fuseki
		String uri = "http://localhost:3030/ds/query";
		Query q = QueryFactory.create(pre + "\n" + qs);
		QueryExecution qExe = QueryExecutionFactory.sparqlService(uri, q);
		results = qExe.execSelect();
		return results;

	}

	public static String queryGetName(String id) {

		String pre = StrUtils
				.strjoinNL(
						"PREFIX : <http://example/>",
						"PREFIX spatial: <http://jena.apache.org/spatial#>",
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
						"PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>",
						"PREFIX geo:   <http://www.opengis.net/ont/geosparql#> ",
						"PREFIX ogc:   <http://www.w3.org/2003/01/geo/wgs84_pos#>",
						"PREFIX geof: <http://www.opengis.net/def/geosparql/function/>",
						"PREFIX dc: <http://purl.org/dc/elements/1.1/>");
		String qs = StrUtils.strjoinNL("SELECT  * WHERE {"
				+ "<http://localhost:3030/ds/" + id
				+ "> skosxl:labelRelation  ?g ." + " ?g skosxl:prefLabel ?l ."
				+ "?l skosxl:literalForm ?name .}");

		// Fuseki
		String uri = "http://localhost:3030/ds/query";
		Query q = QueryFactory.create(pre + "\n" + qs);
		QueryExecution qExe = QueryExecutionFactory.sparqlService(uri, q);
		ResultSet results = qExe.execSelect();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ResultSetFormatter.outputAsJSON(outputStream, results);

		// and turn that into a String
		String json = new String(outputStream.toByteArray());

		String[] aux = json.split("\"name\":");
		String[] name = aux[1].split("\"value\":");
		String[] nombre = name[1].split("}");
		return nombre[0];

	}

	public static String queryGetPunto(String id) {
		String pre = StrUtils
				.strjoinNL(
						"PREFIX : <http://example/>",
						"PREFIX spatial: <http://jena.apache.org/spatial#>",
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
						"PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>",
						"PREFIX geo:   <http://www.opengis.net/ont/geosparql#> ",
						"PREFIX ogc:   <http://www.w3.org/2003/01/geo/wgs84_pos#>",
						"PREFIX geof: <http://www.opengis.net/def/geosparql/function/>",
						"PREFIX dc: <http://purl.org/dc/elements/1.1/>");
		String qs = StrUtils.strjoinNL("SELECT  * WHERE {"
				+ "<http://localhost:3030/ds/" + id + "> geo:geometry  ?p ."
				+ "?p geo:asWKT ?punto ." + "}");

		// Fuseki
		String uri = "http://localhost:3030/ds/query";
		Query q = QueryFactory.create(pre + "\n" + qs);
		QueryExecution qExe = QueryExecutionFactory.sparqlService(uri, q);
		ResultSet results = qExe.execSelect();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ResultSetFormatter.outputAsJSON(outputStream, results);

		// and turn that into a String
		String json = new String(outputStream.toByteArray());
		String[] aux = json.split("\"punto\":");
		String[] name = aux[1].split("\"value\":");
		String[] nombre = name[1].split("}");
		return nombre[0];

	}

	public static void destroy(Dataset spatialDataset) {

		SpatialIndex index = (SpatialIndex) spatialDataset.getContext().get(
				SpatialQuery.spatialIndex);
		if (index instanceof SpatialIndexLucene) {
			deleteOldFiles(LUCENE_INDEX_DIR);

		}
	}

	public static void deleteOldFiles(File indexDir) {
		if (indexDir.exists())
			emptyAndDeleteDirectory(indexDir);
	}

	public static void emptyAndDeleteDirectory(File dir) {
		File[] contents = dir.listFiles();
		if (contents != null) {
			for (File content : contents) {
				if (content.isDirectory()) {
					emptyAndDeleteDirectory(content);
				} else {
					content.delete();
				}
			}
		}
		dir.delete();
	}

	public static void borrarRecurso(String id) {
		// query eliminar recurso
		String qs = StrUtils
				.strjoinNL("PREFIX spatial: <http://jena.apache.org/spatial#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
						+ "PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>"
						+ "PREFIX geo:   <http://www.opengis.net/ont/geosparql#>"
						+ "PREFIX ogc:   <http://www.w3.org/2003/01/geo/wgs84_pos#>"
						+ "PREFIX geof: <http://www.opengis.net/def/geosparql/function/>"
						+ "DELETE { ?x ?p ?o } WHERE { <http://localhost:3030/ds/"
						+ id
						+ ">  ?q ?n1. ?n1 ?p1 ?x . ?x ?p ?o .} ;"
						+ " DELETE { ?x ?p ?o } WHERE {<http://localhost:3030/ds/"
						+ id + ">   ?q ?x. ?x ?p ?o .} ;"
						+ " DELETE WHERE { <http://localhost:3030/ds/" + id
						+ ">   ?q ?x. }");
		// endpoin Fuseki
		String end_point_update = "http://localhost:3030/ds/update";
		// preparar query
		String querypart = "update=" + qs.toString();
		// Ejecutar peticion
		HttpOp.execHttpPost(end_point_update, WebContent.contentTypeHTMLForm,
				querypart);// request.toString());

	}

	public static void insertarRecurso(String id, String propiedades)
			throws UnsupportedEncodingException, FileNotFoundException {
		// Formato del string de entrada propiedades
		// propiedades =
		// "id:2198920.00,nombre_extendido:\"Cerro de los Cardos\",HUSO_ETRS89_REGCAN95:30,geo:\"\",identificador_geografico:\"Cerro de los Cardos\",nombre_alternativo_2:,nombre_alternativo_3:,nombre_variante_1:,nombre_variante_2:,nombre_variante_3:,fuente_nombre_extendido:\"BTN25\",fuente_identificador_geografico:\"BTN25\",fuente_alternativo_2:,fuente_alternativo_3:,fuente_variante_1:,fuente_variante_2:,fuente_variante_3:,idioma_nombre_extendido:,idioma_identificador_geografico:,idioma_alternativo_2:,idioma_alternativo_3:,idioma_variante_1:,idioma_variante_2:,idioma_variante_3:,estatus_nombre_extendido:,SISTEMA_REFERENCIA_ETRS89_REGCAN95:\"ETRS89\",HOJAMTN_25:\"0730c2\",CODIGO_INE:\"10165\",CODIGO_NGBE:\"4.1.2\",crs:3857,LONGITUD_ETRS89_REGCAN95:-6.00,LATITUD_ETRS89_REGCAN95:39.28,XUTM_ETRS89_REGCAN95:240407.08,YUTM_ETRS89_REGCAN95:4353134.19";

		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP("http://localhost:3030/ds/data");
		// Download the updated model
		Model m = accessor.getModel();
		m.setNsPrefix("ogc", "http://www.w3.org/2003/01/geo/wgs84_pos#");
		m.setNsPrefix("geo", "http://www.opengis.net/ont/geosparql#");
		m.setNsPrefix("skosxl", "http://www.w3.org/2008/05/skos-xl#");
		m.setNsPrefix("dc", "http://purl.org/dc/elements/1.1/");
		Resource r = m.createResource("http://localhost:3030/ds/" + id);

		RDFRepository.crearRecursoNew(m, propiedades, r);
		accessor.putModel(m);
	}

	public static Dataset getDataset() {
		return spatialDataset;
	}

	public static void borrarRecursoSpatial(String id) {
		// Cargamos spatialDataset en un modelo
		Model m = spatialDataset.getDefaultModel();

		// Eliminamos el recurso seleccionado
		String uri = "http://localhost:3030/ds" + id;
		Resource s = m.createResource(uri);
		m.removeAll(s, null, (RDFNode) null);

		// Actualizar spatialDataset sin el recurso seleccionado
		spatialDataset.replaceNamedModel("spatialDataset", m);
	}

	public static void insertarRecursoSpatial(String id, String parametros) { // insertar
		String[] procesar = parametros.split(",");
		String iditneficador_geografico = Utils.getString(procesar[4]);
		String[] campos = procesar[30].split(":");
		Double longitud_etrs89_regcan95 = Double.parseDouble(campos[1]);

		campos = procesar[31].split(":");
		Double latitud_etrs89_regcan95 = Double.parseDouble(campos[1]);

		// Download the updated model
		Model m = spatialDataset.getDefaultModel();

		RDFRepository.crearRecursoSpatial(m, id, iditneficador_geografico,
				longitud_etrs89_regcan95, latitud_etrs89_regcan95);
		// Actualizar spatialDataset con el recurso seleccionado
		spatialDataset.replaceNamedModel("spatialDataset", m);
	}

	public static void updateRecursoSpatial(String id, String parametros) { // insertar

		// Cargamso spatialDataset en un modelo
		Model m = spatialDataset.getDefaultModel();

		// Eliminamos el recurso seleccionado
		String uri = "http://localhost:3030/ds" + id;
		Resource s = m.createResource(uri);
		m.removeAll(s, null, (RDFNode) null);
		// Proceamos parametros
		String[] procesar = parametros.split(",");
		String iditneficador_geografico = Utils.getString(procesar[4]);
		String[] campos = procesar[30].split(":");
		Double longitud_etrs89_regcan95 = Double.parseDouble(campos[1]);

		campos = procesar[31].split(":");
		Double latitud_etrs89_regcan95 = Double.parseDouble(campos[1]);

		RDFRepository.crearRecursoSpatial(m, id, iditneficador_geografico,
				longitud_etrs89_regcan95, latitud_etrs89_regcan95);
		// Actualizar spatialDataset con el recurso seleccionado
		spatialDataset.replaceNamedModel("spatialDataset", m);

	}

	public static void crearDatasetSpatial(String file) {
		// TODO Auto-generated method stub
		spatialDataset.begin(ReadWrite.WRITE);
		try {
			Model m = spatialDataset.getDefaultModel();
			RDFDataMgr.read(m, file);
			spatialDataset.commit();
		} finally {
			spatialDataset.end();
		}
	}

	// Funcion PROCESAR QUERY dESCRIBE
	public static String queryDescribe(String id) {
		String pre = StrUtils
				.strjoinNL(
						"PREFIX : <http://example/>",
						"PREFIX spatial: <http://jena.apache.org/spatial#>",
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
						"PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>",
						"PREFIX geo:   <http://www.opengis.net/ont/geosparql#> ",
						"PREFIX ogc:   <http://www.w3.org/2003/01/geo/wgs84_pos#>",
						"PREFIX geof: <http://www.opengis.net/def/geosparql/function/>",
						"PREFIX dc: <http://purl.org/dc/elements/1.1/>");
		// QUERY
		String qs = StrUtils.strjoinNL("describe <http://localhost:3030/ds/"
				+ id + ">");

		// Fuseki
		String uri = "http://localhost:3030/ds/query";
		Query q = QueryFactory.create(pre + "\n" + qs);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(uri, q);
		Model m = qexec.execDescribe();

		// Variables
		String nombre_extendido = "", fuente_nombre_extendido = "", idioma_nombre_extendido = "";
		String n_alternativo = "", fuente_n_alternativo_extendido = "", idioma_n_alternativo_extendido = "";
		String n_variante = "", fuente_n_variante_extendido = "", idioma_n_variante_extendido = "";
		String punto = "", codigo_ine = "", codigo_ngbe = "", hoja = "";
		// Leer prefLabel
		Resource r = m.getResource("http://localhost:3030/ds/" + id);
		com.lms.persistencia.EntityDefinition entDef = new com.lms.persistencia.EntityDefinition(
				"entityField", "geoField");

		Resource geometry = r.getPropertyResourceValue(entDef.geo_geometry);

		// geo:asWKT
		punto = procesarDescribeString(geometry.getProperty(
				entDef.geosparql_asWKT).toString());

		punto = punto.substring(0, 20);// Solo selecciona el punto elimina la
		// parte que especifica el literal

		Resource labelRealtion = r
				.getPropertyResourceValue(SKOSXL.labelRelation);

		// PrefLabel
		if (labelRealtion.hasProperty(SKOSXL.prefLabel)) {
			Resource nombre = labelRealtion
					.getPropertyResourceValue(SKOSXL.prefLabel);
			if (nombre.hasProperty(SKOSXL.literalForm)) {
				nombre_extendido = procesarDescribeString(nombre.getProperty(
						SKOSXL.literalForm).toString());
			}
			if (nombre.hasProperty(DC.source)) {
				fuente_nombre_extendido = procesarDescribeString(nombre
						.getProperty(DC.source).toString());
			}
			if (nombre.hasProperty(DC.language)) {
				idioma_nombre_extendido = procesarDescribeString(nombre
						.getProperty(DC.language).toString());
			}
		}

		// AltLabel
		if (labelRealtion.hasProperty(SKOSXL.altLabel)) {
			Resource nombre_alternativo = labelRealtion
					.getPropertyResourceValue(SKOSXL.altLabel);
			if (nombre_alternativo.hasProperty(SKOSXL.literalForm)) {
				n_alternativo = procesarDescribeString(nombre_alternativo
						.getProperty(SKOSXL.literalForm).toString());
			}
			if (nombre_alternativo.hasProperty(DC.source)) {
				fuente_n_alternativo_extendido = procesarDescribeString(nombre_alternativo
						.getProperty(DC.source).toString());
			}
			if (nombre_alternativo.hasProperty(DC.language)) {
				idioma_n_alternativo_extendido = procesarDescribeString(nombre_alternativo
						.getProperty(DC.language).toString());
			}
		}

		// HiddenLabel
		if (labelRealtion.hasProperty(SKOSXL.hiddenLabel)) {
			Resource nombre_variante = labelRealtion
					.getPropertyResourceValue(SKOSXL.prefLabel);
			if (nombre_variante.hasProperty(SKOSXL.literalForm)) {
				n_variante = procesarDescribeString(nombre_variante
						.getProperty(SKOSXL.literalForm).toString());
			}
			if (nombre_variante.hasProperty(DC.source)) {
				fuente_n_variante_extendido = procesarDescribeString(nombre_variante
						.getProperty(DC.source).toString());
			}
			if (nombre_variante.hasProperty(DC.language)) {
				idioma_n_variante_extendido = procesarDescribeString(nombre_variante
						.getProperty(DC.language).toString());
			}
		}

		// Procesar codigo_ine,codigo_ngbe, hojatm_25
		String aux = r.getProperty(SKOSXL.literalForm).toString();
		String[] tratar = aux.split(",");
		codigo_ine = tratar[2];
		codigo_ngbe = tratar[3];
		hoja = tratar[4].substring(0, tratar[4].length() - 1);

		if (n_alternativo == null) {
			n_alternativo = "";
			fuente_n_alternativo_extendido = "";
			idioma_n_alternativo_extendido = "";
		}
		if (n_variante == null) {
			n_variante = "";
			fuente_n_variante_extendido = "";
			idioma_n_variante_extendido = "";
		}

		String info = "{\"@id\":\"http://localhost/collections/1/features/"
				+ id + "\",\"@type\":\"Point\"," + "\"punto\":" + punto
				+ ") ,\"nombre_extendido\":" + nombre_extendido
				+ ", \"fuente_nombre_extendido\":" + fuente_nombre_extendido
				+ ", \"idioma_nombre_extendido\":" + idioma_nombre_extendido
				+ ", \"n_alternativo\":" + n_alternativo
				+ ", \"fuente_n_alternativo_extendido\":"
				+ fuente_n_alternativo_extendido
				+ ", \"idioma_n_alternativo_extendido\":"
				+ idioma_n_alternativo_extendido + ", \"n_variante\":"
				+ n_variante + ", \"fuente_n_variante_extendido\":"
				+ fuente_n_variante_extendido
				+ ", \"idioma_n_variante_extendido\":"
				+ idioma_n_variante_extendido + ", \"codigo_ine\":"
				+ codigo_ine + ", \"codigo_ngbe\":" + codigo_ngbe
				+ ", \"hoja\":" + hoja + "}";

		return info;

	}

	private static String procesarDescribeString(String propiedad) {
		String[] procesar = propiedad.split(",");
		String resultado = procesar[2].substring(0, procesar[2].length() - 1);
		return resultado;
	}

	// INFO MIN PARA LAS FEATURES DE LA CAPA DEL MAPA
	public ResultSet queryInfoMapa(String id) {
		String pre = StrUtils
				.strjoinNL(
						"PREFIX : <http://example/>",
						"PREFIX spatial: <http://jena.apache.org/spatial#>",
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
						"PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>",
						"PREFIX geo:   <http://www.opengis.net/ont/geosparql#> ",
						"PREFIX ogc:   <http://www.w3.org/2003/01/geo/wgs84_pos#>",
						"PREFIX geof: <http://www.opengis.net/def/geosparql/function/>",
						"PREFIX dc: <http://purl.org/dc/elements/1.1/>");
		String qs = StrUtils.strjoinNL("SELECT  * WHERE {"
				+ "<http://localhost:3030/ds/" + id + "> geo:geometry  ?p ."
				+ "?p geo:asWKT ?punto ." + "<http://localhost:3030/ds/" + id
				+ "> skosxl:prefLabel  ?g ."
				+ "?g skosxl:literalForm ?prefLabel ." + "}");

		// Fuseki
		String uri = "http://localhost:3030/ds/query";
		Query q = QueryFactory.create(pre + "\n" + qs);
		QueryExecution qExe = QueryExecutionFactory.sparqlService(uri, q);
		ResultSet results = qExe.execSelect();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		ResultSetFormatter.outputAsJSON(outputStream, results);

		// and turn that into a String
		String json = new String(outputStream.toByteArray());

		return results;

	}

}
