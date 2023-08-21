package swm.hkcc.LGTM.app.modules.mission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class MissionDetailViewResponse {
    @NotBlank
    private Long missionId;

    @NotBlank
    private String missionStatus;

    @NotBlank
    private String missionTitle;

    @NotBlank
    private List<TechTag> techTagList;

    @NotBlank
    private String missionRepositoryUrl;

    @NotBlank
    private LocalDate registrationDueDate;

    @NotBlank
    private int maxPeopleNumber;

    @NotBlank
    private int currentPeopleNumber;

    @NotBlank
    private int price;

    @NotBlank
    private String description;

    @NotNull
    private String recommendTo;

    @NotNull
    private String notRecommendTo;

    @NotBlank
    private boolean isScraped;

    @NotBlank
    private String memberType;

    @NotBlank
    private MemberProfile memberProfile;
}

