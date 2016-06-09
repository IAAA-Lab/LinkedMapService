package com.lms.controlador;

import com.lms.persistencia.RDFService;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RDFController {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ApplicationContext ctx;

	// Funcion inicializar la parte RDF Spatial indexa y genera el
	// datasetSpatial
	public static void init() throws IOException {
		RDFService.indexar();
		RDFService
				.crearDatasetSpatial("../rdf/src/resources/TTLfromBDSpatial.ttl");

	}

	// funcion actualizar datos en el servidor RDF
	public static void cargarDataset(InputStream file, Lang lang) {
		String serviceURI = "http://localhost:3030/ds/data";
		DatasetAccessorFactory factory = null;
		DatasetAccessor accessor;
		accessor = factory.createHTTP(serviceURI);
		Model m = ModelFactory.createDefaultModel();
		RDFDataMgr.read(m, file, lang);
		accessor.putModel(m);
	}

	// funcion actualizar datos en el servidor RDF
	public static void cargarDataset(String file) {
		String serviceURI = "http://localhost:3030/ds/data";
		DatasetAccessorFactory factory = null;
		DatasetAccessor accessor;
		accessor = factory.createHTTP(serviceURI);
		Model m = ModelFactory.createDefaultModel();
		RDFDataMgr.read(m, file);
		accessor.putModel(m);
	}

	@RequestMapping("test2")
	public String test2() {
		log.info("Test");
		return "OK";
	}

	/**
	 * Funcion getMap RDF
	 **/
	@RequestMapping(path = "/service/getMap", method = RequestMethod.GET, produces = "application/rdf")
	public synchronized @ResponseBody String getMap(HttpServletRequest request)
			throws IOException {
		// Obtiene el bbox de la peticion
		String[] bbox = request.getParameterValues("BBOX");

		// Obtiene el datasetSpatial
		Dataset spatialDataset = RDFService.getDataset();
		// Genera la petición contra el RDF
		ResultSet results = RDFService.querySpatialBBOX(spatialDataset,
				bbox[0].toString());

		String[] procesarUris;
		String id = "";
		String valores = "";
		ArrayList<String> info = new ArrayList<String>();

		// Si el resultado de la peticion es NO nulo procesa los resultados
		while (results != null && results.hasNext()) {

			procesarUris = results.next().get("?s").toString().split("/");
			id = procesarUris[procesarUris.length - 1];
			valores += "\"{type\": \"Feature\",";
			valores += "\"id\":" + id
					+ ", \"@id\": \"http://localhost/collections/1/features/"
					+ id + "\"," + "\"properties\": {\"name\": ";
			String name = RDFService.queryGetName(id);
			valores += name
					+ "}, \"geometry\": {\"type\": \"Point\", \"coordinates\": [";
			String punto = RDFService.queryGetPunto(id);

			String[] aux = punto.split("T");
			String[] aux2 = aux[1].split(" ");
			String lo = aux2[0].substring(1, aux2[0].length());

			String la = aux2[1].substring(0, aux2[1].length() - 2);
			valores += lo + "," + la + "]}};";
			info.add(valores);

		}

		// Valores contiene el nombre y el punto de las features
		// correspondientes y las
		// pasa al cliente para procesarlas y generar la capa LMS
		return valores;
	}

	/**
	 * Función actualizar almacén RDF
	 */
	@RequestMapping(path = "service/rdf/updateRDF/resource/{id}", method = RequestMethod.PUT)
	public synchronized @ResponseBody String updateRDF(
			@RequestBody String parametros)
			throws UnsupportedEncodingException, FileNotFoundException {
		// Obtener el id del recurso a modificar
		String[] procesar = parametros.split(",");
		String[] campos = procesar[0].split(":");
		String id = campos[1];

		// Peticion de borrado del recurso seleccionado
		RDFService.borrarRecurso(id);
		// Peticion de insercion del recurso seleccionado
		RDFService.insertarRecurso(id, parametros);

		return "update-RDF";

	}

	// Funcion actualizar el datasetSpatia sobre el que se lanzan las consultas
	// espaciales
	@RequestMapping(path = "service/rdf/updateRDFSpatial/resource/{id}", method = RequestMethod.PUT)
	public synchronized @ResponseBody String updateSpatialRDF(
			@RequestBody String parametros)
			throws UnsupportedEncodingException, FileNotFoundException {
		// obtener id del recurso
		String[] procesar = parametros.split(",");
		String[] campos = procesar[0].split(":");
		String id = campos[1];
		// obtener longitud del recurso
		campos = procesar[30].split(":");
		double longitud_etrs89_regcan95 = Double.parseDouble(campos[1]);
		// obtener latitud del recurso
		campos = procesar[31].split(":");
		double latitud_etrs89_regcan95 = Double.parseDouble(campos[1]);
		// update recurso
		RDFService.updateRecursoSpatial(id, parametros);

		return parametros;

	}

	/**
	 * Funcion peticion informacionRDF
	 **/
	@RequestMapping(path = "infoRDF", method = RequestMethod.POST)
	public @ResponseBody String infoRDF(@RequestParam(value = "txtId") String id) {
		String respuesta = "infoRDF";
		ResultSet results = RDFService.queryInfoCompleta(id);
		// Si el resultado de la peticion es NO nulo procesa los resultados
		respuesta = "id:" + id;
		QuerySolution r;
		while (results != null && results.hasNext()) {
			r = results.next();
			respuesta += "punto:" + r.get("?punto") + ";";
			respuesta += "prefLabel:" + r.get("?prefLabel") + ";";
			respuesta += "prefSource:" + r.get("?prefSource") + ";";
			respuesta += "prefLenguaje:" + r.get("?prefLenguaje") + ";";
			respuesta += "altlabel:" + r.get("?altlabel") + ";";
			respuesta += "altSource:" + r.get("?altSource") + ";";
			respuesta += "altLenguaje:" + r.get("?altLenguaje2") + ";";
			respuesta += "hiddenLabel:" + r.get("?hiddenLabel") + ";";
			respuesta += "hiddenSource:" + r.get("?hiddenSource") + ";";
			respuesta += "hiddenLenguaje:" + r.get("?hiddenLenguaje") + ";";
			respuesta += "+";// En caso de que haya mas de un resultado
		}

		return respuesta;
	}

	/**
	 * Funcion peticion informacion recuros
	 **/
	@RequestMapping(path = "informacion", method = RequestMethod.GET)
	public @ResponseBody String informacion(
			@RequestParam(value = "txtId") String id) {
		String respuesta = RDFService.queryDescribe(id);
		return respuesta;
	}

	public void procesarRespuesta(String respuesta) {

	}
}
