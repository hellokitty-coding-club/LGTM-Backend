package swm.hkcc.chat.app.modules.chat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import swm.hkcc.chat.app.modules.chat.domain.ChatMessage;

public interface ChatRepository extends MongoRepository<ChatMessage, String> {
}
