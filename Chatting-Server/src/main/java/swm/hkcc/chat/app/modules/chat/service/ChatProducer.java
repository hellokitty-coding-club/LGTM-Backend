package swm.hkcc.chat.app.modules.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import swm.hkcc.chat.app.modules.chat.domain.ChatMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatProducer {

    private final KafkaTemplate<String, ChatMessage> kafkaTemplate;

    @Value("${spring.kafka.topic.chat-message}")
    private String TOPIC_NAME;

    public void sendChatMessage(ChatMessage chatMessage) {
        String str_roomId = String.valueOf(chatMessage.getRoomId());
        Message<ChatMessage> message = MessageBuilder
                .withPayload(chatMessage)
                .setHeader(KafkaHeaders.KEY, str_roomId)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .build();

        kafkaTemplate.send(message);
    }
}
