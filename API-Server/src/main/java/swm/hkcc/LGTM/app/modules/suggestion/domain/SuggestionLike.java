package swm.hkcc.LGTM.app.modules.suggestion.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;
import swm.hkcc.LGTM.app.modules.member.domain.Member;

import java.io.Serializable;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuggestionLike extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suggestion_like_id")
    private Long suggestionLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggestion_id")
    private Suggestion suggestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
