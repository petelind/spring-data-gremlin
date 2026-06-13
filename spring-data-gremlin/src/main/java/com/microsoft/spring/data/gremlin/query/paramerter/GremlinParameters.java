/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.query.paramerter;

import org.springframework.data.repository.query.Parameters;

import java.lang.reflect.Method;
import java.util.List;

public class GremlinParameters extends Parameters<GremlinParameters, GremlinParameter> {

    public GremlinParameters(Method method) {
        super(method, GremlinParameter::new);
    }

    private GremlinParameters(List<GremlinParameter> parameters) {
        super(parameters);
    }

    @Override
    protected GremlinParameters createFrom(List<GremlinParameter> parameters) {
        return new GremlinParameters(parameters);
    }
}
