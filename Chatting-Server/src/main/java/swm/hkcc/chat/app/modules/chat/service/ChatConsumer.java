package swm.hkcc.chat.app.modules.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import swm.hkcc.chat.app.modules.chat.domain.ChatMessage;
import swm.hkcc.chat.app.modules.chat.repository.ChatRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatConsumer {
    private final SimpMessageSendingOperations template;
    private final ChatRepository chatRepository;

    @KafkaListener(topics = "${spring.kafka.topic.chat-message}", groupId = "fsdf")
    public void consume(
            @Payload ChatMessage chat,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        String str_roomId = String.valueOf(chat.getRoomId());
        template.convertAndSend("/sub/chatroom/detail/" + str_roomId, chat);
    }


    @KafkaListener(topics = "${spring.kafka.topic.chat-message}", groupId = "${spring.kafka.group-id.chat-message-repository}")
    public void persist(
            @Payload ChatMessage chat,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long ts
    ) {
        chatRepository.save(chat);
    }
}
