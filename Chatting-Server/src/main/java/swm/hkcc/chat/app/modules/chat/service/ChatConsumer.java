package swm.hkcc.chat.app.modules.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import swm.hkcc.chat.app.modules.chat.model.ChatDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatConsumer {
    private final SimpMessageSendingOperations template;

    @KafkaListener(topics = "${spring.kafka.topic.chat-message}", groupId = "${spring.kafka.group-id.chat-message-repository}")
    public void consume(
            @Payload ChatDto chat,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        template.convertAndSend("/sub/chatroom/detail/" + chat.getRoomId(), chat);
    }


    @KafkaListener(topics = "${spring.kafka.topic.chat-message}", groupId = "${spring.kafka.group-id.chat-message-repository}")
    public void persist(
            @Payload ChatDto chat,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        // mongoDB에 저장
    }

}
