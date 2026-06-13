/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.config;

import com.microsoft.spring.data.gremlin.common.TestRepositoryConfiguration;
import lombok.SneakyThrows;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
public class AbstractGremlinConfigurationIT {

    @Autowired
    private TestRepositoryConfiguration testConfig;

    @Test
    public void testGremlinFactory() {
        assertNotNull(this.testConfig.gremlinFactory());
    }

    @Test
    @SneakyThrows
    public void testMappingGremlinConverter() {
        assertNotNull(this.testConfig.mappingGremlinConverter());
    }

    @Test
    @SneakyThrows
    public void testGremlinTemplate() {
        assertNotNull(this.testConfig.gremlinTemplate(testConfig.gremlinFactory()));
    }
}

