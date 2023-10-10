package swm.hkcc.LGTM.app.modules.userLog.producer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.userLog.dto.TimeIntervalLogMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntervalLogProducer {

    private final KafkaTemplate<String, TimeIntervalLogMessage> kafkaTemplate;

    @Value("${spring.kafka.topic.user-log}")
    private String TOPIC_NAME;
    private final static String KEY = "interval";

    public String sendMessage(TimeIntervalLogMessage data) {
        Message<TimeIntervalLogMessage> message = MessageBuilder
                .withPayload(data)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .setHeader(KafkaHeaders.KEY, KEY)
                .build();

        kafkaTemplate.send(message);
        return TOPIC_NAME;
    }
}
