# Catalog Service
[![Catalog Service CI/CD](https://github.com/fragaLY/catalog-service/actions/workflows/catalog-service.yml/badge.svg?branch=master)](https://github.com/fragaLY/catalog-service/actions/workflows/catalog-service.yml)

## The catalog service is the aggregator for different providers.

## System Design for the final solution

## How to run

Data is stored and handled by ELK. For caches could be used Redis, only Redis supports reactive caching. See docker-compose.local.yml:

```yml
version: "3.8"

services:
  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:${STACK_VERSION}
    container_name: elasticsearch
    environment:
      - xpack.security.enabled=false
      - discovery.type=single-node
    ulimits:
      memlock:
        soft: -1
        hard: -1
      nofile:
        soft: 65536
        hard: 65536
    cap_add:
      - IPC_LOCK
    volumes:
      - elasticsearch-data:/usr/share/elasticsearch/data
    ports:
      - ${ES_PORT}:9200

  kibana:
    container_name: kibana
    image: docker.elastic.co/kibana/kibana:${STACK_VERSION}
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
    ports:
      - ${KIBANA_PORT}:5601
    depends_on:
      - elasticsearch

  redis:
    image: redis:${REDIS_VERSION}
    restart: always
    environment:
      - ALLOW_EMPTY_PASSWORD=yes
      - REDIS_PASSWORD=${REDIS_PASSWORD}
    ports:
      - ${REDIS_PORT}:6379
    command: redis-server --save 20 1 --loglevel warning
    volumes:
      - cache:/data

volumes:
  elasticsearch-data:
    driver: local
  cache:
    driver: local
```

Feel free to run it to work localy. 
To boot up the application use ```./gradlew bootRun``` command or pull the image ```docker pull fragaly/catalog-service:1.0.0-RC1```.

See the Open Api documentation to be familiar with service API:

```yaml
openapi: "3.0.3"
info:
  title: "catalog_service API"
  description: "catalog_service API"
  version: "1.0.0"
servers:
  - url: "https://localhost:8080"
paths:
  /api/v1/equipments:
    get:
      summary: "GET api/v1/equipments"
      operationId: "get"
      parameters:
        - name: "parameters"
          in: "query"
          required: false
          schema:
            type: "string"
        - name: "page"
          in: "query"
          required: false
          schema:
            type: "integer"
            format: "int32"
            default: "0"
        - name: "size"
          in: "query"
          required: false
          schema:
            type: "integer"
            format: "int32"
            default: "20"
      responses:
        "200":
          description: "OK"
          content:
            '*/*':
              schema:
                type: "array"
                items:
                  $ref: "#/components/schemas/Equipment"
  /api/v1/data/process:
    post:
      summary: "POST api/v1/data/process"
      operationId: "process"
      requestBody:
        content:
          application/json:
            schema:
              type: "string"
      responses:
        "200":
          description: "OK"
          content:
            '*/*':
              schema:
                type: "array"
                items:
                  $ref: "#/components/schemas/Equipment"

components:
  schemas:
    BigDecimal:
      type: "object"
      properties: { }
    Equipment:
      type: "object"
      properties:
        id:
          type: "string"
        brandName:
          type: "string"
        details:
          type: "string"
        sizes:
          type: "array"
          items:
            type: "string"
        mrp:
          type: "string"
        sellPrice:
          $ref: "#/components/schemas/BigDecimal"
        discount:
          $ref: "#/components/schemas/BigDecimal"
        category:
          type: "string"
    DataProcessingPayload:
      type: "object"
      properties:
        uris:
          type: "array"
          items:
            type: "string"
            format: "uri"
```

## Browser compatibility

The latest versions and the previous one for [IDENTIFY].
Telegram Web.

## Performance

| Definition                          | Option |
| :---                                | :---:  |
| Average Page Load Time              | `300ms`|
| Max Page Load Time                  | `500ms`|
| Average Search Result Time          | `1s`   |
| Max Search Result Time              | `3s`   |
| Max Page Save Time                  | `300ms`|
| Caching CDN                         | `95%`  |
| Backend Cache Hit Ratio             | `95%`  |
| Browser Cache - Cache Hit Ratio     | `95%`  |

## Testability

| Definition                          | Option |
| :---                                | :---:  |
| Minimized manual testing            | `yes`  |
| Unit test coverage                  | `~80%`|
| Integration test coverage           | `~100%`|

## Security

| Definition                          | Option |
| :---                                | :---:  |
| DAST scanning                       | `yes`  |
| SAST scanning                       | `yes`  |

## Integrity

| Definition                          | Option |
| :---                                | :---:  |
| Back up                             | `Production DB should be backed up at least once per day to prevent data loss.`  |
| Recovery plan                       | `Tested recovery procedures to ensure data integrity in case of system failures or disasters.`  |
| Audit log                           | `All inserts, modifications and deletes of data must be appropriately audited so that every change can be tracked to identify what the change was and who performed the change.`  |
| Version Control                     | `It should be possible to track any changes to code and configuration, maintaining a record of who made the changes and when.`  |
| Access Control                      | `Strict access controls and user permissions must be enforced to ensure that only authorized users can access and modify data and system functions.`  |
| Data Validation                     | `All user inputs must be rigorously validated to prevent the entry of erroneous or malicious data into the system.`  |

## Availability

| Definition                                    | Option |
| :---                                          | :---:  |
| High availability	Uptime                      | `99.9` |
| Health checks (liveness and readiness probes) | `yes`  |

## Scalability

| Definition                          | Option                                     |
| :---                                | :---:                                      |
| Number of users in the nearest year | `10_000`                                   |
| Number of users in 1-3 years        | `100_000`                                  |
| Number of users in 2030             | 1% of Telegram Expected Users `15_000_000` |

##	Capacity

| Definition                                              | Target   | Supported |
| :---                                                    | :---:    | :---:     |
| Number of concurrent users                              | `10_000` | `TBD`     |
| Number of supported concurrent transactions in a second | `50_000` | `TBD`     |

## Data storage volume

Client (~ 469 bytes per record)

| Column           | Type        | Size (bytes) |
| :---             | :---        | :---:        |
|id                | UUID        |   `16`       |
|telegram_id       | BIGINT      |   `8`        |
|first_name        | VARCHAR(64) |   `65`       |
|last_name         | VARCHAR(64) |   `65`       |
|telegram_username | VARCHAR(32) |   `33`       |
|is_bot            | BOOLEAN     |   `1`        |
|is_premium        | BOOLEAN     |   `1`        |
|image             | VARCHAR(255)|   `256`      |
|authenticated_at  | TIMESTAMP   |   `8`        |
|created_at        | TIMESTAMP   |   `8`        |
|updated_at        | TIMESTAMP   |   `8`        |

Lottery (~ 72 bytes per record)

| Column           | Type        | Size (bytes) |
| :---             | :---        | :---:        |
|id                | UUID        |   `16`       |
|winner_id         | UUID        |   `16`       |
|type              | VARCHAR(7)  |   `8`        |
|status            | VARCHAR(11) |   `12`       |
|start_date        | DATE        |   `4`        |
|created_at        | TIMESTAMP   |   `8`        |
|updated_at        | TIMESTAMP   |   `8`        |

Client's Lotteries (~ 32 bytes per record)

| Column           | Type        | Size (bytes) |
| :---             | :---        | :---:        |
|client_id         | UUID        |   `16`       |
|lottery_id        | UUID        |   `16`       |

Total Volume Per Year At Max

| Entity           | Amount              | Total Size |
| :---             | :---                | :---:      |
|client            | ~15_000_000         |   `7GB`    |
|lottery           | ~500                |   `36KB`   |
|clients_lotteries | clients * lotteries |   `240GB`  |

In total `~250 GB` per year at max.
3 replicas and `~750 GB` of data.
The data will be archived once per year.

## Bandwidth, Latency, and Throughput

Typical latencies:
- Reading 1 MB from RAM: 0.25 ms
- Reading 1 MB from SSD: 1 ms
- Transfer 1 MB over network: 10 ms
- Reading 1 MB from HDD: 20 ms
- Inter-continental round trip: 150 ms

Taking in advance the targeted transactions per second that should be handled ~50_000, we can calculate the latency by default in non-cached request at max 200ms.
10_000 clients * 5 lotteries * 600 bytes = 30 mb/s.