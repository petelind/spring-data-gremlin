/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.repository;

import com.microsoft.spring.data.gremlin.common.GremlinEntityType;
import com.microsoft.spring.data.gremlin.common.TestConstants;
import com.microsoft.spring.data.gremlin.common.TestRepositoryConfiguration;
import com.microsoft.spring.data.gremlin.common.domain.Network;
import com.microsoft.spring.data.gremlin.common.domain.Person;
import com.microsoft.spring.data.gremlin.common.domain.Project;
import com.microsoft.spring.data.gremlin.common.domain.Relationship;
import com.microsoft.spring.data.gremlin.common.repository.NetworkRepository;
import com.microsoft.spring.data.gremlin.common.repository.PersonRepository;
import com.microsoft.spring.data.gremlin.common.repository.ProjectRepository;
import com.microsoft.spring.data.gremlin.common.repository.RelationshipRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
public class NetworkRepositoryIT {

    private final Person person = new Person(TestConstants.VERTEX_PERSON_ID, TestConstants.VERTEX_PERSON_NAME);
    private final Project project = new Project(TestConstants.VERTEX_PROJECT_ID, TestConstants.VERTEX_PROJECT_NAME,
            TestConstants.VERTEX_PROJECT_URI);
    private final Relationship relationship = new Relationship(TestConstants.EDGE_RELATIONSHIP_ID,
            TestConstants.EDGE_RELATIONSHIP_NAME, TestConstants.EDGE_RELATIONSHIP_LOCATION,
            this.person, this.project);

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RelationshipRepository relationshipRepository;

    @BeforeEach
    public void setup() {
        this.networkRepository.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        this.networkRepository.deleteAll();
    }

    @Test
    public void testDeleteById() {
        final Network network = new Network();

        network.setId("fake-id");
        network.vertexAdd(this.person);
        network.vertexAdd(this.project);
        network.edgeAdd(this.relationship);

        this.networkRepository.save(network);

        assertTrue(personRepository.findById(this.person.getId()).isPresent());
        assertTrue(projectRepository.findById(this.project.getId()).isPresent());
        assertTrue(relationshipRepository.findById(this.relationship.getId()).isPresent());

        this.networkRepository.deleteById(network.getId());

        assertFalse(personRepository.findById(this.person.getId()).isPresent());
        assertFalse(projectRepository.findById(this.project.getId()).isPresent());
        assertFalse(relationshipRepository.findById(this.relationship.getId()).isPresent());
    }

        @Test
    public void testFindAllException() {
        final Network network = new Network();
        network.setId("fake-id");
        network.vertexAdd(this.person);
        network.vertexAdd(this.project);
        network.edgeAdd(this.relationship);
        this.networkRepository.save(network);
        assertThrows(UnsupportedOperationException.class, () -> this.networkRepository.findAll(Network.class));
    }

        @Test
    public void testFindScriptGeneratorException() {
        final Network network = new Network();
        network.setId("fake-id");
        network.vertexAdd(this.person);
        network.vertexAdd(this.project);
        network.edgeAdd(this.relationship);
        this.networkRepository.save(network);
        assertThrows(UnsupportedOperationException.class, () -> this.networkRepository.findByEdgeList(Collections.singletonList(this.relationship)));
    }

    @Test
    public void testDeleteAllByType() {
        final Network network = new Network();

        network.setId("fake-id");
        network.vertexAdd(this.person);
        network.vertexAdd(this.project);
        network.edgeAdd(this.relationship);

        this.networkRepository.save(network);
        this.networkRepository.deleteAll(GremlinEntityType.GRAPH);

        assertFalse(this.personRepository.findById(this.person.getId()).isPresent());
        assertFalse(this.projectRepository.findById(this.project.getId()).isPresent());
        assertFalse(this.relationshipRepository.findById(this.relationship.getId()).isPresent());
    }

    @Test
    public void testDeleteAllByClass() {
        final Network network = new Network();

        network.setId("fake-id");
        network.vertexAdd(this.person);
        network.vertexAdd(this.project);
        network.edgeAdd(this.relationship);

        this.networkRepository.save(network);
        this.networkRepository.deleteAll(Network.class);

        assertFalse(this.relationshipRepository.findById(this.relationship.getId()).isPresent());
        assertFalse(this.personRepository.findById(this.person.getId()).isPresent());
        assertFalse(this.projectRepository.findById(this.project.getId()).isPresent());
    }
}
