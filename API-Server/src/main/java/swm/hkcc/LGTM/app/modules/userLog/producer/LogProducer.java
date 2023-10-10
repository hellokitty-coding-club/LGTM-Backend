package swm.hkcc.LGTM.app.modules.userLog.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.userLog.dto.LogMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogProducer {

    private final KafkaTemplate<String, LogMessage> kafkaTemplate;

    @Value("${spring.kafka.topic.user-log}")
    private String TOPIC_NAME;

    public String sendMessage(LogMessage data) {
        Message<LogMessage> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .build();

        kafkaTemplate.send(message);

        return TOPIC_NAME;
    }
}
