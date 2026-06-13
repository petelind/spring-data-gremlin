/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.conversion.source;

import com.microsoft.spring.data.gremlin.common.GremlinUtils;
import com.microsoft.spring.data.gremlin.exception.GremlinEntityInformationException;
import com.microsoft.spring.data.gremlin.exception.GremlinUnexpectedEntityTypeException;
import lombok.NonNull;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.data.mapping.PersistentProperty;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;

public abstract class AbstractGremlinSourceReader {

    protected Object readProperty(@NonNull PersistentProperty property, @Nullable Object value) {
        final Class<?> type = property.getTypeInformation().getType();
        final JavaType javaType = TypeFactory.defaultInstance().constructType(property.getType());

        if (value == null) {
            return null;
        } else if (type == int.class || type == Integer.class
                || type == Boolean.class || type == boolean.class
                || type == String.class) {
            return value;
        } else if (type == Date.class) {
            Assert.isTrue(value instanceof Long, "Date store value must be instance of long");
            return new Date((Long) value);
        } else {
            final Object object;

            try {
                object = GremlinUtils.getObjectMapper().readValue(value.toString(), javaType);
            } catch (IOException e) {
                throw new GremlinUnexpectedEntityTypeException("Failed to read String to Object", e);
            }

            return object;
        }
    }

    protected Object getGremlinSourceId(@NonNull GremlinSource source) {
        if (!source.getId().isPresent()) {
            return null;
        }

        final Object id = source.getId().get();
        final Class<?> idType = source.getIdField().getType();

        if (idType == String.class) {
            return id.toString();
        }
        if (idType == Integer.class || idType == int.class) {
            if (id instanceof Integer) {
                return id;
            }
            if (id instanceof Number) {
                return ((Number) id).intValue();
            }
        }
        if (idType == Long.class || idType == long.class) {
            if (id instanceof Long) {
                return id;
            }
            if (id instanceof Number) {
                return ((Number) id).longValue();
            }
        }

        throw new GremlinEntityInformationException("unsupported id field type: " + id.getClass().getSimpleName());
    }
}

