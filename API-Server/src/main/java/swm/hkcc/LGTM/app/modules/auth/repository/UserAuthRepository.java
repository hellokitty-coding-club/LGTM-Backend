package swm.hkcc.LGTM.app.modules.auth.repository;

import org.springframework.stereotype.Repository;
import swm.hkcc.LGTM.app.modules.auth.entity.User;

import java.util.Optional;

public interface UserAuthRepository {

    public Optional<User> findUserByGithubId(String githubId);

    public User createUser(User user);

}
