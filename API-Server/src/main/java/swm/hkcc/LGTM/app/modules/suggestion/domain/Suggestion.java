package swm.hkcc.LGTM.app.modules.suggestion.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.suggestion.dto.CreateSuggestionRequest;

import java.io.Serializable;

@Entity
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Suggestion extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suggestion_id")
    private Long suggestionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    public static Suggestion from(CreateSuggestionRequest requestBody, Member writer) {
        return Suggestion.builder()
                .title(requestBody.getTitle().trim())
                .description(requestBody.getDescription())
                .writer(writer)
                .build();
    }
}
