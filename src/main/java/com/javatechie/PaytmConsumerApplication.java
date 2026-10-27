package com.javatechie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javatechie.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class PaytmConsumerApplication {


    @Autowired
    Environment env;

    public static void main(String[] args) {
        SpringApplication.run(PaytmConsumerApplication.class, args);
    }

    @KafkaListener(topics = "${paytm.consumer.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void paymentConsumer1(PaymentRequest paymentRequest) throws JsonProcessingException {
        //business logic
        log.info("Consumer listening on port {}", env.getProperty("server.port", "9000"));
        log.info("paymentConsumer1 consumed message {} ", new ObjectMapper().writeValueAsString(paymentRequest));
    }

    @KafkaListener(topics = "${paytm.consumer.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void paymentConsumer2(PaymentRequest paymentRequest) throws JsonProcessingException {
        //business logic
        log.info("Consumer listening on port {}", env.getProperty("server.port", "9000"));
        log.info("paymentConsumer2 consumed message {} ", new ObjectMapper().writeValueAsString(paymentRequest));
    }

}
