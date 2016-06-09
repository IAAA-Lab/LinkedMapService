/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.apache.jena.graph.Node;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Definition of a "document"
 */
public class EntityDefinition {

    public final static Property geosparql_asWKT = ResourceFactory
            .createProperty("http://www.opengis.net/ont/geosparql#asWKT");
    public final static Resource geosparql_wktLiteral = ResourceFactory
            .createResource("http://www.opengis.net/ont/geosparql#wktLiteral");
    private final static String geo_ns = "http://www.opengis.net/ont/geosparql#";
    public final static Property geo_geometry = ResourceFactory
            .createProperty(geo_ns + "geometry");
    private final static String ogc = "http://www.w3.org/2003/01/geo/wgs84_pos#";
    public final static Property geo_latitude = ResourceFactory
            .createProperty(ogc + "lat");
    public final static Property geo_longitude = ResourceFactory
            .createProperty(ogc + "long");
    private final String entityField;
    private final String geoField;
    private final Set<Node> WKTPredicates;
    private final Set<Node> builtinWKTPredicates;

    /**
     * @param entityField The entity being indexed (e.g. it's URI).
     */
    public EntityDefinition(String entityField, String geoField) {
        this.entityField = entityField == null || entityField.isEmpty() ? "entityField"
                : entityField;
        this.geoField = geoField == null || geoField.isEmpty() ? "geoField"
                : geoField;
        this.WKTPredicates = new HashSet<Node>();
        this.builtinWKTPredicates = new HashSet<Node>();
        initBuiltinPredicates();
    }


    private void initBuiltinPredicates() {
        addBuiltinWKTPredicate(geo_geometry);
        addBuiltinWKTPredicate(geosparql_asWKT);

    }

    private boolean addBuiltinWKTPredicate(Resource predicate) {
        builtinWKTPredicates.add(predicate.asNode());
        return addWKTPredicate(predicate);
    }

    public boolean addWKTPredicate(Resource predicate) {
        return WKTPredicates.add(predicate.asNode());
    }


    public String getEntityField() {
        return entityField;
    }

    public String getGeoField() {
        return geoField;
    }

    public boolean isWKTPredicate(Node predicate) {
        return this.WKTPredicates.contains(predicate);
    }


    public int getCustomWKTPredicateCount() {
        return this.WKTPredicates.size() - builtinWKTPredicates.size();
    }

    public int getWKTPredicateCount() {
        return this.WKTPredicates.size();
    }


    @Override
    public String toString() {
        return entityField + ":" + geoField;

    }
}
