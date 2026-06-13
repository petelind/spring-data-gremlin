/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */
package com.microsoft.spring.data.gremlin.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.microsoft.spring.data.gremlin.common.TestRepositoryConfiguration;
import com.microsoft.spring.data.gremlin.common.domain.Service;
import com.microsoft.spring.data.gremlin.common.domain.ServiceType;
import com.microsoft.spring.data.gremlin.common.domain.SimpleDependency;
import com.microsoft.spring.data.gremlin.common.repository.ServiceRepository;
import com.microsoft.spring.data.gremlin.common.repository.SimpleDependencyRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.microsoft.spring.data.gremlin.common.domain.ServiceType.BACK_END;
import static com.microsoft.spring.data.gremlin.common.domain.ServiceType.FRONT_END;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestRepositoryConfiguration.class)
public class ServiceRepositoryIT {

    private static Service serviceA;
    private static Service serviceB;
    private static Service serviceC;

    private static Date createDateA;
    private static Date createDateB;
    private static Date createDateC;

    private static final Map<String, Object> PROPERTIES_A = new HashMap<>();
    private static final Map<String, Object> PROPERTIES_B = new HashMap<>();
    private static final Map<String, Object> PROPERTIES_C = new HashMap<>();

    private static final String ID_A = "1234";
    private static final String ID_B = "8731";
    private static final String ID_C = "5781";

    private static final int COUNT_A = 2;
    private static final int COUNT_B = 8;
    private static final int COUNT_C = 2;

    private static final String NAME_A = "name-A";
    private static final String NAME_B = "name-B";
    private static final String NAME_C = "name-A";

    @Autowired
    private ServiceRepository repository;

    @Autowired
    private SimpleDependencyRepository dependencyRepo;

    @BeforeAll
    @SneakyThrows
    public static void initialize() {
        PROPERTIES_B.put("serviceB-port", 8761);
        PROPERTIES_B.put("priority", "high");
        PROPERTIES_B.put("enabled-hystrix", false);

        PROPERTIES_A.put("serviceA-port", 8888);
        PROPERTIES_A.put("serviceB-port", 8761);
        PROPERTIES_A.put("priority", "highest");

        PROPERTIES_C.put("serviceC-port", 8090);
        PROPERTIES_C.put("serviceB-port", 8761);
        PROPERTIES_C.put("priority", "medium");

        createDateA = new SimpleDateFormat("yyyyMMdd").parse("20180601");
        createDateB = new SimpleDateFormat("yyyyMMdd").parse("20180603");
        createDateC = new SimpleDateFormat("yyyyMMdd").parse("20180503");

        serviceA = new Service(ID_A, COUNT_A, true, NAME_A, FRONT_END, createDateA, PROPERTIES_A);
        serviceB = new Service(ID_B, COUNT_B, false, NAME_B, BACK_END, createDateB, PROPERTIES_B);
        serviceC = new Service(ID_C, COUNT_C, false, NAME_C, BACK_END, createDateC, PROPERTIES_C);
    }

    @BeforeEach
    public void setup() {
        this.repository.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        this.repository.deleteAll();
    }

    @Test
    public void testQueries() {
        assertFalse(this.repository.findById(serviceA.getId()).isPresent());
        assertFalse(this.repository.findById(serviceB.getId()).isPresent());

        this.repository.save(serviceA);
        this.repository.save(serviceB);

        Optional<Service> foundOptional = this.repository.findById(serviceA.getId());
        assertTrue(foundOptional.isPresent());
        assertEquals(foundOptional.get(), serviceA);

        foundOptional = this.repository.findById(serviceB.getId());
        assertTrue(foundOptional.isPresent());
        assertEquals(foundOptional.get(), serviceB);

        this.repository.deleteById(serviceA.getId());
        this.repository.deleteById(serviceB.getId());

        assertFalse(this.repository.findById(serviceA.getId()).isPresent());
        assertFalse(this.repository.findById(serviceB.getId()).isPresent());
    }

    @Test
    public void testEdgeFromToStringId() {
        final SimpleDependency depend = new SimpleDependency("fakeId", "faked", serviceA.getId(), serviceB.getId());

        this.repository.save(serviceA);
        this.repository.save(serviceB);
        this.dependencyRepo.save(depend);

        final Optional<SimpleDependency> foundOptional = this.dependencyRepo.findById(depend.getId());
        assertTrue(foundOptional.isPresent());
        assertEquals(foundOptional.get(), depend);

        this.dependencyRepo.delete(foundOptional.get());

        assertTrue(this.repository.findById(serviceA.getId()).isPresent());
        assertTrue(this.repository.findById(serviceB.getId()).isPresent());
    }

