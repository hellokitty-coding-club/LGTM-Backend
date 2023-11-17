package swm.hkcc.LGTM.app.modules.mission.repository;

import org.springframework.cache.annotation.Cacheable;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionView;

import java.util.List;

public interface MissionViewCustomRepository {
    @Cacheable(value = "most_viewed_missions", key = "#p0")
    List<MissionView> findByOrderByViewCountDesc(int n);
}
