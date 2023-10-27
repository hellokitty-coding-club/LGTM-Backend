package swm.hkcc.LGTM.app.modules.notification.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;
import swm.hkcc.LGTM.app.modules.member.domain.Member;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String title;
    private String body;

    @ColumnDefault("false")
    private boolean isRead;

    public static Notification from(
            Member member,
            Map<String, String> data
    ) {
        return Notification.builder()
                .title(data.getOrDefault("title", ""))
                .body(data.getOrDefault("body", ""))
                .member(member)
                .isRead(false)
                .build();
    }
}
