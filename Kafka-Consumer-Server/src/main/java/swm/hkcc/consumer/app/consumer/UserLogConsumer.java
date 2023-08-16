package swm.hkcc.consumer.app.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import swm.hkcc.consumer.app.dto.TimestampLogMessage;

@Service
@Slf4j
public class UserLogConsumer {

    @KafkaListener(topics = "${spring.kafka.topic.user-log}", groupId = "${spring.kafka.group-id.user-log}")
    public void consume(TimestampLogMessage message){
        log.info(String.format("Json message recieved -> %s", message.toString()));
    }
}
