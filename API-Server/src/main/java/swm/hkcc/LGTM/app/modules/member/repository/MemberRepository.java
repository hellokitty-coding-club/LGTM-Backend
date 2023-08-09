package swm.hkcc.LGTM.app.modules.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import swm.hkcc.LGTM.app.modules.member.domain.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findOneByGithubId(String githubId);

    Optional<Member> findByGithubOauthId(Integer githubOauthId);

    boolean existsByNickName(String nickName);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.deviceToken = NULL WHERE m.deviceToken = :token")
    void eraseDeviceToken(@Param("token") String token);
}
