package swm.hkcc.LGTM.app.modules.mission.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateMissionRequest {
    @NotBlank
    @URL
    private String missionRepositoryUrl;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private MissionStatus missionStatus; // todo: enum

    @NotNull
    protected List<String> tagList;

    @NotBlank
    @URL
    private String thumbnailImageUrl;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotBlank
    @Size(max = 1000)
    private String reomnnandTo;

    @NotBlank
    @Size(max = 1000)
    private String notReomnnandTo;

    @NotNull
    @FutureOrPresent
    private LocalDateTime registrationDueDate;

    @NotNull
    @FutureOrPresent
    private LocalDateTime assignmentDueDate;

    @NotNull
    @FutureOrPresent
    private LocalDateTime reviewCompletationDueDate;

    @NotNull
    @Positive
    private Integer price;

    @NotNull
    @Positive
    private Integer maxPeopleNumber;
}
