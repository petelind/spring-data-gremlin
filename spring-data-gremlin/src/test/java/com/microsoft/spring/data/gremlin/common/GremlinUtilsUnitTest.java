/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.common;

import com.microsoft.spring.data.gremlin.common.domain.Service;
import com.microsoft.spring.data.gremlin.conversion.source.AbstractGremlinSource;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class GremlinUtilsUnitTest {

        @Test
    public void testCreateIntegerInstance() {
        assertThrows(IllegalArgumentException.class, () -> GremlinUtils.createInstance(Integer.class));
    }

        @Test
    public void testCreateTestConstantsInstance() {
        assertThrows(IllegalArgumentException.class, () -> GremlinUtils.createInstance(TestConstants.class));
    }

        @Test
    public void testCreateAbstractInstance() {
        assertThrows(IllegalArgumentException.class, () -> GremlinUtils.createInstance(AbstractGremlinSource.class));
    }

        @Test
    public void testTimeToMilliSecondsException() {
        assertThrows(UnsupportedOperationException.class, () -> GremlinUtils.timeToMilliSeconds(new Service()));
    }

        @Test
    public void testToPrimitiveLongException() {
        assertThrows(UnsupportedOperationException.class, () -> GremlinUtils.toPrimitiveLong((short) 2));
    }

    @Test
    public void testToPrimitiveLong() {
        assertEquals((long) 3, GremlinUtils.toPrimitiveLong(new Long(3)));
    }
}
