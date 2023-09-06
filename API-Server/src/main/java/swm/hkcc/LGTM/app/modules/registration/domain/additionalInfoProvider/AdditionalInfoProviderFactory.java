package swm.hkcc.LGTM.app.modules.registration.domain.additionalInfoProvider;

import org.springframework.stereotype.Component;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AdditionalInfoProviderFactory {
    private final DefaultInfoProvider defaultInfoProvider;
    private final Map<ProcessStatus, AdditionalInfoProvider> providerMap = new ConcurrentHashMap<>();

    public AdditionalInfoProviderFactory(
            PaymentInfoProvider paymentInfoProvider,
            PullRequestInfoProvider pullRequestInfoProvider,
            FeedbackInfoProvider feedbackInfoProvider,
            DefaultInfoProvider defaultInfoProvider) {
        this.defaultInfoProvider = defaultInfoProvider;

        providerMap.put(ProcessStatus.PAYMENT_CONFIRMATION, paymentInfoProvider);
        providerMap.put(ProcessStatus.CODE_REVIEW, pullRequestInfoProvider);
        providerMap.put(ProcessStatus.FEEDBACK_REVIEWED, feedbackInfoProvider);
    }

    public AdditionalInfoProvider getProvider(ProcessStatus status) {
        return providerMap.getOrDefault(status, defaultInfoProvider);
    }

}
