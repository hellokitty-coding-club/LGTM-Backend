package swm.hkcc.LGTM.app.modules.review.domain;


import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordReviewPerReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_review_per_review_id")
    private Long keywordReviewPerReviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_review_id")
    private KeywordReview keywordReview;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;
}
