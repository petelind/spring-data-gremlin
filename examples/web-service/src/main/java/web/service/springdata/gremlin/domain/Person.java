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
package web.service.springdata.gremlin.domain;

import com.microsoft.spring.data.gremlin.annotation.Vertex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Vertex(label = "person")
@AllArgsConstructor
@NoArgsConstructor
public class Person {

  public static final String DEFAULT_PARTITION_KEY = "demo";

  @Id
  private String id;

  private String name;

  private String partitionKey = DEFAULT_PARTITION_KEY;

  public Person(String id, String name) {
    this.id = id;
    this.name = name;
    this.partitionKey = DEFAULT_PARTITION_KEY;
  }
}
