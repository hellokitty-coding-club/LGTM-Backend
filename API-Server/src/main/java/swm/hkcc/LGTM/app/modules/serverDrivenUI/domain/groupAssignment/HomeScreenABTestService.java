package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.groupAssignment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.ABTestUserGroup;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.ABTestUserGroupRepository;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.groupAssignment.strategy.ModuloBasedGroupAssignmentStrategy;
import swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.groupAssignment.strategy.RandomGroupAssignmentStrategy;

import static swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ABTest.HOME_SCREEN_SEQUENCE_TEST;
import static swm.hkcc.LGTM.app.modules.serverDrivenUI.constant.ABTest.HOT_MISSION_FEATURE_TEST;

@Service
@RequiredArgsConstructor
public class HomeScreenABTestService implements ABTestService {
    private final ABTestUserGroupRepository abTestUserGroupRepository;
    private final ModuloBasedGroupAssignmentStrategy moduloBasedGroupAssignmentStrategy;
    private final RandomGroupAssignmentStrategy randomGroupAssignmentStrategy;

    // TODO: 테스트 이름에 따라서 group assignment strategy를 미리 정의하고 그에 맞게 group을 할당해주는 로직을 구현
    @Override
    public String getGroupName(Long memberId, String testName) {
        return abTestUserGroupRepository.findGroupNameByMemberIdAndTestName(memberId, testName)
                .orElseGet(() -> assignGroup(memberId, testName));
    }

    private String assignGroup(Long memberId, String testName) {
        String groupName;
        if (HOT_MISSION_FEATURE_TEST.getTestName().equals(testName)) {
            groupName = useRandomGroupAssignmentStrategy(memberId);
        } else if (HOME_SCREEN_SEQUENCE_TEST.getTestName().equals(testName)) {
            groupName = useModuloBasedGroupAssignmentStrategy(memberId);
        } else {
            groupName = useRandomGroupAssignmentStrategy(memberId);
        }

        saveABTestUserGroup(memberId, testName, groupName);
        return groupName;
    }

    private String useModuloBasedGroupAssignmentStrategy(Long memberId) {
        return moduloBasedGroupAssignmentStrategy.assignGroup(memberId);
    }

    private String useRandomGroupAssignmentStrategy(Long memberId) {
        return randomGroupAssignmentStrategy.assignGroup(memberId);
    }

    private void saveABTestUserGroup(Long memberId, String testName, String groupName) {
        abTestUserGroupRepository.save(
                ABTestUserGroup.builder()
                        .memberId(memberId)
                        .testName(testName)
                        .groupName(groupName)
                        .build()
        );
    }
}
