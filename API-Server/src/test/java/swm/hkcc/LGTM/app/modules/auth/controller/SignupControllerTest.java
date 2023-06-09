package swm.hkcc.LGTM.app.modules.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.JuniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpResponse;
import swm.hkcc.LGTM.app.modules.auth.service.AuthService;
import swm.hkcc.LGTM.app.modules.auth.utils.GithubUserInfoProvider;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SignupControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private GithubUserInfoProvider githubUserInfoProvider;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("주니어 회원가입 테스트")
    void juniorSignup() throws Exception {
        // given

        JuniorSignUpRequest juniorSignUpRequest = JuniorSignUpRequest.builder()
                .githubId("testGithubId")
                .githubOauthId(12345)
                .nickName("Test NickName")
                .deviceToken("Test DeviceToken")
                .profileImageUrl("Test ProfileImageUrl")
                .introduction("Test Introduction")
                .tagList(Arrays.asList("tag1", "tag2"))
                .educationalHistory("Test EducationalHistory")
                .realName("Test RealName")
                .build();

        SignUpResponse expectedResponse = SignUpResponse.builder()
                .memberId(1L)
                .githubId("testGithubId")
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();

        // when
        Mockito.when(authService.signupJunior(juniorSignUpRequest)).thenReturn(expectedResponse);

        // then
        mockMvc.perform(post("/v1/signup/junior")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(juniorSignUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.memberId").value(1L))
                .andExpect(jsonPath("$.data.githubId").value("testGithubId"))
                .andExpect(jsonPath("$.data.accessToken").value("testAccessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("testRefreshToken"));
    }


    @Test
    @DisplayName("시니어 회원가입 테스트")
    void seniorSignup() throws Exception {
        // given

        SeniorSignUpRequest seniorSignUpRequest = SeniorSignUpRequest.builder()
                .githubId("testGithubId")
                .githubOauthId(12345)
                .nickName("Test NickName")
                .deviceToken("Test DeviceToken")
                .profileImageUrl("Test ProfileImageUrl")
                .introduction("Test Introduction")
                .tagList(Arrays.asList("tag1", "tag2"))
                .companyInfo("Test CompanyInfo")
                .careerPeriod(5)
                .position("Test Position")
                .accountNumber("Test AccountNumber")
                .bankName("국민은행")
                .build();

        SignUpResponse expectedResponse = SignUpResponse.builder()
                .memberId(1L)
                .githubId("testGithubId")
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .build();

        // when
        Mockito.when(authService.signupSenior(seniorSignUpRequest)).thenReturn(expectedResponse);

        // then
        mockMvc.perform(post("/v1/signup/senior")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(seniorSignUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data.memberId").value(1L))
                .andExpect(jsonPath("$.data.githubId").value("testGithubId"))
                .andExpect(jsonPath("$.data.accessToken").value("testAccessToken"))
                .andExpect(jsonPath("$.data.refreshToken").value("testRefreshToken"));
    }

    @Test
    @DisplayName("닉네임 중복 검사")
    void checkNickname_NonDuplicate() throws Exception {
        // given
        String nonDuplicateNickname = "nonDuplicateNickname";

        // when
        Mockito.when(authService.checkDuplicateNickname(nonDuplicateNickname)).thenReturn(false);

        // then
        mockMvc.perform(get("/v1/signup/check-nickname")
                        .param("nickname", nonDuplicateNickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").value(false));
    }


}