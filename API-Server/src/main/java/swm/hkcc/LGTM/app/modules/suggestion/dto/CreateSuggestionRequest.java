package swm.hkcc.LGTM.app.modules.suggestion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EqualsAndHashCode
public class CreateSuggestionRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100자 이내로 입력해주세요.")
    private String title;

    @NotBlank(message = "본문을 입력해주세요.")
    @Size(max = 1000, message = "본문은 1000자 이내로 입력해주세요.")
    private String description;

}
