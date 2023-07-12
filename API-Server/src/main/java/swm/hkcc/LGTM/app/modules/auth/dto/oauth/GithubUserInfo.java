package swm.hkcc.LGTM.app.modules.auth.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GithubUserInfo {

    private String login;
    private int id;
    private String node_id;
    private String avatar_url;
    private String gravatar_id;
    private String url;
    private String html_url;
    private String followers_url;
    private String following_url;
    private String gists_url;
    private String starred_url;
    private String subscriptions_url;
    private String organizations_url;
    private String repos_url;
    private String events_url;
    private String received_events_url;
    private String type;
    private boolean site_admin;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private Object hireable; // Could be null
    private Object bio; // Could be null
    private Object twitter_username; // Could be null
    private int public_repos;
    private int public_gists;
    private int followers;
    private int following;
    private String created_at;
    private String updated_at;

    @JsonProperty("plan")
    private Plan plan;

    @Getter
    @Setter
    @ToString
    public static class Plan {
        private String name;
        private long space;
        private int collaborators;
        private int private_repos;
    }
}