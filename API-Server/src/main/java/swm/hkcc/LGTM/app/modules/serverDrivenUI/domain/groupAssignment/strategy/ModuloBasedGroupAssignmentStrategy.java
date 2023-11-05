package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.groupAssignment.strategy;

import org.springframework.stereotype.Component;

@Component
public class ModuloBasedGroupAssignmentStrategy implements GroupAssignmentStrategy {
    private static final String GROUP_A = "A";
    private static final String GROUP_B = "B";

    @Override
    public String assignGroup(Long memberId) {
        return memberId % 2 == 0 ? GROUP_A : GROUP_B;
    }
}
