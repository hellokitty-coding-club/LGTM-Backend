package swm.hkcc.LGTM.app.modules.registration.service.additionalInfoProvider;

import org.springframework.stereotype.Service;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.review.repository.ReviewRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdditionalInfoProviderFactory {
    private final Map<ProcessStatus, AdditionalInfoProvider> providerMap = new ConcurrentHashMap<>();

    public AdditionalInfoProviderFactory(MissionRegistrationRepository missionRegistrationRepository, ReviewRepository reviewRepository) {

        providerMap.put(ProcessStatus.PAYMENT_CONFIRMATION, new PaymentInfoProvider());
        providerMap.put(ProcessStatus.CODE_REVIEW, new PullRequestInfoProvider(missionRegistrationRepository));
        providerMap.put(ProcessStatus.FEEDBACK_REVIEWED, new FeedbackInfoProvider(reviewRepository));
    }

    public AdditionalInfoProvider getProvider(ProcessStatus status) {
        return providerMap.getOrDefault(status, new DefaultInfoProvider());
    }

}
