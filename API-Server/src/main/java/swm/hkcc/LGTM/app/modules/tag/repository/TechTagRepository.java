package swm.hkcc.LGTM.app.modules.tag.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;

import java.util.Optional;

@Repository
public interface TechTagRepository extends JpaRepository<TechTag, Long> {
    Optional<TechTag> findByName(String name);

    boolean existsByName(String name);
}
