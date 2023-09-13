package swm.hkcc.LGTM.app.modules.registration.domain.additionalInfoProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JuniorInfoProviderFactory {
    private final JuniorDefaultInfoProvider defaultInfoProvider = new JuniorDefaultInfoProvider();

    private final Map<ProcessStatus, JuniorInfoProvider> providerMap = new ConcurrentHashMap<>();

    public JuniorInfoProviderFactory(JuniorAccountInfoProvider accountInfoProvider, JuniorPullRequestInfoProvider prInfoProvider, JuniorFeedbackInfoProvider feedbackInfoProvider) {
        providerMap.put(ProcessStatus.WAITING_FOR_PAYMENT, accountInfoProvider);
        providerMap.put(ProcessStatus.PAYMENT_CONFIRMATION, accountInfoProvider);

        providerMap.put(ProcessStatus.CODE_REVIEW, prInfoProvider);
        providerMap.put(ProcessStatus.MISSION_FINISHED, prInfoProvider);

        providerMap.put(ProcessStatus.FEEDBACK_REVIEWED, feedbackInfoProvider);
    }

    public JuniorInfoProvider getProvider(ProcessStatus status) {
        return providerMap.getOrDefault(status, defaultInfoProvider);
    }

}
