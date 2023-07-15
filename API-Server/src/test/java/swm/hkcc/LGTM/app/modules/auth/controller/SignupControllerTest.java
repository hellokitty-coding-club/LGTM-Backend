package swm.hkcc.LGTM.app.modules.auth.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.JuniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpResponse;
import swm.hkcc.LGTM.app.modules.auth.service.AuthService;
import swm.hkcc.LGTM.app.modules.auth.utils.GithubUserInfoProvider;

import java.util.Arrays;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class SignupControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private GithubUserInfoProvider githubUserInfoProvider;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
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
        ResultActions perform = mockMvc.perform(post("/v1/signup/junior")
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

        // document
        perform
                .andDo(document("post-signup-junior",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .summary("주니어 회원가입")
                                .description("주니어 회원가입 정보 입력 후, 회원가입 정보를 반환한다.")
                                .requestFields(
                                        fieldWithPath("githubId").type(JsonFieldType.STRING).description("Github 아이디"),
                                        fieldWithPath("githubOauthId").type(JsonFieldType.NUMBER).description("Github Oauth ID"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("deviceToken").type(JsonFieldType.STRING).description("디바이스 토큰"),
                                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기소개"),
                                        fieldWithPath("tagList").type(JsonFieldType.ARRAY).description("태그 리스트"),
                                        fieldWithPath("educationalHistory").type(JsonFieldType.STRING).description("학력"),
                                        fieldWithPath("realName").type(JsonFieldType.STRING).description("실명")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 아이디"),
                                        fieldWithPath("data.githubId").type(JsonFieldType.STRING).description("Github 아이디"),
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                )
                                .build())
                ));
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
        ResultActions perform = mockMvc.perform(post("/v1/signup/senior")
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

        // document
        perform
                .andDo(document("post-signup-senior",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .summary("시니어 회원가입")
                                .description("시니어 회원가입 정보 입력 후, 회원가입 정보를 반환한다.")
                                .requestFields(
                                        fieldWithPath("githubId").type(JsonFieldType.STRING).description("Github 아이디"),
                                        fieldWithPath("githubOauthId").type(JsonFieldType.NUMBER).description("Github Oauth ID"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("deviceToken").type(JsonFieldType.STRING).description("디바이스 토큰"),
                                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기소개"),
                                        fieldWithPath("tagList").type(JsonFieldType.ARRAY).description("태그 리스트"),
                                        fieldWithPath("companyInfo").type(JsonFieldType.STRING).description("회사 정보"),
                                        fieldWithPath("careerPeriod").type(JsonFieldType.NUMBER).description("경력 기간"),
                                        fieldWithPath("position").type(JsonFieldType.STRING).description("직급"),
                                        fieldWithPath("accountNumber").type(JsonFieldType.STRING).description("계좌 번호"),
                                        fieldWithPath("bankName").type(JsonFieldType.STRING).description("은행 이름")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 아이디"),
                                        fieldWithPath("data.githubId").type(JsonFieldType.STRING).description("Github 아이디"),
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                )
                                .build())
                ));
    }

    @Test
    @DisplayName("닉네임 중복 검사")
    void checkNickname_NonDuplicate() throws Exception {
        // given
        String nonDuplicateNickname = "nonDuplicateNickname";

        // when
        Mockito.when(authService.checkDuplicateNickname(nonDuplicateNickname)).thenReturn(false);

        // then
        ResultActions perform = mockMvc.perform(get("/v1/signup/check-nickname")
                        .param("nickname", nonDuplicateNickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").value(false));

        // document
        perform
                .andDo(document("get-check-nickname",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .summary("닉네임 중복 검사")
                                .description("닉네임 중복 검사 후, 중복 여부를 반환한다.")
                                .queryParameters(
                                        parameterWithName("nickname").description("닉네임")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("닉네임 중복 여부")
                                )
                                .build())
                ));
    }


    @Test
    @DisplayName("닉네임 중복 검사")
    void checkNickname_NonDuplicate() throws Exception {
        // given
        String nonDuplicateNickname = "nonDuplicateNickname";

        // when
        Mockito.when(authService.checkDuplicateNickname(nonDuplicateNickname)).thenReturn(false);

        // then
        ResultActions perform = mockMvc.perform(get("/v1/signup/check-nickname")
                        .param("nickname", nonDuplicateNickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").value(false));

        // document
        perform
                .andDo(document("get-check-nickname",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .summary("닉네임 중복 검사")
                                .description("닉네임 중복 검사 후, 중복 여부를 반환한다.")
                                .queryParameters(
                                        parameterWithName("nickname").description("닉네임")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("닉네임 중복 여부")
                                )
                                .build())
                ));
    }

}