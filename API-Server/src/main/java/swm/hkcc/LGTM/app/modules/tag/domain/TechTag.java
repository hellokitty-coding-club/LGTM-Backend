package swm.hkcc.LGTM.app.modules.tag.domain;

import jakarta.persistence.*;
import lombok.*;
import swm.hkcc.LGTM.app.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_tag_id")
    private Long techTagId;

    @Column(nullable = false, unique = true)
    private String name;
}