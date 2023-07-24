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
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.JuniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpResponse;
import swm.hkcc.LGTM.app.modules.auth.exception.DuplicateNickName;
import swm.hkcc.LGTM.app.modules.auth.exception.InvalidTechTag;
import swm.hkcc.LGTM.app.modules.auth.service.AuthService;
import swm.hkcc.LGTM.app.modules.auth.utils.GithubUserInfoProvider;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.util.Arrays;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableHead;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableRow;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class SignupJuniorTest {

    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private GithubUserInfoProvider githubUserInfoProvider;

    // given request boy
    JuniorSignUpRequest juniorSignUpRequest;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        juniorSignUpRequest = JuniorSignUpRequest.builder()
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
    }

    @Test
    @DisplayName("주니어 회원가입 테스트")
    void juniorSignup() throws Exception {
        // given

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
                                .summary("[회원인증] 주니어 회원가입")
                                .description(
                                        CustomMDGenerator.builder()
                                                .h1("[Descriptions]")
                                                .h3("주니어 회원가입 정보 입력 후, 회원가입 정보를 반환한다.")
                                                .h3("View : 회원가입 화면")
                                                .h1("[Request values]")
                                                .table(
                                                        tableHead("Request values", "Data Type", "Description"),
                                                        tableRow("githubId", "String", "Github 아이디"),
                                                        tableRow("githubOauthId", "Integer", "Github의 사용자 식별 번호. 해당 id 이용하여 LGTM의 서비스 이용자를 식별한다."),
                                                        tableRow("nickName", "String", "닉네임, 1자 이상 10자 이하, 클라이언트에서 trim()처리하여 보낸다, 동일한 닉네임이 있을 경우 400 에러 반환"),
                                                        tableRow("deviceToken", "String", "디바이스 토큰"),
                                                        tableRow("profileImageUrl", "String", "프로필 이미지 URL"),
                                                        tableRow("introduction", "String", "나의 한줄 소개, 최대 500자, 클라이언트에서 trim()처리하여 보낸다"),
                                                        tableRow("tagList", "List<String>", "태그 리스트, 텍스트의 리스트로 전달한다. 1개 이상이어야 한다. 선택가능한 태그 외의 문자열이 전달될 경우 400에러 반환"),
                                                        tableRow("educationalHistory", "String", "학력"),
                                                        tableRow("realName", "String", "실명")
                                                )
                                                .line()
                                                .h1("[Errors]")
                                                .table(
                                                        tableHead("HTTP Status", "Response Code", "Message"),
                                                        tableRow(
                                                                ResponseCode.DUPLICATE_NICK_NAME.getHttpStatus().toString(),
                                                                ResponseCode.DUPLICATE_NICK_NAME.getCode().toString(),
                                                                ResponseCode.DUPLICATE_NICK_NAME.getMessage()),
                                                        tableRow(
                                                                ResponseCode.INVALID_TECH_TAG.getHttpStatus().toString(),
                                                                ResponseCode.INVALID_TECH_TAG.getCode().toString(),
                                                                ResponseCode.INVALID_TECH_TAG.getMessage())
                                                )
                                                .build()
                                )
                                .requestFields(
                                        fieldWithPath("githubId").type(JsonFieldType.STRING).description("Github 아이디"),
                                        fieldWithPath("githubOauthId").type(JsonFieldType.NUMBER).description("Github Oauth ID"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("deviceToken").type(JsonFieldType.STRING).description("디바이스 토큰"),
                                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                                        fieldWithPath("introduction").type(JsonFieldType.STRING).description("나의 한줄 소개"),
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
    @DisplayName("주니어 회원가입 실패 테스트 - 닉네임 중복")
    void juniorSignupDuplicatedNickname() throws Exception {
        // given

        // when
        Mockito.when(authService.signupJunior(juniorSignUpRequest)).thenThrow(new DuplicateNickName());

        // then
        ResponseCode expectedResponseCode = ResponseCode.DUPLICATE_NICK_NAME;
        ResultActions perform = mockMvc.perform(post("/v1/signup/junior")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(juniorSignUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        perform
                .andDo(document("post-signup-junior-duplicated-nickname",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                                )
                                .build())
                ));
    }

    @Test
    @DisplayName("주니어 회원가입 실패 테스트 - 부적절한 태그")
    void juniorSignupInvalidTag() throws Exception {
        // given

        // when
        Mockito.when(authService.signupJunior(juniorSignUpRequest)).thenThrow(new InvalidTechTag());

        // then
        ResponseCode expectedResponseCode = ResponseCode.INVALID_TECH_TAG;
        ResultActions perform = mockMvc.perform(post("/v1/signup/junior")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(juniorSignUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        perform
                .andDo(document("post-signup-junior-Invalid-tag",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                                )
                                .build())
                ));
    }

}
