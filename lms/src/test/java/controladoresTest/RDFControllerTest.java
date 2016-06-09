package controladoresTest;

import com.lms.controlador.RDFController;
import com.lms.persistencia.RDFRepository;
import com.lms.persistencia.RDFService;

import org.apache.http.protocol.HttpContext;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.atlas.web.auth.HttpAuthenticator;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.WebContent;
import org.apache.jena.riot.web.HttpOp;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.modify.UpdateProcessRemoteForm;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.apache.jena.sparql.util.Context;
import org.apache.jena.sparql.util.QueryExecUtils;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.FileManager;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class RDFControllerTest {
	@Test
	public void getMapTest() {

	}

	@Test
	public void infoRDFTest() {

	}

	@Test
	public void RDFServiceTest() throws IOException {
		RDFService test = new RDFService();

		test.indexar();
		test.crearDatasetSpatial(RDFController.class.getResourceAsStream("../lms/src/test/resources/modeloSpatial.ttl"), Lang.TURTLE);

		Dataset spatialDataset=test.getDataset();
		ResultSet results = test.querySpatial(spatialDataset);
		
		ArrayList<String> info = new ArrayList<String>();
		System.out.println("test");
		String[] procesarUris;
		String id = "";
		String valores = "";
		if (results==null){
			System.out.println("NULOO");
			}else{System.out.println("NO NULOO");
		while (results.hasNext()) {
			procesarUris = results.next().get("?s").toString().split("/");
			id = procesarUris[procesarUris.length - 1];
			valores = "nombre:" + test.queryGetName(id);
			valores += "punto:" + test.queryGetPunto(id);
			info.add(valores);

		}}

		System.out.println("#########################");
		System.out.println(info.get(0));
		System.out.println(info.get(1));
		System.out.println(info.get(2));
		System.out.println(info.get(3));
		System.out.println(info.get(4));
		System.out.println("===============================");

	
	}

	@Test
	public void selectResource() {
		String idURI = "<http://localhost:3030/ds/2198920>";
		String pre = StrUtils
				.strjoinNL(
						"PREFIX : <http://example/>",
						"PREFIX spatial: <http://jena.apache.org/spatial#>",
						"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>",
						"PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>",
						"PREFIX geo:   <http://www.opengis.net/ont/geosparql#> ",
						"PREFIX ogc:   <http://www.w3.org/2003/01/geo/wgs84_pos#>",
						"PREFIX geof: <http://www.opengis.net/def/geosparql/function/>");

		String qs = StrUtils.strjoinNL("select * where { ?s ?p ?v . " + idURI
				+ " ?p ?v .}");

		try {
			String uri = "http://localhost:3030/ds/query";
			Query q = QueryFactory.create(pre + "\n" + qs);
			QueryExecution qExe = QueryExecutionFactory.sparqlService(uri, q);
			ResultSet results = qExe.execSelect();

			ResultSetFormatter.outputAsJSON(System.out, results);
		} finally {
		}

	}

	@Test
	public void borrarRecurso() {
		String id = "2199609";// ID para realizar el TEST

		String idURI = "<http://localhost:3030/ds/" + id + ">";
		String uri = "http://localhost:3030/ds/update";
		idURI = "<http://localhost:3030/ds/2199212>";

		String qs = StrUtils
				.strjoinNL("PREFIX spatial: <http://jena.apache.org/spatial#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
						+ "PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>"
						+ "PREFIX geo:   <http://www.opengis.net/ont/geosparql#>"
						+ "PREFIX ogc:   <http://www.w3.org/2003/01/geo/wgs84_pos#>"
						+ "PREFIX geof: <http://www.opengis.net/def/geosparql/function/>"
						+
						// " DELETE  {?s ?p ?v } WHERE {?s ?p ?v . <http://localhost:3030/ds/2199501> ?p ?v . };");
						"DELETE { ?x ?p ?o } WHERE { <http://localhost:3030/ds/2198920>  ?q ?n1. ?n1 ?p1 ?x . ?x ?p ?o .} ;"
						+ " DELETE { ?x ?p ?o } WHERE {<http://localhost:3030/ds/2198920>   ?q ?x. ?x ?p ?o .} ;"
						+ " DELETE WHERE { <http://localhost:3030/ds/2198920>   ?q ?x. }");
		String end_point_update = "http://localhost:3030/ds/update";
		String querypart = "update=" + qs.toString(); // escape makes the string
														// ASCII-portable

		HttpOp.execHttpPost(end_point_update, WebContent.contentTypeHTMLForm,
				querypart);

	}

	@Test
	public void insertarRecurso() throws UnsupportedEncodingException,
			FileNotFoundException {
		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP("http://localhost:3030/ds/data");
		// Insertar de prueba
		String propiedades = "id:2198920.00,nombre_extendido:\"Cerro de los Cardos\",HUSO_ETRS89_REGCAN95:30,geo:\"\",identificador_geografico:\"Cerro de los Cardos\",nombre_alternativo_2:,nombre_alternativo_3:,nombre_variante_1:,nombre_variante_2:,nombre_variante_3:,fuente_nombre_extendido:\"BTN25\",fuente_identificador_geografico:\"BTN25\",fuente_alternativo_2:,fuente_alternativo_3:,fuente_variante_1:,fuente_variante_2:,fuente_variante_3:,idioma_nombre_extendido:,idioma_identificador_geografico:,idioma_alternativo_2:,idioma_alternativo_3:,idioma_variante_1:,idioma_variante_2:,idioma_variante_3:,estatus_nombre_extendido:,SISTEMA_REFERENCIA_ETRS89_REGCAN95:\"ETRS89\",HOJAMTN_25:\"0730c2\",CODIGO_INE:\"10165\",CODIGO_NGBE:\"4.1.2\",crs:3857,LONGITUD_ETRS89_REGCAN95:-6.00,LATITUD_ETRS89_REGCAN95:39.28,XUTM_ETRS89_REGCAN95:240407.08,YUTM_ETRS89_REGCAN95:4353134.19";
		// Download the updated model
		Model m = accessor.getModel();
		Resource r = m.createResource("http://localhost:3030/ds/2198920");
		RDFRepository.crearRecursoNew(m, propiedades, r);
		// m.write(System.out,"TTL");
		accessor.putModel(m);
	}

	@Test
	public void updateRDF() {

		// Delete Resource
		String qs = StrUtils
				.strjoinNL("PREFIX spatial: <http://jena.apache.org/spatial#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
						+ "PREFIX skosxl: <http://www.w3.org/2008/05/skos-xl#>"
						+ "PREFIX geo:   <http://www.opengis.net/ont/geosparql#>"
						+ "PREFIX ogc:   <http://www.w3.org/2003/01/geo/wgs84_pos#>"
						+ "PREFIX geof: <http://www.opengis.net/def/geosparql/function/>"
						+ "DELETE { ?x ?p ?o } WHERE { <http://localhost:3030/ds/2198920>  ?q ?n1. ?n1 ?p1 ?x . ?x ?p ?o .} ;"
						+ " DELETE { ?x ?p ?o } WHERE {<http://localhost:3030/ds/2198920>   ?q ?x. ?x ?p ?o .} ;"
						+ " DELETE WHERE { <http://localhost:3030/ds/2198920>   ?q ?x. }");
		String end_point_update = "http://localhost:3030/ds/update";
		String querypart = "update=" + qs.toString();

		HttpOp.execHttpPost(end_point_update, WebContent.contentTypeHTMLForm,
				querypart);// request.toString());

		// Insert Resource
		DatasetAccessor accessor = DatasetAccessorFactory
				.createHTTP("http://localhost:3030/ds/data");
		// Insertar de prueba
		String propiedades = "id:2198920.00,nombre_extendido:\"Cerro de los Cardos\",HUSO_ETRS89_REGCAN95:30,geo:\"\",identificador_geografico:\"Cerro de los Cardos\",nombre_alternativo_2:,nombre_alternativo_3:,nombre_variante_1:,nombre_variante_2:,nombre_variante_3:,fuente_nombre_extendido:\"BTN25\",fuente_identificador_geografico:\"BTN25\",fuente_alternativo_2:,fuente_alternativo_3:,fuente_variante_1:,fuente_variante_2:,fuente_variante_3:,idioma_nombre_extendido:,idioma_identificador_geografico:,idioma_alternativo_2:,idioma_alternativo_3:,idioma_variante_1:,idioma_variante_2:,idioma_variante_3:,estatus_nombre_extendido:,SISTEMA_REFERENCIA_ETRS89_REGCAN95:\"ETRS89\",HOJAMTN_25:\"0730c2\",CODIGO_INE:\"10165\",CODIGO_NGBE:\"4.1.2\",crs:3857,LONGITUD_ETRS89_REGCAN95:-6.00,LATITUD_ETRS89_REGCAN95:39.28,XUTM_ETRS89_REGCAN95:240407.08,YUTM_ETRS89_REGCAN95:4353134.19";
		// Download the updated model
		Model m = accessor.getModel();
		Resource r = m.createResource("http://localhost:3030/ds/2198920");
		RDFRepository.crearRecursoNew(m, propiedades, r);

		// Update modelo server
		accessor.putModel(m);
	}

	@Test
	public void cargarDataset() {

		String serviceURI = "http://localhost:3030/ds/data";
		DatasetAccessorFactory factory = null;
		DatasetAccessor accessor;
		accessor = factory.createHTTP(serviceURI);
		Model m = ModelFactory.createDefaultModel();

		m.read("../lms/src/test/resources/testUris.ttl");

		accessor.putModel(m);
	}
	@Test
	public void actualizarSpatialDataset() throws IOException{
		RDFService.indexar();
		RDFService.crearDatasetSpatial("../lms/src/test/resources/modeloSpatialTestear.ttl");

		
		RDFService.borrarRecursoSpatial("1844860");
	}
	
	@Test
	public void actualizarSpatialDataset2() throws IOException{
		RDFService.indexar();
		RDFService.crearDatasetSpatial("../lms/src/test/resources/modeloSpatialTestear.ttl");

		String id="1844860";
		String propiedades="";
		RDFService.insertarRecursoSpatial(id, propiedades);
	}
	
	@Test
	public void testDescribe(){
		RDFService.queryDescribe("92");
	}
}
