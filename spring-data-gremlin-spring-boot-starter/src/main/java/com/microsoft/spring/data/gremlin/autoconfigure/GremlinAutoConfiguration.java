/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.autoconfigure;

import com.microsoft.spring.data.gremlin.common.GremlinConfig;
import com.microsoft.spring.data.gremlin.common.GremlinFactory;
import com.microsoft.spring.data.gremlin.conversion.MappingGremlinConverter;
import com.microsoft.spring.data.gremlin.mapping.GremlinMappingContext;
import com.microsoft.spring.data.gremlin.query.GremlinTemplate;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(GremlinFactory.class)
@EnableConfigurationProperties(GremlinProperties.class)
public class GremlinAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GremlinMappingContext gremlinMappingContext() {
        return new GremlinMappingContext();
    }

    @Bean
    @ConditionalOnMissingBean
    public GremlinConfig gremlinConfig(GremlinProperties properties) {
        return GremlinConfig.defaultBuilder()
                .endpoint(properties.getEndpoint())
                .port(properties.getPort())
                .username(properties.getUsername())
                .password(properties.getPassword())
                .sslEnabled(properties.isSslEnabled())
                .telemetryAllowed(properties.isTelemetryAllowed())
                .serializer(properties.getSerializer())
                .maxContentLength(properties.getMaxContentLength())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public GremlinFactory gremlinFactory(GremlinConfig gremlinConfig) {
        return new GremlinFactory(gremlinConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public MappingGremlinConverter mappingGremlinConverter(GremlinMappingContext gremlinMappingContext)
            throws ClassNotFoundException {
        return new MappingGremlinConverter(gremlinMappingContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public GremlinTemplate gremlinTemplate(GremlinFactory gremlinFactory,
                                           MappingGremlinConverter mappingGremlinConverter) {
        return new GremlinTemplate(gremlinFactory, mappingGremlinConverter);
    }
}
