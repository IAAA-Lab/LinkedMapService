/*CONTROLAR VISUALIZACION DE INFO MINIMA DE LA FEATURE */
var highlightStyleCache = {};

var featureOverlay = new ol.layer.Vector({
    source: new ol.source.Vector(),
    map: map,
    style: function (feature, resolution) {
        var text = resolution < 5000 ? feature.get('name') : '';
        if (!highlightStyleCache[text]) {
            highlightStyleCache[text] = [new ol.style.Style({
                stroke: new ol.style.Stroke({
                    color: '#f00',
                    width: 1
                }),
                fill: new ol.style.Fill({
                    color: 'rgba(255,0,0,0.1)'
                }),
                text: new ol.style.Text({
                    font: '12px Calibri,sans-serif',
                    text: text,
                    fill: new ol.style.Fill({
                        color: '#000'
                    }),
                    stroke: new ol.style.Stroke({
                        color: '#f00',
                        width: 3
                    })
                })
            })];
        }
        return highlightStyleCache[text];
    }
});

var highlight;
var displayFeatureInfo = function (pixel) {

    var feature = map.forEachFeatureAtPixel(pixel, function (feature, layer) {
        return feature;
    });

    var info = document.getElementById('info');
    if (feature) {
        info.innerHTML = feature.getId() + ': ' + feature.get('name');
    } else {
        info.innerHTML = '&nbsp;';
    }

    if (feature !== highlight) {
        if (highlight) {
            featureOverlay.getSource().removeFeature(highlight);
        }
        if (feature) {
            featureOverlay.getSource().addFeature(feature);
        }
        highlight = feature;
    }

};
/**
 *CONTROLAR POP-UP CON INFORMACION MINIMA.
 */
map.on('pointermove', function (evt) {
    if (evt.dragging) {
        return;
    }
    var coordinate = evt.coordinate;
    var pixel = map.getEventPixel(evt.originalEvent);

    var feature = map.forEachFeatureAtPixel(pixel, function (feature, layer) {
        return feature;
    });

    if (feature) {
        content.innerHTML =
            'ID: ' + feature.getId() + '\n NOMBRE: ' + feature.get('name');
        ;
    } else {
        content.innerHTML = '&nbsp;';
    }

    if (feature) {
        overlay.setPosition(coordinate);
    }
    else {
        overlay.setPosition(undefined);
        closer.blur();
    }
});
/*Doble click select feature editable por campos + info full */ /*TERMINAR LA FUNCION*/
var huso29Projection = new ol.proj.Projection({
    code: 'EPSG:25829',
    // The extent is used to determine zoom level 0. Recommended values for a
    // projection's validity extent can be found at http://epsg.io/.
    extent: [276874.0776, 5337325.4505, 723125.9224, 6655205.4835],
    units: 'm'
});
ol.proj.addProjection(huso29Projection);
map.on('dblclick', function (evt) {
    var pixel = map.getEventPixel(evt.originalEvent);
    var feature = map.forEachFeatureAtPixel(pixel, function (feature, layer) {
        return feature;
    });

    var txtId = document.getElementById("txtId");
    var txtName = document.getElementById("txtName");
    var txtHuso = 30;
    var longitud = document.getElementById("longitud");
    var latitud = document.getElementById("latitud");
    var txtX = document.getElementById("txtX");
    var txtY = document.getElementById("txtY");
    

    txtId.value = feature.get('id');
    txtName.value = feature.get('name');
    txtHuso.value = feature.get('huso');

    var geom = feature.getGeometry().getExtent();



    var coor5 = ol.extent.getBottomLeft(geom);
    var bbox2 = String(coor5).split(',');
    txtX.value = bbox2[0];
    txtY.value = bbox2[1];
    // si get('geometry') funciona pero desaparece el punto al hacer dbclick
    var feature2 = feature.clone(); //clono la feature original para q no sufra cambios en el
                                    // trasnform
    var geom2 = feature2.getGeometry();
    var type = geom2.getType();

    var coor2 = ol.proj.transform([txtX.value, txtY.value], 'EPSG:3857', 'EPSG:4326');
    var bbox = String(coor2).split(',');
    longitud.value = coor2[0];
    latitud.value = coor2[1];

    //geom2.transform('EPSG:4326','EPSG:3857');//vuelve a dejar como esta para que
    //la feature no se desplace

   
});
function changeLonLat() {
    //Modificas Lon-Lat y obtiene X-Y correspondiente
    var longitud = document.getElementById("longitud");
    var latitud = document.getElementById("latitud");
    var txtX = document.getElementById("txtX");
    var txtY = document.getElementById("txtY");

    var lon = parseFloat(longitud.value);
    var lat = parseFloat(latitud.value);
    var x = parseFloat(txtX.value);
    var y = parseFloat(txtY.value);

    var coor2 = [lon, lat];
    var coor3 = [x, y];

    var coor4 = ol.proj.transform(coor2, 'EPSG:4326', 'EPSG:3857');
    txtX.value = coor4[0];
    txtY.value = coor4[1];
}
function changeXY() {
    //Modificadas X-Y y obtiene la LON-LAT correspondiente
    var longitud = document.getElementById("longitud");
    var latitud = document.getElementById("latitud");
    var txtX = document.getElementById("txtX");
    var txtY = document.getElementById("txtY");

    var lon = parseFloat(longitud.value);
    var lat = parseFloat(latitud.value);
    var x = parseFloat(txtX.value);
    var y = parseFloat(txtY.value);

    var coor2 = [lon, lat];
    var coor3 = [x, y];

    var coor = ol.proj.transform(coor3, 'EPSG:3857', 'EPSG:4326');
    longitud.value = coor[0];
    latitud.value = coor[1];
}

