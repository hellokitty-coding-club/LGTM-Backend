package swm.hkcc.LGTM.app.modules.mission.domain.serverDrivenUI;

import org.junit.jupiter.api.BeforeEach;
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
    public void testGetServerDrivenUISequenceByVersion_ValidVersion() {
        // Given
        int validVersion = 1;
        List<MissionContentType> contents = HomeServerDrivenUISequenceByVersion.V1_HOME_SERVER_DRIVEN_UI_SEQUENCE.getContents();

        // When
        MissionContentSequence result = homeServerDrivenUISequenceFactory.getServerDrivenUISequenceByVersion(validVersion);

        // Then
        assertNotNull(result);
        assertEquals(contents, result.getMissionContents());
    }

    @Test
    public void testGetServerDrivenUISequenceByVersion_InvalidVersion() {
        // Given
        int invalidVersion = -1;

        // When & Then
        assertThrows(GeneralException.class,
                () -> homeServerDrivenUISequenceFactory.getServerDrivenUISequenceByVersion(invalidVersion),
                ResponseCode.DATA_ACCESS_ERROR.getMessage());
    }
}
