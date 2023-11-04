package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.groupAssignment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.groupAssignment.strategy.ModuloBasedGroupAssignmentStrategy;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.groupAssignment.strategy.RandomGroupAssignmentStrategy;

import static swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ABTest.HOME_SCREEN_SEQUENCE_TEST;

@Service
@RequiredArgsConstructor
public class HomeScreenABTestService implements ABTestService {
    private final ModuloBasedGroupAssignmentStrategy moduloBasedGroupAssignmentStrategy;
    private final RandomGroupAssignmentStrategy randomGroupAssignmentStrategy;

    // TODO: 테스트 이름에 따라서 group assignment strategy를 미리 정의하고 그에 맞게 group을 할당해주는 로직을 구현
    @Override
    public String getGroupName(Long memberId, String testName) {
        if (HOME_SCREEN_SEQUENCE_TEST.getTestName().equals(testName)) {
            return useModuloBasedGroupAssignmentStrategy(memberId);
        }

        return useRandomGroupAssignmentStrategy(memberId);
    }

    private String useModuloBasedGroupAssignmentStrategy(Long memberId) {
        return moduloBasedGroupAssignmentStrategy.assignGroup(memberId);
    }

    private String useRandomGroupAssignmentStrategy(Long memberId) {
        return randomGroupAssignmentStrategy.assignGroup(memberId);
    }
}
