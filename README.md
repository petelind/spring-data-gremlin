# Spring Data Gremlin

Spring Data Gremlin provides Spring Data repository support for graph databases using the Apache TinkerPop Gremlin query language, including Azure Cosmos DB Gremlin API.

This project is community-maintained and targets modern Spring Boot 3.5+ applications.

## Version matrix

| Spring Boot | spring-data-gremlin | Gremlin driver | Java |
|-------------|---------------------|----------------|------|
| 3.5.x       | 3.0.x               | 3.8.1          | 17–21 |

## Cosmos DB compatibility

This release uses the latest Apache TinkerPop Gremlin driver (3.8.x). Azure Cosmos DB Gremlin officially recommends older driver versions (3.4.13) and may not fully support newer serializers or driver features. **Cosmos does not support GraphSON v3** — use `GRAPHSON_V2` (set `GREMLIN_SERIALIZER=GRAPHSON_V2` or `gremlin.serializer: GRAPHSON_V2`). Validate against your Cosmos account before production use. See [Cosmos Gremlin compatibility](https://learn.microsoft.com/en-us/azure/cosmos-db/gremlin/support).

## Quick start (Spring Boot REST)

Add the starter:

```xml
<dependency>
    <groupId>com.microsoft.spring.data.gremlin</groupId>
    <artifactId>spring-data-gremlin-spring-boot-starter</artifactId>
    <version>3.0.0-SNAPSHOT</version>
</dependency>
```

Configure Gremlin (Cosmos example):

```yaml
gremlin:
  endpoint: account.gremlin.cosmos.azure.com
  port: 443
  username: /dbs/your-db/colls/your-graph
  password: ${GREMLIN_PASSWORD}
  ssl-enabled: true
  serializer: ${GREMLIN_SERIALIZER:GRAPHSON_V2}
  telemetry-allowed: false
```

Enable repositories and use Spring Web:

```java
@SpringBootApplication
@EnableGremlinRepositories
public class GraphApplication {
    public static void main(String[] args) {
        SpringApplication.run(GraphApplication.class, args);
    }
}

@RestController
@RequestMapping("/people")
public class PersonController {
    private final PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> find(@PathVariable String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
```

See [examples/web-service](examples/web-service) for a full REST sample.

## Modules

- `spring-data-gremlin` — core Spring Data Gremlin library
- `spring-data-gremlin-spring-boot-starter` — Boot auto-configuration
- `examples` — sample CLI and web-service applications

## Building

Requires Java 17+ and Maven 3.9+.

```bash
mvn verify -Dskip.integration.tests=true
```

Integration tests (requires Gremlin Server 3.8.x on port 8889):

```bash
mvn verify -Pfull-test -pl spring-data-gremlin
```

Cosmos DB Gremlin smoke test (requires `GREMLIN_*` environment variables):

```bash
source .cosmos-gremlin.env   # or export GREMLIN_ENDPOINT, GREMLIN_USERNAME, GREMLIN_PASSWORD, etc.
mvn verify -Pcosmos -pl spring-data-gremlin
```

Or use `scripts/run-cosmos-tests.sh`, which sources `.cosmos-gremlin.env` when present.

Run the REST demo against Cosmos:

```bash
source .cosmos-gremlin.env
mvn -pl examples/web-service spring-boot:run
```

Then `PUT http://localhost:8080/people/alice` with body `{"id":"alice","name":"Alice"}`.

## Configuration properties

| Property | Description | Default |
|----------|-------------|---------|
| `gremlin.endpoint` | Gremlin server hostname | — |
| `gremlin.port` | Port | `443` |
| `gremlin.username` | Auth username (Cosmos: `/dbs/db/colls/graph`) | — |
| `gremlin.password` | Auth password / key | — |
| `gremlin.ssl-enabled` | Enable TLS | `true` |
| `gremlin.serializer` | TinkerPop serializer name | `GRAPHSON_V3` |
| `gremlin.max-content-length` | Max response size | `65536` |
| `gremlin.telemetry-allowed` | Send Microsoft telemetry | `false` |

## Features

- Spring Data `CrudRepository` for vertices, edges, and graphs
- `@Vertex`, `@Edge`, `@Graph` entity mapping
- Query methods (`findByName`, etc.)
- Manual configuration via `AbstractGremlinConfiguration` (advanced)

## License

MIT