    @Test
    public void testFindByName() {
        this.repository.save(serviceA);
        this.repository.save(serviceB);

        final List<Service> services = this.repository.findByName(serviceA.getName());

        assertEquals(services.size(), 1);
        assertEquals(services.get(0), serviceA);

        this.repository.deleteAll();

        assertTrue(this.repository.findByName(serviceA.getName()).isEmpty());
    }

    @Test
    public void testFindByInstanceCount() {
        this.repository.save(serviceA);
        this.repository.save(serviceB);

        final List<Service> services = this.repository.findByInstanceCount(serviceB.getInstanceCount());

        assertEquals(services.size(), 1);
        assertEquals(services.get(0), serviceB);

        this.repository.deleteAll();

        assertTrue(this.repository.findByInstanceCount(serviceB.getInstanceCount()).isEmpty());
    }

    @Test
    public void testFindByIsActive() {
        this.repository.save(serviceA);
        this.repository.save(serviceB);

        final List<Service> services = this.repository.findByActive(serviceB.isActive());

        assertEquals(services.size(), 1);
        assertEquals(services.get(0), serviceB);

        this.repository.deleteAll();

        assertTrue(this.repository.findByActive(serviceB.isActive()).isEmpty());
    }

    @Test
    public void testFindByCreateAt() {
        this.repository.save(serviceA);
        this.repository.save(serviceB);

        final List<Service> services = this.repository.findByCreateAt(serviceA.getCreateAt());

        assertEquals(services.size(), 1);
        assertEquals(services.get(0), serviceA);

        this.repository.deleteAll();

        assertTrue(this.repository.findByCreateAt(serviceB.getCreateAt()).isEmpty());
    }

    @Test
    public void testFindByProperties() {
        this.repository.save(serviceA);
        this.repository.save(serviceB);

        final List<Service> services = this.repository.findByProperties(serviceB.getProperties());

        assertEquals(services.size(), 1);
        assertEquals(services.get(0), serviceB);

        this.repository.deleteAll();

        assertTrue(this.repository.findByProperties(serviceB.getProperties()).isEmpty());
    }

    @Test
    public void testFindById() {
        this.repository.save(serviceA);
        this.repository.save(serviceB);

        final Optional<Service> foundConfig = this.repository.findById(serviceA.getId());
        final Optional<Service> foundEureka = this.repository.findById(serviceB.getId());

        assertTrue(foundConfig.isPresent());
        assertTrue(foundEureka.isPresent());

        assertEquals(foundConfig.get(), serviceA);
        assertEquals(foundEureka.get(), serviceB);
    }

    @Test
    public void testFindByNameAndInstanceCount() {
        this.repository.save(serviceA);
        this.repository.save(serviceB);

        final List<Service> services = repository.findByNameAndInstanceCount(NAME_B, COUNT_B);

        assertEquals(services.size(), 1);
        assertEquals(services.get(0), serviceB);
        assertTrue(repository.findByNameAndInstanceCount(NAME_B, COUNT_A).isEmpty());
    }

    @Test
    public void testFindByNameAndInstanceCountAndType() {
        this.repository.save(serviceA);
        this.repository.save(serviceB);

        final List<Service> services = repository.findByNameAndInstanceCountAndType(NAME_B, COUNT_B, BACK_END);

        assertEquals(services.size(), 1);
        assertEquals(services.get(0), serviceB);
        assertTrue(repository.findByNameAndInstanceCountAndType(NAME_B, COUNT_A, ServiceType.BOTH).isEmpty());
    }

    @Test
    public void testFindByNameOrInstanceCount() {
        final List<Service> services = Arrays.asList(serviceA, serviceB);
        this.repository.saveAll(services);

        List<Service> foundServices = repository.findByNameOrInstanceCount(NAME_A, COUNT_B);

        services.sort(Comparator.comparing(Service::getId));
        foundServices.sort(Comparator.comparing(Service::getId));

        assertEquals(foundServices.size(), 2);
        assertEquals(foundServices, services);

        foundServices = repository.findByNameOrInstanceCount("fake-name", COUNT_A);

        assertEquals(foundServices.size(), 1);
        assertEquals(foundServices.get(0), serviceA);
    }

