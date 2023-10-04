package swm.hkcc.chat.app.modules.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import swm.hkcc.chat.app.modules.chat.model.ChatDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatProducer {

    private final KafkaTemplate<String, ChatDto> kafkaTemplate;

    @Value("${spring.kafka.topic.chat-message}")
    private String TOPIC_NAME;

    public void sendChatMessage(ChatDto chatDto) {
        Message<ChatDto> message = MessageBuilder
                .withPayload(chatDto)
                .setHeader(KafkaHeaders.KEY, chatDto.getRoomId())
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .build();

        kafkaTemplate.send(message);
    }
}
