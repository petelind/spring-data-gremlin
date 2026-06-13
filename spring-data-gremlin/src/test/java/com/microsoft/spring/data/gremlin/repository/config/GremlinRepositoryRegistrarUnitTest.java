/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.repository.config;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class GremlinRepositoryRegistrarUnitTest {

    private GremlinRepositoryRegistrar registrar;

    @BeforeEach
    public void setup() {
        this.registrar = new GremlinRepositoryRegistrar();
    }

    @Test
    public void testGremlinRepositoryRegistrarGetters() {
        assertSame(this.registrar.getAnnotation(), EnableGremlinRepositories.class);
        assertTrue(this.registrar.getExtension() instanceof GremlinRepositoryConfigurationExtension);
    }
}
