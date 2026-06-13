/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion.result;

import com.microsoft.spring.data.gremlin.conversion.script.GremlinScriptLiteralEdge;
import com.microsoft.spring.data.gremlin.conversion.script.GremlinScriptLiteralGraph;
import com.microsoft.spring.data.gremlin.conversion.script.GremlinScriptLiteralVertex;
import com.microsoft.spring.data.gremlin.conversion.source.GremlinSourceEdge;
import com.microsoft.spring.data.gremlin.conversion.source.GremlinSourceVertex;
import com.microsoft.spring.data.gremlin.exception.GremlinUnexpectedSourceTypeException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class GremlinResultUnitTest {

        @Test
    public void testVertexInsertException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinScriptLiteralVertex().generateInsertScript(new GremlinSourceEdge()));
    }

        @Test
    public void testVertexUpdateException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinScriptLiteralVertex().generateUpdateScript(new GremlinSourceEdge()));
    }

        @Test
    public void testVertexFindByIdException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinScriptLiteralVertex().generateFindByIdScript(new GremlinSourceEdge()));
    }

        @Test
    public void testEdgeInsertException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinScriptLiteralEdge().generateInsertScript(new GremlinSourceVertex()));
    }

        @Test
    public void testEdgeUpdateException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinScriptLiteralEdge().generateUpdateScript(new GremlinSourceVertex()));
    }

        @Test
    public void testEdgeFindByIdException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinScriptLiteralEdge().generateFindByIdScript(new GremlinSourceVertex()));
    }

        @Test
    public void testGraphInsertException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinScriptLiteralGraph().generateInsertScript(new GremlinSourceVertex()));
    }

        @Test
    public void testGraphUpdateException() {
        assertThrows(GremlinUnexpectedSourceTypeException.class, () -> new GremlinScriptLiteralGraph().generateUpdateScript(new GremlinSourceVertex()));
    }

        @Test
    public void testGraphFindByIdException() {
        assertThrows(UnsupportedOperationException.class, () -> new GremlinScriptLiteralGraph().generateFindByIdScript(new GremlinSourceVertex()));
    }
}
