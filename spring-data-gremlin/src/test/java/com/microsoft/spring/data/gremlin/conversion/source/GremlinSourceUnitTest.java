/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion.source;

import com.microsoft.spring.data.gremlin.annotation.EdgeFrom;
import com.microsoft.spring.data.gremlin.annotation.EdgeTo;
import com.microsoft.spring.data.gremlin.annotation.Vertex;
import com.microsoft.spring.data.gremlin.conversion.MappingGremlinConverter;
import com.microsoft.spring.data.gremlin.exception.GremlinEntityInformationException;
import com.microsoft.spring.data.gremlin.exception.GremlinUnexpectedSourceTypeException;
import com.microsoft.spring.data.gremlin.mapping.GremlinMappingContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScanner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.annotation.Persistent;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class GremlinSourceUnitTest {

    private MappingGremlinConverter converter;

    @Autowired
    private ApplicationContext context;

    @BeforeEach
    @SneakyThrows
    public void setup() {
        final GremlinMappingContext mappingContext = new GremlinMappingContext();

        mappingContext.setInitialEntitySet(new EntityScanner(this.context).scan(Persistent.class));

        this.converter = new MappingGremlinConverter(mappingContext);
    }

        @Test
    public void testVertexWriteException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinSourceVertexWriter().write(new Object(), this.converter, new GremlinSourceEdge()));
    }

        @Test
    public void testVertexReadException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinSourceVertexReader().read(Object.class, this.converter, new GremlinSourceEdge()));
    }

        @Test
    public void testEdgeWriteException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinSourceEdgeWriter().write(new Object(), this.converter, new GremlinSourceVertex()));
    }

        @Test
    public void testEdgeReadException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinSourceEdgeReader().read(Object.class, this.converter, new GremlinSourceVertex()));
    }

        @Test
    public void testGraphWriteException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinSourceGraphWriter().write(new Object(), this.converter, new GremlinSourceVertex()));
    }

        @Test
    public void testGraphReadException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinSourceEdgeReader().read(Object.class, this.converter, new GremlinSourceVertex()));
    }

        @Test
    public void testGraphAddSourceException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinSourceGraph().addGremlinSource(new GremlinSourceGraph()));
    }

        @Test
    public void testVertexWithPredefinedProperty() {
        @SuppressWarnings("unchecked") final GremlinSource source = new GremlinSourceVertex(TestVertex.class);
        assertThrows(GremlinEntityInformationException.class, () -> new GremlinSourceVertexWriter().write(new TestVertex("fake-id", "fake-name"), this.converter, source));
    }

        @Test
    public void testEdgeWithPredefinedProperty() {
        @SuppressWarnings("unchecked") final GremlinSource source = new GremlinSourceEdge(TestEdge.class);
        assertThrows(GremlinEntityInformationException.class, () -> new GremlinSourceEdgeWriter().write(new TestEdge("fake-id", "fake-name", "1", "2"), this.converter, source));
    }

    @Vertex
    @Data
    @AllArgsConstructor
    private static class TestVertex {

        private String id;

        private String _classname;
    }

    @Vertex
    @Data
    @AllArgsConstructor
    private static class TestEdge {

        private String id;

        private String _classname;

        @EdgeFrom
        private String from;

        @EdgeTo
        private String to;
    }
}
