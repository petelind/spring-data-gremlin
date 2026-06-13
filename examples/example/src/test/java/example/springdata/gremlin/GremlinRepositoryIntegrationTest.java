/*
 * Copyright 2015-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.springdata.gremlin;

import example.springdata.gremlin.domain.Network;
import example.springdata.gremlin.domain.Person;
import example.springdata.gremlin.domain.Relation;
import example.springdata.gremlin.repository.NetworkRepository;
import example.springdata.gremlin.repository.PersonRepository;
import example.springdata.gremlin.repository.RelationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Disabled("Requires a running Gremlin Server; run with -Pfull-test against localhost:8889")
public class GremlinRepositoryIntegrationTest {
    private static final String PERSON_ID = "89757";
    private static final String PERSON_ID_0 = "0123456789";
    private static final String PERSON_ID_1 = "666666";
    private static final String PERSON_NAME = "person-name";
    private static final String PERSON_NAME_0 = "person-No.0";
    private static final String PERSON_NAME_1 = "person-No.1";
    private static final String PERSON_AGE = "4";
    private static final String PERSON_AGE_0 = "18";
    private static final String PERSON_AGE_1 = "27";

    private static final String RELATION_ID = "2333";
    private static final String RELATION_NAME = "brother";

    private final Person person = new Person(PERSON_ID, PERSON_NAME, PERSON_AGE, "pk");
    private final Person person0 = new Person(PERSON_ID_0, PERSON_NAME_0, PERSON_AGE_0, "pk");
    private final Person person1 = new Person(PERSON_ID_1, PERSON_NAME_1, PERSON_AGE_1, "pk");
    private final Relation relation = new Relation(RELATION_ID, RELATION_NAME, person0, person1);
    private final Network network = new Network();

    @Autowired
    private PersonRepository personRepo;

    @Autowired
    private RelationRepository relationRepo;

    @Autowired
    private NetworkRepository networkRepo;

    @BeforeEach
    public void setup() {
        this.networkRepo.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        this.networkRepo.deleteAll();
    }

    @Test
    public void testRepository() {
        this.network.getVertexes().add(this.person);
        this.network.getVertexes().add(this.person0);
        this.network.getVertexes().add(this.person1);
        this.network.getEdges().add(this.relation);

        this.networkRepo.save(this.network);

        final Optional<Person> personOptional = this.personRepo.findById(this.person.getId());
        assertTrue(personOptional.isPresent());

        final Person personFound = personOptional.get();
        assertEquals(personFound.getId(), this.person.getId());
        assertEquals(personFound.getName(), this.person.getName());
        assertEquals(personFound.getAge(), this.person.getAge());

        final Optional<Relation> relationOptional = this.relationRepo.findById(this.relation.getId());
        assertTrue(relationOptional.isPresent());

        final Relation relationFound = relationOptional.get();

        assertEquals(relationFound.getId(), this.relation.getId());
        assertEquals(relationFound.getName(), this.relation.getName());
    }
}