    @Test
    public void testFindByNameAndIsActiveOrProperties() {
        final List<Service> services = Arrays.asList(serviceA, serviceB);
        this.repository.saveAll(services);

        List<Service> foundServices = repository.findByNameAndActiveOrProperties(NAME_A, true, PROPERTIES_B);

        services.sort(Comparator.comparing(Service::getId));
        foundServices.sort(Comparator.comparing(Service::getId));

        assertEquals(foundServices.size(), 2);
        assertEquals(foundServices, services);

        foundServices = repository.findByNameAndActiveOrProperties(NAME_B, false, new HashMap<>());
        assertEquals(foundServices.size(), 1);
        assertEquals(foundServices.get(0), serviceB);
    }

    @Test
    public void testFindByNameOrInstanceCountAndType() {
        final List<Service> services = Arrays.asList(serviceA, serviceB);
        this.repository.saveAll(services);

        List<Service> foundServices = repository.findByNameOrInstanceCountAndType(NAME_A, COUNT_B, BACK_END);

        services.sort(Comparator.comparing(Service::getId));
        foundServices.sort(Comparator.comparing(Service::getId));

        assertEquals(foundServices.size(), 2);
        assertEquals(foundServices, services);

        foundServices = repository.findByNameOrInstanceCountAndType(NAME_B, COUNT_A, BACK_END);
        assertEquals(foundServices.size(), 1);
        assertEquals(foundServices.get(0), serviceB);
    }

    @Test
    public void testFindByNameAndInstanceCountOrType() {
        final List<Service> services = Arrays.asList(serviceA, serviceB);
        this.repository.saveAll(services);

        List<Service> foundServices = repository.findByNameAndInstanceCountOrType(NAME_A, COUNT_A, BACK_END);

        services.sort(Comparator.comparing(Service::getId));
        foundServices.sort(Comparator.comparing(Service::getId));

        assertEquals(foundServices.size(), 2);
        assertEquals(foundServices, services);

        foundServices = repository.findByNameAndInstanceCountOrType(NAME_A, COUNT_B, BACK_END);
        assertEquals(foundServices.size(), 1);
        assertEquals(foundServices.get(0), serviceB);
    }

    @Test
    public void testExistsByName() {
        final List<Service> services = Arrays.asList(serviceA, serviceB, serviceC);
        this.repository.saveAll(services);

        final List<Service> foundServices = repository.findByActiveExists();

        assertEquals(foundServices.size(), 1);
        assertEquals(foundServices.get(0), serviceA);

        this.repository.deleteAll();

        assertTrue(repository.findByActiveExists().isEmpty());
    }

    @Test
    @SneakyThrows
    public void testFindByCreateAtAfter() {
        final List<Service> services = Arrays.asList(serviceA, serviceB);
        this.repository.saveAll(services);

        Date testDate = new SimpleDateFormat("yyyyMMdd").parse("20180602");
        List<Service> foundServices = repository.findByCreateAtAfter(testDate);
        assertEquals(foundServices.size(), 1);
        assertEquals(foundServices.get(0), serviceB);

        testDate = new SimpleDateFormat("yyyyMMdd").parse("20180502");
        foundServices = repository.findByCreateAtAfter(testDate);
        services.sort(Comparator.comparing(Service::getId));
        foundServices.sort(Comparator.comparing(Service::getId));
        assertEquals(foundServices.size(), 2);
        assertEquals(foundServices, services);

        testDate = new SimpleDateFormat("yyyyMMdd").parse("20180606");
        foundServices = repository.findByCreateAtAfter(testDate);
        assertTrue(foundServices.isEmpty());
    }

