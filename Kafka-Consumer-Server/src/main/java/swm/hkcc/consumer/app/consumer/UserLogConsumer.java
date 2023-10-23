package swm.hkcc.consumer.app.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import swm.hkcc.consumer.app.dto.LogMessage;
import swm.hkcc.consumer.app.repository.UserDataLogRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserLogConsumer {

    private final UserDataLogRepository userDataLogRepository;

    @KafkaListener(topics = "${spring.kafka.topic.user-log-dev}", groupId = "${spring.kafka.group-id.user-log-dev}")
    public void consume(
            @Payload LogMessage message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        log.info(String.format("Json message recieved -> %s", message.toString()));
        userDataLogRepository.save(message.toUserLog(topic, partition, ts));
    }

    @KafkaListener(topics = "${spring.kafka.topic.user-log-prod}", groupId = "${spring.kafka.group-id.user-log-prod}")
    public void consumeProd(
            @Payload LogMessage message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        log.info(String.format("Json message recieved -> %s", message.toString()));
        userDataLogRepository.save(message.toUserLog(topic, partition, ts));
    }
}
