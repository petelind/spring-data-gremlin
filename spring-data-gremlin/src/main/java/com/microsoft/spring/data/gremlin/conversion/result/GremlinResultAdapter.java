/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion.result;

import com.microsoft.spring.data.gremlin.common.Constants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Normalizes Gremlin driver {@link Result} payloads from GraphSON v1/v2/v3 and native
 * {@link Vertex}/{@link Edge} elements into the map shape expected by the result readers.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GremlinResultAdapter {

    public static Map<String, Object> toElementMap(@NonNull Result result) {
        final Object object = result.getObject();

        if (object instanceof Map) {
            @SuppressWarnings("unchecked")
            final Map<String, Object> map = new LinkedHashMap<>((Map<String, Object>) object);
            normalizeProperties(map);
            return map;
        }

        if (object instanceof Vertex) {
            return fromVertex((Vertex) object);
        }

        if (object instanceof Edge) {
            return fromEdge((Edge) object);
        }

        throw new IllegalArgumentException(
                "Unsupported Gremlin result type: " + object.getClass().getName());
    }

    private static Map<String, Object> fromVertex(Vertex vertex) {
        final Map<String, Object> elementMap = new LinkedHashMap<>();
        elementMap.put(Constants.PROPERTY_ID, vertex.id());
        elementMap.put(Constants.PROPERTY_LABEL, vertex.label());
        elementMap.put(Constants.PROPERTY_TYPE, Constants.RESULT_TYPE_VERTEX);

        final Map<String, Object> properties = new LinkedHashMap<>();
        vertex.keys().forEach(key -> {
            final Property<?> property = vertex.property(key);
            if (property.isPresent()) {
                properties.put(key, wrapScalarPropertyValue(property.value()));
            }
        });
        elementMap.put(Constants.PROPERTY_PROPERTIES, properties);
        return elementMap;
    }

    private static Map<String, Object> fromEdge(Edge edge) {
        final Map<String, Object> elementMap = new LinkedHashMap<>();
        elementMap.put(Constants.PROPERTY_ID, edge.id());
        elementMap.put(Constants.PROPERTY_LABEL, edge.label());
        elementMap.put(Constants.PROPERTY_TYPE, Constants.RESULT_TYPE_EDGE);
        elementMap.put(Constants.PROPERTY_OUTV, edge.outVertex().id());
        elementMap.put(Constants.PROPERTY_INV, edge.inVertex().id());

        final Map<String, Object> properties = new LinkedHashMap<>();
        edge.keys().forEach(key -> {
            final Property<?> property = edge.property(key);
            if (property.isPresent()) {
                properties.put(key, property.value());
            }
        });
        elementMap.put(Constants.PROPERTY_PROPERTIES, properties);
        return elementMap;
    }

    private static void normalizeProperties(Map<String, Object> elementMap) {
        if (!elementMap.containsKey(Constants.PROPERTY_PROPERTIES)) {
            return;
        }

        final Object properties = elementMap.get(Constants.PROPERTY_PROPERTIES);
        if (properties == null) {
            return;
        }

        if (!(properties instanceof Map)) {
            return;
        }

        @SuppressWarnings("unchecked")
        final Map<String, Object> propertyMap = (Map<String, Object>) properties;
        final String elementType = elementMap.get(Constants.PROPERTY_TYPE) != null
                ? elementMap.get(Constants.PROPERTY_TYPE).toString()
                : "";

        if (Constants.RESULT_TYPE_EDGE.equals(elementType)) {
            elementMap.put(Constants.PROPERTY_PROPERTIES, flattenEdgeProperties(propertyMap));
        } else {
            elementMap.put(Constants.PROPERTY_PROPERTIES, normalizeVertexProperties(propertyMap));
        }
    }

    private static Map<String, Object> normalizeVertexProperties(Map<String, Object> properties) {
        final Map<String, Object> normalized = new LinkedHashMap<>();

        properties.forEach((key, value) -> {
            if (value instanceof ArrayList) {
                normalized.put(key, value);
            } else if (value instanceof Map) {
                normalized.put(key, wrapPropertyValue(value));
            } else {
                normalized.put(key, wrapScalarPropertyValue(value));
            }
        });

        return normalized;
    }

    private static Map<String, Object> flattenEdgeProperties(Map<String, Object> properties) {
        final Map<String, Object> flattened = new LinkedHashMap<>();

        properties.forEach((key, value) -> {
            if (value instanceof ArrayList) {
                flattened.put(key, extractPropertyValue(value));
            } else if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> propertyMap = (Map<String, Object>) value;
                if (propertyMap.containsKey(Constants.PROPERTY_VALUE)) {
                    flattened.put(key, propertyMap.get(Constants.PROPERTY_VALUE));
                } else {
                    flattened.put(key, value);
                }
            } else {
                flattened.put(key, value);
            }
        });

        return flattened;
    }

    private static ArrayList<LinkedHashMap<String, Object>> wrapScalarPropertyValue(Object value) {
        final ArrayList<LinkedHashMap<String, Object>> values = new ArrayList<>(1);
        final LinkedHashMap<String, Object> entry = new LinkedHashMap<>();
        entry.put(Constants.PROPERTY_VALUE, value);
        values.add(entry);
        return values;
    }

    private static ArrayList<LinkedHashMap<String, Object>> wrapPropertyValue(Object value) {
        if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            final Map<String, Object> propertyMap = (Map<String, Object>) value;
            if (propertyMap.containsKey(Constants.PROPERTY_VALUE)) {
                return wrapScalarPropertyValue(propertyMap.get(Constants.PROPERTY_VALUE));
            }
        }

        return wrapScalarPropertyValue(value);
    }

    private static Object extractPropertyValue(Object value) {
        if (value instanceof ArrayList) {
            @SuppressWarnings("unchecked")
            final ArrayList<Object> values = (ArrayList<Object>) value;
            if (!values.isEmpty() && values.get(0) instanceof Map) {
                @SuppressWarnings("unchecked")
                final Map<String, Object> entry = (Map<String, Object>) values.get(0);
                if (entry.containsKey(Constants.PROPERTY_VALUE)) {
                    return entry.get(Constants.PROPERTY_VALUE);
                }
            }
        }

        return value;
    }
}
