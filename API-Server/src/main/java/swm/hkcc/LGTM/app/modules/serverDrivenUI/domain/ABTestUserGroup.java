package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ABTestUserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long abTestUserGroupId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String testName;

    @Column(nullable = false)
    private String groupName;
}
