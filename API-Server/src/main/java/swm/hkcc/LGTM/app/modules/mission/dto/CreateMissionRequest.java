package swm.hkcc.LGTM.app.modules.mission.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate registrationDueDate;

    @NotNull
    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate assignmentDueDate;

    @NotNull
    @FutureOrPresent(message = "리뷰 마감일은 현재 날짜보다 미래여야 합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate reviewCompletationDueDate;

    @NotNull
    @Positive
    private Integer price;

    @NotNull
    @Positive
    private Integer maxPeopleNumber;
}
