package swm.hkcc.consumer.app.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import swm.hkcc.consumer.app.dto.CommonMessage;
import swm.hkcc.consumer.app.dto.TimeIntervalLogMessage;
import swm.hkcc.consumer.app.dto.TimestampLogMessage;

@Service
@Slf4j
public class UserLogConsumer {

    @KafkaListener(topics = "${spring.kafka.topic.user-log}", groupId = "${spring.kafka.group-id.user-log}")
    public void consume(
            @Payload CommonMessage message,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        log.info(String.format("Json message recieved -> %s", message.getMessageByKey(key).toString()));
    }
}
