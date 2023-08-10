package swm.hkcc.LGTM.app.modules.mission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class CreateMissionRequest {
    @NotBlank
    @URL
    private String missionRepositoryUrl;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotNull
    protected List<String> tagList;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate registrationDueDate;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate assignmentDueDate;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate reviewCompletationDueDate;

    @NotNull
    @Positive
    private Integer price;

    @NotNull
    @Positive
    private Integer maxPeopleNumber;
}
