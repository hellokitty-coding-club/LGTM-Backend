package swm.hkcc.LGTM.app.modules.chat.domain;

import jakarta.persistence.*;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;

public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    @Enumerated(EnumType.STRING)
    private ChatRoom type;

    @Column(nullable = false)
    private String kafkaTopicName;

    @Column(nullable = false)
    private String kafkaPartitionKey;

}
