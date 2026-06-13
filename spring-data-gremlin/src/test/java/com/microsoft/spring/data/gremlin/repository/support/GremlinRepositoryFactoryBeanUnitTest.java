/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.repository.support;

import com.microsoft.spring.data.gremlin.common.TestConstants;
import com.microsoft.spring.data.gremlin.common.domain.Person;
import com.microsoft.spring.data.gremlin.common.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class GremlinRepositoryFactoryBeanUnitTest {

    @Autowired
    private ApplicationContext context;

    private GremlinRepositoryFactoryBean factoryBean;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setup() {
        this.factoryBean = new GremlinRepositoryFactoryBean(PersonRepository.class);
    }

    @Test
    public void testGetFactoryInstance() {
        final Person person = new Person(TestConstants.VERTEX_PERSON_ID, TestConstants.VERTEX_PERSON_NAME);
        final RepositoryFactorySupport factorySupport = this.factoryBean.getFactoryInstance(this.context);

        assertNotNull(factorySupport);
        assertEquals(factorySupport.getEntityInformation(Person.class).getIdType(), String.class);
        assertEquals(factorySupport.getEntityInformation(Person.class).getId(person), person.getId());
    }
}
