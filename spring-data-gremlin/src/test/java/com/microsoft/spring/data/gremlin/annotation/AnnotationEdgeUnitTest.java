/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.annotation;

import com.microsoft.spring.data.gremlin.common.GremlinUtils;
import com.microsoft.spring.data.gremlin.common.TestConstants;
import com.microsoft.spring.data.gremlin.common.domain.Dependency;
import com.microsoft.spring.data.gremlin.common.domain.Relationship;
import com.microsoft.spring.data.gremlin.conversion.source.GremlinSource;
import com.microsoft.spring.data.gremlin.conversion.source.GremlinSourceEdge;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AnnotationEdgeUnitTest {

    @Test
    public void testAnnotationEdgeDefaultLabel() {
        final GremlinSource source = GremlinUtils.toGremlinSource(Dependency.class);

        assertTrue(source instanceof GremlinSourceEdge);
        assertNotNull(source.getLabel());
        assertEquals(source.getLabel(), Dependency.class.getSimpleName());
    }

    @Test
    public void testAnnotationEdgeSpecifiedLabel() {
        final GremlinSource source = GremlinUtils.toGremlinSource(Relationship.class);

        assertTrue(source instanceof GremlinSourceEdge);
        assertNotNull(source.getLabel());
        assertEquals(source.getLabel(), TestConstants.EDGE_RELATIONSHIP_LABEL);
    }
}
