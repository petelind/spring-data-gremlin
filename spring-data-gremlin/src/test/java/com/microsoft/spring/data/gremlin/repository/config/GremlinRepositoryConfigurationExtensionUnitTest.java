/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.repository.config;

import com.microsoft.spring.data.gremlin.repository.GremlinRepository;
import com.microsoft.spring.data.gremlin.repository.support.GremlinRepositoryFactoryBean;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

public class GremlinRepositoryConfigurationExtensionUnitTest {

    private static final String GREMLIN_MODULE_NAME = "Gremlin";
    private static final String GREMLIN_MODULE_PREFIX = "gremlin";
    private static final String GREMLIN_MAPPING_CONTEXT = "gremlinMappingContext";

    private GremlinRepositoryConfigurationExtension extension;

    @BeforeEach
    public void setup() {
        this.extension = new GremlinRepositoryConfigurationExtension();
    }

    @Test
    public void testGremlinRepositoryConfigurationExtensionGetters() {
        assertEquals(this.extension.getModuleName(), GREMLIN_MODULE_NAME);
        assertEquals(this.extension.getModulePrefix(), GREMLIN_MODULE_PREFIX);
        assertEquals(this.extension.getIdentifyingTypes().size(), 1);

        assertSame(this.extension.getIdentifyingTypes().toArray()[0], GremlinRepository.class);
        assertTrue(this.extension.getIdentifyingAnnotations().isEmpty());
    }

    @Test
    public void testGremlinRepositoryConfigurationExtensionRegisterBeansForRoot() {
        final ResourceLoader loader = new PathMatchingResourcePatternResolver();
        final Environment environment = new StandardEnvironment();
        final BeanDefinitionRegistry registry = new DefaultListableBeanFactory();
        final StandardAnnotationMetadata metadata = new StandardAnnotationMetadata(GremlinConfig.class, true);
        final RepositoryConfigurationSource config = new AnnotationRepositoryConfigurationSource(metadata,
                EnableGremlinRepositories.class, loader, environment, registry);

        assertFalse(registry.containsBeanDefinition(GREMLIN_MAPPING_CONTEXT));

        this.extension.registerBeansForRoot(registry, config);

        assertTrue(registry.containsBeanDefinition(GREMLIN_MAPPING_CONTEXT));
    }

    @Test
    public void testGetRepositoryFactoryBeanClassName() {
        assertEquals(GremlinRepositoryFactoryBean.class.getName(), this.extension.getRepositoryFactoryBeanClassName());
    }

    @EnableGremlinRepositories
    private static class GremlinConfig {

    }
}
