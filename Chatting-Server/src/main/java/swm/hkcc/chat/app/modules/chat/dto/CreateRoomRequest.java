package swm.hkcc.chat.app.modules.chat.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomRequest {
    private String roomName;
    private String roomType;
    private Long missionId;
    private String nickName;
    private String processStatus;
}
