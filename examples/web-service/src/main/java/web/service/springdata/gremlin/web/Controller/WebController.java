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

package web.service.springdata.gremlin.web.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import web.service.springdata.gremlin.domain.MicroService;
import web.service.springdata.gremlin.domain.ServicesDataFlow;
import web.service.springdata.gremlin.repository.MicroServiceRepository;
import web.service.springdata.gremlin.repository.ServicesDataFlowRepository;
import web.service.springdata.gremlin.web.domain.Greeting;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class WebController {

    private final MicroServiceRepository microServiceRepo;
    private final ServicesDataFlowRepository dataFlowRepo;
    private final AtomicLong counter = new AtomicLong();

    public WebController(MicroServiceRepository microServiceRepo,
                         ServicesDataFlowRepository dataFlowRepo) {
        this.microServiceRepo = microServiceRepo;
        this.dataFlowRepo = dataFlowRepo;
    }

    @GetMapping("/greeting")
    public Greeting greeting() {
        return new Greeting(String.valueOf(this.counter.incrementAndGet()), "Greetings to User.");
    }

    @GetMapping("/services/{id}")
    public ResponseEntity<MicroService> getService(@PathVariable String id) {
        return microServiceRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/services/{id}")
    public MicroService putService(@PathVariable String id, @RequestBody MicroService service) {
        if (!service.getId().equals(id)) {
            service.setId(id);
        }
        ensurePartitionKey(service);
        microServiceRepo.save(service);
        return service;
    }

    @DeleteMapping("/services/{id}")
    public void deleteService(@PathVariable String id) {
        microServiceRepo.deleteById(id);
    }

    @DeleteMapping("/services/")
    public void deleteService(@RequestBody MicroService service) {
        microServiceRepo.delete(service);
    }

    @DeleteMapping("/services/all")
    public void deleteServicesAll() {
        microServiceRepo.deleteAll();
    }

    @PutMapping("/services/")
    public MicroService putService(@RequestBody MicroService service) {
        ensurePartitionKey(service);
        microServiceRepo.save(service);
        return service;
    }

    @GetMapping("/services/")
    public List<MicroService> getServiceList() {
        return (List<MicroService>) microServiceRepo.findAll(MicroService.class);
    }

    @PutMapping("/services/create/{id}")
    public MicroService createService(@PathVariable String id, @RequestBody MicroService service) {
        return putService(id, service);
    }

    @PutMapping("/services/create/")
    public MicroService createService(@RequestBody MicroService service) {
        return putService(service);
    }

    @GetMapping("/dataflow/{id}")
    public ResponseEntity<ServicesDataFlow> getServicesDataFlow(@PathVariable String id) {
        return dataFlowRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/dataflow/{id}")
    public ServicesDataFlow putServicesDataFlow(@PathVariable String id, @RequestBody ServicesDataFlow dataFlow) {
        if (!dataFlow.getId().equals(id)) {
            dataFlow.setId(id);
        }
        dataFlowRepo.save(dataFlow);
        return dataFlow;
    }

    @PutMapping("/dataflow/")
    public ServicesDataFlow putServicesDataFlow(@RequestBody ServicesDataFlow dataFlow) {
        dataFlowRepo.save(dataFlow);
        return dataFlow;
    }

    @GetMapping("/dataflow/")
    public List<ServicesDataFlow> getServicesDataFlowList() {
        return (List<ServicesDataFlow>) dataFlowRepo.findAll(ServicesDataFlow.class);
    }

    private void ensurePartitionKey(MicroService service) {
        if (service.getPartitionKey() == null || service.getPartitionKey().isBlank()) {
            service.setPartitionKey("demo");
        }
    }
}
