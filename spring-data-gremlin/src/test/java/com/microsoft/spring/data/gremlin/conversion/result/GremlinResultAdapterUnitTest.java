/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion.result;

import static com.microsoft.spring.data.gremlin.common.Constants.PROPERTY_ID;
import static com.microsoft.spring.data.gremlin.common.Constants.PROPERTY_LABEL;
import static com.microsoft.spring.data.gremlin.common.Constants.PROPERTY_PROPERTIES;
import static com.microsoft.spring.data.gremlin.common.Constants.PROPERTY_TYPE;
import static com.microsoft.spring.data.gremlin.common.Constants.PROPERTY_VALUE;
import static com.microsoft.spring.data.gremlin.common.Constants.RESULT_TYPE_VERTEX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.junit.jupiter.api.Test;

class GremlinResultAdapterUnitTest {

    @Test
    void toElementMapFromVertexElement() {
        final VertexProperty<Object> nameProperty = mock(VertexProperty.class);
        when(nameProperty.isPresent()).thenReturn(true);
        when(nameProperty.value()).thenReturn("bill");

        final VertexProperty<Object> partitionKeyProperty = mock(VertexProperty.class);
        when(partitionKeyProperty.isPresent()).thenReturn(true);
        when(partitionKeyProperty.value()).thenReturn("demo");

        final Vertex vertex = mock(Vertex.class);
        when(vertex.id()).thenReturn("123");
        when(vertex.label()).thenReturn("label-person");
        when(vertex.keys()).thenReturn(Set.of("name", "partitionKey"));
        when(vertex.property("name")).thenReturn(nameProperty);
        when(vertex.property("partitionKey")).thenReturn(partitionKeyProperty);

        final Result result = mock(Result.class);
        when(result.getObject()).thenReturn(vertex);

        final Map<String, Object> map = GremlinResultAdapter.toElementMap(result);

        assertEquals("123", map.get(PROPERTY_ID));
        assertEquals("label-person", map.get(PROPERTY_LABEL));
        assertEquals(RESULT_TYPE_VERTEX, map.get(PROPERTY_TYPE));

        @SuppressWarnings("unchecked")
        final Map<String, Object> properties = (Map<String, Object>) map.get(PROPERTY_PROPERTIES);
        assertEquals("bill", readVertexProperty(properties, "name"));
        assertEquals("demo", readVertexProperty(properties, "partitionKey"));
    }

    @Test
    void toElementMapFromGraphSonMap() {
        final Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("name", "bill");

        final Map<String, Object> element = new LinkedHashMap<>();
        element.put(PROPERTY_ID, "123");
        element.put(PROPERTY_LABEL, "label-person");
        element.put(PROPERTY_TYPE, RESULT_TYPE_VERTEX);
        element.put(PROPERTY_PROPERTIES, properties);

        final Result result = mock(Result.class);
        when(result.getObject()).thenReturn(element);

        final Map<String, Object> map = GremlinResultAdapter.toElementMap(result);

        assertEquals("123", map.get(PROPERTY_ID));
        @SuppressWarnings("unchecked")
        final Map<String, Object> normalized = (Map<String, Object>) map.get(PROPERTY_PROPERTIES);
        assertEquals("bill", readVertexProperty(normalized, "name"));
    }

    private static Object readVertexProperty(Map<String, Object> properties, String key) {
        final Object value = properties.get(key);
        assertInstanceOf(ArrayList.class, value);
        @SuppressWarnings("unchecked")
        final ArrayList<LinkedHashMap<String, Object>> values = (ArrayList<LinkedHashMap<String, Object>>) value;
        assertEquals(1, values.size());
        assertTrue(values.get(0).containsKey(PROPERTY_VALUE));
        return values.get(0).get(PROPERTY_VALUE);
    }
}