// BOTON EDITAR
function editarFeature() {
    var txtId = document.getElementById("txtId");
    var txtName = document.getElementById("txtName");
    var txtHuso = document.getElementById("txtHuso");
    var longitud = document.getElementById("longitud");
    var latitud = document.getElementById("latitud");
    var txtX = document.getElementById("txtX");
    var txtY = document.getElementById("txtY");

    //Codigo para añadir la feature modificada en otro color
    var lon = parseFloat(longitud.value);
    var lat = parseFloat(latitud.value);
    var x = parseFloat(txtX.value);
    var y = parseFloat(txtY.value);
    var coor2 = [lon, lat];
    var coor3 = [x, y];

    //Creo el punto con X-Y editado
    var point = new ol.geom.Point([txtX.value, txtY.value]);

    //Crear Feature Modificada

    var featurething = new ol.Feature({  //Añadir los registros q faltan (2-32) pero tener
        id: txtId.value,			 //en cuenta que no se tienen que visualizar en el 
        name: txtName.value,		 //pop-up de información minima.Solo almacenarlos para
        huso: txtHuso,		 //cuando se ejecute la funcion "Actualizar"
        geometry: point,
        identificador_geografico: document.getElementById("2").value,
        nombre_alternativo_2: document.getElementById("3").value,
        nombre_alternativo_3: document.getElementById("4").value,
        nombre_variante_1: document.getElementById("5").value,
        nombre_variante_2: document.getElementById("6").value,
        nombre_variante_3: document.getElementById("7").value,
        fuente_nombre_extendido: document.getElementById("8").value,
        fuente_identificador_geografico: document.getElementById("9").value,
        fuente_alternativo_2: document.getElementById("10").value,
        fuente_alternativo_3: document.getElementById("11").value,
        fuente_variante_1: document.getElementById("12").value,
        fuente_variante_2: document.getElementById("13").value,
        fuente_variante_3: document.getElementById("14").value,
        idioma_nombre_extendido: document.getElementById("15").value,
        idioma_identificador_geografico: document.getElementById("16").value,
        idioma_alternativo_2: document.getElementById("17").value,
        idioma_alternativo_3: document.getElementById("18").value,
        idioma_variante_1: document.getElementById("19").value,
        idioma_variante_2: document.getElementById("20").value,
        idioma_variante_3: document.getElementById("21").value,
        estatus_nombre_extendido: document.getElementById("22").value,
        sistema_referencia_etrs89_regcan95: document.getElementById("28").value,
        hojamtn_25: document.getElementById("29").value,
        codigo_ine: document.getElementById("30").value,
        codigo_ngbe: document.getElementById("31").value,
        crs: document.getElementById("32").value,
        lon: lon,
        lat: lat,
        x: x,
        y: y
    });

    //Añadir feature modificada al mapa
    //vectorSource.addFeature( featurething );
    featurething.setId(txtId.value);
    //Comprobar que la feature editada no haya sido editada anteriormente
    //sin llegar a actualizar su valor

    var f = editSource.getFeatureById(txtId.value);
    if (f != null) {
        editSource.removeFeature(editSource.getFeatureById(txtId.value));
    }

    //Añadir feature modificada 
    editSource.addFeature(featurething);

   
    //Mensajes de test de salida

  
    

} // FIN EDITAR

