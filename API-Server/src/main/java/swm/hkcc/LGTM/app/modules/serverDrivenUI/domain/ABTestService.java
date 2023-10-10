package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ABTestService {
    private final ABTestUserGroupRepository abTestUserGroupRepository;

    public String getGroupName(Long memberId, String testName) {
        return abTestUserGroupRepository.findGroupNameByMemberIdAndTestName(memberId, testName)
                .orElse("A");
    }
}
