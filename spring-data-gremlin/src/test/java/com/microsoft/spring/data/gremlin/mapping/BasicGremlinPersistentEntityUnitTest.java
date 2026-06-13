/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.mapping;

import com.microsoft.spring.data.gremlin.annotation.Edge;
import com.microsoft.spring.data.gremlin.annotation.Graph;
import com.microsoft.spring.data.gremlin.annotation.Vertex;
import com.microsoft.spring.data.gremlin.common.TestConstants;
import com.microsoft.spring.data.gremlin.common.domain.Network;
import com.microsoft.spring.data.gremlin.common.domain.Person;
import com.microsoft.spring.data.gremlin.common.domain.Relationship;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.util.ClassTypeInformation;

public class BasicGremlinPersistentEntityUnitTest {

    @Test
    public void testVertexPersistentEntity() {
        final BasicGremlinPersistentEntity<Person> entity =
                new BasicGremlinPersistentEntity<>(ClassTypeInformation.from(Person.class));
        final Vertex annotation = entity.findAnnotation(Vertex.class);

        assertEquals(entity.getType(), Person.class);
        assertEquals(annotation.annotationType(), Vertex.class);
        assertEquals(annotation.label(), TestConstants.VERTEX_PERSON_LABEL);
    }

    @Test
    public void testEdgePersistentEntity() {
        final BasicGremlinPersistentEntity<Relationship> entity =
                new BasicGremlinPersistentEntity<>(ClassTypeInformation.from(Relationship.class));
        final Edge annotation = entity.findAnnotation(Edge.class);

        assertEquals(entity.getType(), Relationship.class);
        assertEquals(annotation.annotationType(), Edge.class);
        assertEquals(annotation.label(), TestConstants.EDGE_RELATIONSHIP_LABEL);
    }

    @Test
    public void testGraphPersistentEntity() {
        final BasicGremlinPersistentEntity<Network> entity =
                new BasicGremlinPersistentEntity<>(ClassTypeInformation.from(Network.class));
        final Graph annotation = entity.findAnnotation(Graph.class);

        assertEquals(entity.getType(), Network.class);
        assertEquals(annotation.annotationType(), Graph.class);
    }
}