//Serializar feature (convertir a json)
function featureTOstring(feature) {
    var keys = [];
    keys = feature.getKeys();

    var json = "{";
    for (j = 0; j < keys.length - 1; j++) {
        json += keys[j] + ":" + feature.get(keys[j]) + ",";
    }
    json += keys[keys.length - 1] + ":" + feature.get(keys[keys.length - 1]);
    json += "}"
    return json;
}
// BOTON ACTUALIZAR
function actualizarFeature() {

    var features = [];
    features = editSource.getFeatures();

    var resul = [];

    for (i = 0; i < features.length; i++) {

        resul[i] = featureTOstring(features[i]);

    }
 
    return resul;
} // FIN ACTUALIZAR
function getIds() {
    var features = [];
    features = editSource.getFeatures();
    var ids = [];
    for (h = 0; h < features.length; h++) {

        ids[h] = features[h].get("id");
    }
    return ids;
}
function update(features, ids) {
    //var uri="";
    for (p = 0; p < features.length; p++) { 
        updateRDF(ids[p], p, features);	//Actualizar RDF
        updateBD(ids[p], p, features);	//Actualiza BD 
    }
  
    editSource.clear();
}
function updateBD(id, num, features) {

    $.ajax("service/bd/resource/" + id, {  //Actualiza BD
        type: "PUT",
        data: features[num],
        contentType: "application/json",
        //cache: false,
        success: function (response) {
            // Acciones a realizar cuando la petición ha terminado con éxito
            document.getElementById("info").value += " bd return";

            document.getElementById("info").value += "1";
            wmsLayer.getSource().updateParams({"time": Date.now()});
         }
    });

}
function updateRDF(id, num, features) {

    $.ajax("service/rdf/updateRDF/resource/" + id, {  //Actualiza RDF
        type: "PUT",
        data: features[num],
        contentType: "application/json",
        success: function (response) {
            // Acciones a realizar cuando la petición ha terminado con éxito
            document.getElementById("info").value += " RDF return";    
        }
    });
//Update modeloSpatial volver a hacer RDFController.init();
    $.ajax("service/rdf/updateRDFSpatial/resource/" + id, {  //Actualiza RDF
        type: "PUT",
        data: features[num],
        contentType: "application/json",
        //cache: false,
        success: function (response) {
            // Acciones a realizar cuando la petición ha terminado con éxito
            document.getElementById("info").value += " RDFSpatial return"; 
          
            //Eliminar feature de la capa
            var f=vectorSource.getFeatureById(id);
          
            vectorSource.removeFeature(f);
           
            //Crear feature modificada
            var procesar = response.split(",");
    		var campos = procesar[1].split(":");
    		var nombre=campos[1];
    		
    		// obtener longitud del recurso
    		campos = procesar[30].split(":");
    		var longitud_etrs89_regcan95 = parseFloat(campos[1]);
    		 
    		// obtener latitud del recurso
    		campos = procesar[31].split(":");
    		var latitud_etrs89_regcan95 = parseFloat(campos[1]);
    		
    		 var coor2 = [longitud_etrs89_regcan95,latitud_etrs89_regcan95];
    		 
    		  var coor4 = ol.proj.transform(coor2,'EPSG:4326', 'EPSG:3857');
    		 
    		  var point = new ol.geom.Point([coor4[0], coor4[1]]);
    		
    		  var featurething = new ol.Feature({  //Añadir los registros q faltan (2-32) pero tener
    		      id: id,			 //en cuenta que no se tienen que visualizar en el 
    		      name: nombre,	 //pop-up de información minima.Solo almacenarlos para
    		      geometry: point
    		  });
    		    
    		  featurething.setId(id);
    		//Añadir feature modificada 
    		  vectorSource.addFeature(featurething);
    		 
    		 
    		  
    		  
        }
    });  
}

function añadirFeatureLMS(valores){
	  //Crear Feature Modificada
	
	 var procesar=valores.split(";");
	   var cont=0;
	   var fs;
	   
	   var cont=0;
	   var length=procesar.length;
	  
	  while(length > cont){
		 
		var prop=procesar[cont].split(",");
	   fs=prop[1].split("id\":");
		
	  
	  
	   var aux2=prop[3].split("name\":");
	 
	   var name=aux2[1].split("}");
	  
	   var aux3=prop[5].split("[");
	  
	
	  var lat= aux3[1];
	  var long= prop[6].split("]");

	  var flong=parseFloat(long[0]);
	  var flat=parseFloat(lat);
	  var coor2 = [flat,flong];
	 
	  var coor4 = ol.proj.transform(coor2,'EPSG:4326', 'EPSG:3857');
	 
	  var point = new ol.geom.Point([coor4[0], coor4[1]]);
	 
	  var featurething = new ol.Feature({  //Añadir los registros q faltan (2-32) pero tener
	      id: fs[1],		 //en cuenta que no se tienen que visualizar en el 
	      name: name[0],	 //pop-up de información minima.Solo almacenarlos para
	      geometry: point
	  });
	  featurething.setId(fs[1]);
	  
	  //Comprobar que la feature editada no haya sido editada anteriormente
	  //sin llegar a actualizar su valor

	  var f = vectorSource.getFeatureById(fs[1]);


	//Añadir feature si no existe en la capa 
	  if (f==null){
	vectorSource.addFeature(featurething);}

	cont++;
	  }
}


