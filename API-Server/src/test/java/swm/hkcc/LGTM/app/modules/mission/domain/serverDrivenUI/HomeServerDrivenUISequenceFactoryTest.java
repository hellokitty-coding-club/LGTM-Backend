package swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.global.exception.GeneralException;
import swm.hkcc.LGTM.app.modules.mission.constant.HomeServerDrivenUISequenceByVersion;
import swm.hkcc.LGTM.app.modules.mission.constant.MissionContentType;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionContentSequence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HomeServerDrivenUISequenceFactoryTest {

    private HomeServerDrivenUISequenceFactory homeServerDrivenUISequenceFactory;

    @BeforeEach
    public void setup() {
        // Given
        homeServerDrivenUISequenceFactory = new HomeServerDrivenUISequenceFactory();
    }

    @Test
    @DisplayName("valid한 groupName을 입력했을 때, 해당하는 MissionContentSequence를 반환한다.")
    public void testGetServerDrivenUISequenceByVersion_ValidVersion() {
        // Given
        String validGroupName = "A";
        List<MissionContentType> contents = HomeServerDrivenUISequenceByVersion.A_HOME_SERVER_DRIVEN_UI_SEQUENCE.getContents();

        // When
        MissionContentSequence result = homeServerDrivenUISequenceFactory.getServerDrivenUISequence(validGroupName);

        // Then
        assertNotNull(result);
        assertEquals(contents, result.getMissionContents());
    }

    @Test
    @DisplayName("invalid한 groupName을 입력했을 때, Exception이 발생한다.")
    public void testGetServerDrivenUISequenceByVersion_InvalidVersion() {
        // Given
        String invalidGroupName = "invalidGroupName";

        // When & Then
        assertThrows(GeneralException.class,
                () -> homeServerDrivenUISequenceFactory.getServerDrivenUISequence(invalidGroupName),
                ResponseCode.DATA_ACCESS_ERROR.getMessage());
    }
}
