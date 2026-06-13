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

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.service.springdata.gremlin.domain.Person;
import web.service.springdata.gremlin.repository.PersonRepository;

@RestController
@RequestMapping("/people")
public class PersonController {

  private final PersonRepository personRepository;

  public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Person> find(@PathVariable("id") String id) {
    return personRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<Person> findAll() {
    return (List<Person>) personRepository.findAll(Person.class);
  }

  @PutMapping("/{id}")
  public Person save(@PathVariable("id") String id, @RequestBody Person person) {
    if (!person.getId().equals(id)) {
      person.setId(id);
    }
    ensurePartitionKey(person);
    personRepository.save(person);
    return person;
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") String id) {
    personRepository.deleteById(id);
  }

  private void ensurePartitionKey(Person person) {
    if (person.getPartitionKey() == null || person.getPartitionKey().isBlank()) {
      person.setPartitionKey(Person.DEFAULT_PARTITION_KEY);
    }
  }
}
