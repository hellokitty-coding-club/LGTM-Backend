package swm.hkcc.chat.app.modules.chat.domain;


import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "ChatMessage")
public class ChatMessage {

    @Id
    private String messageId;

    private MessageType messageType;

    @Indexed
    private Long roomId;

    private String sender;

    private String message;

    @Indexed
    private LocalDateTime time;

    public enum MessageType {
        ENTER, LEAVE, TALK;
    }
}