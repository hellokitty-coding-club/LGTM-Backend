package swm.hkcc.LGTM.app.modules.tag.repository;


import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.Optional;

@Repository
public interface TechTagRepository extends JpaRepository<TechTag, Long> {
    @Cacheable(value = "tech_tag", key = "#p0")
    Optional<TechTag> findByName(String name);

    @Cacheable(value = "tech_tag_exist", key = "#p0")
    boolean existsByName(String name);
}
