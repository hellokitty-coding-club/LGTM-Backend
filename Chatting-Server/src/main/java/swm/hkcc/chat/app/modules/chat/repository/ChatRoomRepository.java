package swm.hkcc.chat.app.modules.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import swm.hkcc.chat.app.modules.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
