/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.common;

import static org.junit.jupiter.api.Assertions.*;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {

    public static <T> void assertEntitiesEquals(List<T> expect, List<T> actual) {
        assertEquals(actual.size(), expect.size());

        actual.sort(Comparator.comparing(T::toString));
        expect.sort(Comparator.comparing(T::toString));

        assertEquals(actual, expect);
    }
}
