/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query.parameter;

import com.microsoft.spring.data.gremlin.query.paramerter.GremlinParameter;
import com.microsoft.spring.data.gremlin.query.paramerter.GremlinParameters;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;

import java.lang.reflect.Method;

public class GremlinParameterUnitTest {

    private Method method;
    private MethodParameter methodParameter;

    public String handle(@NonNull String name) {
        return "handle: " + name;
    }

    @BeforeEach
    @SneakyThrows
    public void setup() {
        method = this.getClass().getMethod("handle", String.class);
        methodParameter = new MethodParameter(this.getClass().getMethod("handle", String.class), 0);
    }

    @Test
    public void testGremlinParameter() {
        final GremlinParameter parameter = new GremlinParameter(this.methodParameter);

        assertNotNull(parameter);
        assertEquals(parameter.getType(), String.class);
        assertEquals(parameter.getIndex(), 0);
    }

    @Test
    public void testGremlinParameters() {
        final GremlinParameters gremlinParameters = new GremlinParameters(this.method);

        assertNotNull(gremlinParameters);
        assertEquals(gremlinParameters.getNumberOfParameters(), 1);
        assertNotNull(gremlinParameters.getParameter(0));
    }
}

