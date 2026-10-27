# Spring Boot Kafka Consumer Demo

A **Spring Boot 2.7 / Java 8 source-compatible** consumer for the [paired producer example](https://github.com/rocky-bgta/spring-boot-with-kafka-producer). CI runs broker-free unit tests on Java 17. This is a teaching example, not a production-grade payment processor.

## Architecture and delivery semantics

```text
spring-boot-with-kafka-producer
  -> Kafka topic: payment-topic (3 partitions suggested)
  -> Payment_consumer_group
     -> paymentConsumer1(PaymentRequest)
     -> paymentConsumer2(PaymentRequest)
```

The two `@KafkaListener` methods belong to the **same consumer group**. Kafka distributes partitions among group members; it does not deliver every event to both handlers. Parallelism is limited by the number of topic partitions. Payloads are deserialized into `com.javatechie.dto.PaymentRequest` with Spring Kafka's JSON deserializer.

## Run locally

Prerequisites: JDK 8+, Maven, and a running Kafka broker with `payment-topic` created. The producer and consumer must be able to reach the same broker and topic.

| Environment variable | Default | Purpose |
| --- | --- | --- |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Kafka bootstrap address |
| `PAYMENT_TOPIC` | `payment-topic` | Topic for both listeners |
| `KAFKA_CONSUMER_GROUP` | `Payment_consumer_group` | Shared consumer group ID |

For a local Kafka CLI:

```bash
kafka-topics.sh --bootstrap-server localhost:9092 --create --if-not-exists --topic payment-topic --partitions 3 --replication-factor 1
mvn spring-boot:run
```

The application listens on port **9000**; it does not expose a payment HTTP API. Start the paired producer separately on port 9191 and POST to its `/paytm/payment` endpoint to generate example payment events.

## Tests and limitations

```bash
mvn -B -Dtest=PaytmConsumerApplicationUnitTest test
```

The unit tests verify listener topic/group configuration and invoke both handler methods with a synthetic payment request. They do not start Kafka or prove consumer-group partition assignment. The existing `@SpringBootTest` context test may require an available broker; CI deliberately selects the isolated unit test.

The demo only logs received payloads. It does **not** implement durable business processing, dead-letter topics, retries, idempotency, offset-commit policy, exactly-once semantics, or broker integration tests. The JSON trusted package is limited to `com.javatechie.dto`; do not widen it to `*` for convenience.
