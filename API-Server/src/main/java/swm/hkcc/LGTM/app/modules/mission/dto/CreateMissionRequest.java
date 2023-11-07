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
    @URL(message = "저장소 URL 형식이 올바르지 않습니다.")
    private String missionRepositoryUrl;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100자 이내로 입력해주세요.")
    private String title;

    @NotNull(message = "태그는 최소 1개 이상이어야 합니다.")
    protected List<String> tagList;

    @NotBlank(message = "설명을 입력해주세요.")
    @Size(max = 1000, message = "설명은 1000자 이내로 입력해주세요.")
    private String description;

    @Size(max = 1000, message = "추천하는 사람은 1000자 이내로 입력해주세요.")
    private String recommendTo;

    @Size(max = 1000, message = "추천하지 않는 사람은 1000자 이내로 입력해주세요.")
    private String notRecommendTo;

    @NotNull(message = "등록 마감일을 입력해주세요.")
    @FutureOrPresent(message = "등록 마감일은 오늘 또는 이후여야 합니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate registrationDueDate;

    @NotNull(message = "가격을 입력해주세요.")
    @PositiveOrZero(message = "가격은 0 이상이어야 합니다.")
    private Integer price;

    @NotNull(message = "최대 인원을 입력해주세요.")
    @Positive(message = "최대 인원은 1 이상이어야 합니다.")
    private Integer maxPeopleNumber;

    public void trimTitle() {
        this.title = this.title.trim();
    }
}
