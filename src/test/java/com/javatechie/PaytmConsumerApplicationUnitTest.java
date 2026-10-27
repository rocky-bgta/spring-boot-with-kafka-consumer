package com.javatechie;

import com.javatechie.dto.PaymentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(OutputCaptureExtension.class)
class PaytmConsumerApplicationUnitTest {

    @Test
    void bothListenersUseConfigurableTopicAndSharedGroup() throws Exception {
        for (String methodName : new String[]{"paymentConsumer1", "paymentConsumer2"}) {
            KafkaListener listener = PaytmConsumerApplication.class
                    .getDeclaredMethod(methodName, PaymentRequest.class)
                    .getAnnotation(KafkaListener.class);
            assertNotNull(listener);
            assertArrayEquals(new String[]{"${paytm.consumer.topic.name}"}, listener.topics());
            assertEquals("${spring.kafka.consumer.group-id}", listener.groupId());
        }
    }

    @Test
    void bothHandlersCanProcessPaymentPayloadWithoutBroker(CapturedOutput output) throws Exception {
        PaytmConsumerApplication application = new PaytmConsumerApplication();
        ReflectionTestUtils.setField(application, "env", new MockEnvironment().withProperty("server.port", "9000"));
        PaymentRequest payment = new PaymentRequest("tx-123", "source", "destination", 25, null);

        application.paymentConsumer1(payment);
        application.paymentConsumer2(payment);

        assertTrue(output.getOut().contains("paymentConsumer1 consumed message"));
        assertTrue(output.getOut().contains("paymentConsumer2 consumed message"));
        assertTrue(output.getOut().contains("tx-123"));
    }
}
