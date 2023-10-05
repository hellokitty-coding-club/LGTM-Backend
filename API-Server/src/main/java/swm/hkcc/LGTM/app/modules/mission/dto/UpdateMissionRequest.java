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
public class UpdateMissionRequest {
    @NotBlank
    @URL
    private String missionRepositoryUrl;

    @NotBlank
    @Size(max = 100)
    private String title;

    protected List<String> tagList;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @Size(max = 1000)
    private String recommendTo;

    @Size(max = 1000)
    private String notRecommendTo;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate registrationDueDate;

    @PositiveOrZero
    private Integer price;

    @Positive
    private Integer maxPeopleNumber;

    public void trimTitle() {
        this.title = this.title.trim();
    }
}
