/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion.script;

import com.microsoft.spring.data.gremlin.common.GremlinEntityType;
import com.microsoft.spring.data.gremlin.exception.GremlinInvalidEntityIdFieldException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AbstractGremlinScriptLiteralUnitTest extends AbstractGremlinScriptLiteral {

        @Test
    public void testEntityInvalidIdType() {
        final Double id = 12.342;
        assertThrows(GremlinInvalidEntityIdFieldException.class, () -> AbstractGremlinScriptLiteral.generateEntityWithRequiredId(id, GremlinEntityType.EDGE));
    }

        @Test
    public void testPropertyInvalidIdType() {
        final Double id = 12.342;
        assertThrows(GremlinInvalidEntityIdFieldException.class, () -> AbstractGremlinScriptLiteral.generatePropertyWithRequiredId(id));
    }
}
