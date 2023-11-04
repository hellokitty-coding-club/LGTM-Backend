package swm.hkcc.LGTM.app.modules.serverDrivenUI.domain.groupAssignment.strategy;

import org.springframework.stereotype.Component;

@Component
public class RandomGroupAssignmentStrategy implements GroupAssignmentStrategy {
    private static final String GROUP_A = "A";
    private static final String GROUP_B = "B";
    @Override
    public String assignGroup(Long memberId) {
        return Math.random() < 0.5 ? GROUP_A : GROUP_B;
    }
}
