/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.annotation;

import com.microsoft.spring.data.gremlin.common.GremlinUtils;
import com.microsoft.spring.data.gremlin.common.domain.Network;
import com.microsoft.spring.data.gremlin.common.domain.Roadmap;
import com.microsoft.spring.data.gremlin.conversion.source.GremlinSource;
import com.microsoft.spring.data.gremlin.conversion.source.GremlinSourceGraph;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AnnotationGraphUnitTest {

    @Test
    public void testAnnotationGraphDefaultCollection() {
        final GremlinSource source = GremlinUtils.toGremlinSource(Network.class);

        assertTrue(source instanceof GremlinSourceGraph);
        assertTrue(source.getLabel().isEmpty());
    }

    @Test
    public void testAnnotationGraphSpecifiedCollection() {
        final GremlinSource source = GremlinUtils.toGremlinSource(Roadmap.class);

        assertTrue(source instanceof GremlinSourceGraph);
        assertTrue(source.getLabel().isEmpty());
    }
}
