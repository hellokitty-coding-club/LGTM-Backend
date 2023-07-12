package swm.hkcc.LGTM.app.modules.review.domain;


import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_review_id")
    private Long keywordReviewId;


    @Column(name = "description", length = 500)
    private String description;
}
