/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.cosmos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microsoft.spring.data.gremlin.common.TestRepositoryConfiguration;
import com.microsoft.spring.data.gremlin.common.domain.Person;
import com.microsoft.spring.data.gremlin.common.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
class CosmosGremlinSmokeIT {

    @Autowired
    private PersonRepository personRepository;

    @DynamicPropertySource
    static void cosmosGremlinProperties(DynamicPropertyRegistry registry) {
        registry.add("gremlin.endpoint", () -> requiredEnv("GREMLIN_ENDPOINT"));
        registry.add("gremlin.port", () -> Integer.parseInt(requiredEnv("GREMLIN_PORT")));
        registry.add("gremlin.username", () -> requiredEnv("GREMLIN_USERNAME"));
        registry.add("gremlin.password", () -> requiredEnv("GREMLIN_PASSWORD"));
        registry.add("gremlin.sslEnabled", () -> true);
        registry.add("gremlin.serializer", () -> requiredEnv("GREMLIN_SERIALIZER", "GRAPHSON_V2"));
        registry.add("gremlin.telemetryAllowed", () -> false);
    }

    @Test
    void saveFindDeletePersonAgainstCosmos() {
        final String id = "cosmos-smoke-" + System.currentTimeMillis();
        final Person person = new Person(id, "cosmos-smoke");

        this.personRepository.save(person);
        assertTrue(this.personRepository.existsById(id));
        assertEquals("cosmos-smoke", this.personRepository.findById(id).orElseThrow().getName());

        this.personRepository.deleteById(id);
        assertFalse(this.personRepository.existsById(id));
    }

    private static String requiredEnv(String name) {
        return requiredEnv(name, null);
    }

    private static String requiredEnv(String name, String defaultValue) {
        final String value = System.getenv(name);
        if (value != null && !value.isBlank()) {
            return value;
        }
        if (defaultValue != null) {
            return defaultValue;
        }
        throw new IllegalStateException(
                "Set " + name + " before running Cosmos tests (e.g. source .cosmos-gremlin.env)");
    }
}
