//OpenLayers.ProxyHost = "proxy.cgi?url=";
/*ESTILO VISUALIZACION DE LAS FEATURES EN EL MAPA */
var style = new ol.style.Style({
  fill: new ol.style.Fill({
    color: 'rgba(255, 255, 255, 0.6)'
  }),
  stroke: new ol.style.Stroke({
    color: '#319FD3',
    width: 1
  }),
   image: new ol.style.Circle({
        radius: 6,
        fill: new ol.style.Fill({color: 'rgba(255, 0, 0, 0.1)'}),
        stroke: new ol.style.Stroke({color: 'red', width: 1})
      }),
  text: new ol.style.Text({
    font: '12px Calibri,sans-serif',
    fill: new ol.style.Fill({
      color: '#000'
    }),
    stroke: new ol.style.Stroke({
      color: '#fff',
      width: 3
    })
  })
});

var styles = [style];
var style2= new ol.style.Style({fill: new ol.style.Fill({
    color: 'rgba(255, 255, 255, 0.6)'
}),
stroke: new ol.style.Stroke({
  color: '#319FD3',
  width: 1
}),
 image: new ol.style.Circle({
      radius: 6,
      fill: new ol.style.Fill({color: 'rgba(255, 0, 0, 0.1)'}),
      stroke: new ol.style.Stroke({color: 'blue', width: 1})
    }),
text: new ol.style.Text({
  font: '12px Calibri,sans-serif',
  fill: new ol.style.Fill({
    color: '#000'
  }),
  stroke: new ol.style.Stroke({
    color: '#fff',
    width: 3
  })
})});
var styles2 = [style2];
/* CAPAS DEL SERVICIO*/
var raster = new ol.layer.Tile({
  source: new ol.source.MapQuest({
    layer: 'osm'
  })
});

//Capa LMS
var vectorSource = new ol.source.Vector({
	    format: new ol.format.GeoJSON(),
		 wrapX: false
	});

//Funcion get Features RDF llamada cuando hay peticion WMS
function vectorLoad(src){
	var p=src;
    var bbox=p.split("BBOX="); 
    var coors= bbox[1].split("%2C");
    var minX=parseFloat(coors[0]);
    var minY=parseFloat(coors[1]);
    var maxX=parseFloat(coors[2]);
    var maxY=parseFloat(coors[3]);
;
    
    var coor2=ol.proj.transform([minX,minY],'EPSG:3857', 'EPSG:4326');
    var minLon=coor2[0];
    var minLat=coor2[1];
    coor2=ol.proj.transform([maxX, maxY],'EPSG:3857', 'EPSG:4326');
    var maxLon=coor2[0];
    var maxLat=coor2[1];
    var paramTrans=bbox[0]+"BBOX="+ minLon+","+minLat+","+maxLon+","+maxLat;
     $.ajax({url: '/service/getMap',
			 headers: {          
				 Accept : "application/rdf"        		    
			 },
			 data: paramTrans,
			 }).then(function(response) {
				 if (response!=""){
					 añadirFeatureLMS(response);	
				 }
	    });
 

}

var vectorLayer = new ol.layer.Vector({
  source: vectorSource,
  style: function(feature, resolution) {
   
    return styles;
  }
});

//Capa WMS
wmsSource = new ol.source.TileWMS({
  url:'http://localhost:8080/service/getMap',
	 params: {'LAYERS': 'ngbe'},
	crossOrigin: null,
 	
 	tileLoadFunction: function(imageTile, src) {
 		var dataEntries = src.split("&");
 		  var url;
 		  var params ="";// "&SERVICE=WMS";
 		  for (var i = 0 ; i< dataEntries.length ; i++){
 		      console.log("dataEntries[i]",dataEntries[i]);
 		      if (i===0){
 		      url = dataEntries[i];    
 		      }
 		      else{
 		      params = params + "&"+dataEntries[i];
 		      }
 		  } 
 	url="Http://localhost:8080/service/getMap?"
 		 imageTile.getImage().src=url+params;
 	vectorLoad(params);
 	  }
});


 
var wmsLayer = new ol.layer.Tile({
 source: wmsSource,
 
});


/*Capa vectorial "auxiliar" para la edición de puntos y posterior almacenamiento*/
var editSource = new ol.source.Vector({});
var editarLayer = new ol.layer.Vector({
	 source: editSource,
	 style: function(feature, resolution) {
		   
		    return styles2;
		  }
	});
/*FUNCIONES EDITAR PUNTO INTERACTIVAMENTE*/

var select = new ol.interaction.Select({
  wrapX: false
});

var modify = new ol.interaction.Modify({
	
  features: select.getFeatures()
});

/* ELEMENTOS VISUALIZACION POP-UP */
var container = document.getElementById('popup');
var content = document.getElementById('popup-content');
var closer = document.getElementById('popup-closer');
var infobox=document.getElementById('infobox');

/* CONTROLADOR CERRAR(ESCONDER) POP-UP */
closer.onclick = function() {
  overlay.setPosition(undefined);
  closer.blur();
  return false;
};


/*CONTROLADOR CREAR PANEL POP-UP */
var overlay = new ol.Overlay(/** @type {olx.OverlayOptions} */ ({
  element: container,
  autoPan: true,
  autoPanAnimation: {
    duration: 250
  }
}));


/*CONTROL PRINCIPAL MAPA */

view = new ol.View({
	  center: ol.proj.transform([0, 40], 'EPSG:4326', 'EPSG:3857'),
	  zoom: 5
	});

var map = new ol.Map({
interactions: 														//editar mapa interactivamente
			   ol.interaction.defaults({doubleClickZoom :false}),//.extend([select, modify]),
  layers: [
    raster,
    wmsLayer,
    vectorLayer,
    editarLayer
  ],
  overlays: [overlay],
  target: 'map',
  view: view
});





getUrlFeatureInfo= function (coor){ //coor=[X,Y]
	var viewResolution = /** @type {number} */ (view.getResolution());
	var url = wmsSource.getGetFeatureInfoUrl(
		      coor, viewResolution, 'EPSG:3857',
		      {'INFO_FORMAT': 'text/plain'});
	 
	return url;
}

getURLwmsSource= function(){
	var url=wmsSource.getUrls();
	var params=wmsSource.getProperties()
	 document.getElementById("info").value += url[0];
	return url[0]+params[0]+params[1];
}