    @Test
    @SneakyThrows
    public void testFindByNameOrTypeAndInstanceCountAndCreateAtAfter() {
        final List<Service> services = Arrays.asList(serviceA, serviceB);
        this.repository.saveAll(services);

        Date testDate = new SimpleDateFormat("yyyyMMdd").parse("20180601");
        List<Service> foundServices = repository.findByNameOrTypeAndInstanceCountAndCreateAtAfter(NAME_A,
                serviceB.getType(), COUNT_B, testDate);

        services.sort(Comparator.comparing(Service::getId));
        foundServices.sort(Comparator.comparing(Service::getId));
        assertEquals(foundServices.size(), 2);
        assertEquals(foundServices, services);

        testDate = new SimpleDateFormat("yyyyMMdd").parse("20180607");
        foundServices = repository.findByNameOrTypeAndInstanceCountAndCreateAtAfter(NAME_A, serviceB.getType(), COUNT_B,
                testDate);
        assertEquals(foundServices.size(), 1);
        assertEquals(foundServices.get(0), serviceA);
        assertTrue(repository.findByNameOrTypeAndInstanceCountAndCreateAtAfter("fake-name", serviceB.getType(),
                COUNT_B, testDate).isEmpty());
    }

    @Test
    @SneakyThrows
    public void testFindByCreateAtBefore() {
        final List<Service> services = Arrays.asList(serviceA, serviceB);
        this.repository.saveAll(services);

        Date testDate = new SimpleDateFormat("yyyyMMdd").parse("20180602");
        List<Service> foundServices = repository.findByCreateAtBefore(testDate);
        assertEquals(foundServices.size(), 1);
        assertEquals(foundServices.get(0), serviceA);

        testDate = new SimpleDateFormat("yyyyMMdd").parse("20180606");
        foundServices = repository.findByCreateAtBefore(testDate);
        services.sort(Comparator.comparing(Service::getId));
        foundServices.sort(Comparator.comparing(Service::getId));
        assertEquals(foundServices.size(), 2);
        assertEquals(foundServices, services);

        testDate = new SimpleDateFormat("yyyyMMdd").parse("20180506");
        foundServices = repository.findByCreateAtBefore(testDate);
        assertTrue(foundServices.isEmpty());
    }

    @Test
    @SneakyThrows
    public void testFindByCreateAtBeforeAndCreateAtAfter() {
        final List<Service> services = Arrays.asList(serviceA, serviceB);
        this.repository.saveAll(services);

        Date startDate = new SimpleDateFormat("yyyyMMdd").parse("20180602");
        Date endDate = new SimpleDateFormat("yyyyMMdd").parse("20180606");
        List<Service> foundServices = repository.findByCreateAtAfterAndCreateAtBefore(startDate, endDate);
        assertEquals(foundServices.size(), 1);
        assertEquals(foundServices.get(0), serviceB);

        startDate = new SimpleDateFormat("yyyyMMdd").parse("20180506");
        endDate = new SimpleDateFormat("yyyyMMdd").parse("20180606");
        foundServices = repository.findByCreateAtAfterAndCreateAtBefore(startDate, endDate);
        services.sort(Comparator.comparing(Service::getId));
        foundServices.sort(Comparator.comparing(Service::getId));
        assertEquals(foundServices.size(), 2);
        assertEquals(foundServices, services);

        startDate = new SimpleDateFormat("yyyyMMdd").parse("20180606");
        endDate = new SimpleDateFormat("yyyyMMdd").parse("20180607");
        foundServices = repository.findByCreateAtAfterAndCreateAtBefore(startDate, endDate);
        assertTrue(foundServices.isEmpty());
    }

    @Test
    @SneakyThrows
    public void testFindByCreateAtBetween() {
        final List<Service> services = Arrays.asList(serviceA, serviceB);
        this.repository.saveAll(services);

        Date startDate = new SimpleDateFormat("yyyyMMdd").parse("20180602");
        Date endDate = new SimpleDateFormat("yyyyMMdd").parse("20180606");
        List<Service> foundServices = repository.findByCreateAtBetween(startDate, endDate);
        assertEquals(foundServices.size(), 1);
        assertEquals(foundServices.get(0), serviceB);

        startDate = new SimpleDateFormat("yyyyMMdd").parse("20180601");
        endDate = new SimpleDateFormat("yyyyMMdd").parse("20180604");
        foundServices = repository.findByCreateAtBetween(startDate, endDate);
        services.sort(Comparator.comparing(Service::getId));
        foundServices.sort(Comparator.comparing(Service::getId));
        assertEquals(foundServices.size(), 2);
        assertEquals(foundServices, services);

        startDate = new SimpleDateFormat("yyyyMMdd").parse("20180606");
        endDate = new SimpleDateFormat("yyyyMMdd").parse("20180607");
        foundServices = repository.findByCreateAtBetween(startDate, endDate);
        assertTrue(foundServices.isEmpty());
    }
}

