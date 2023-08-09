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
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SeniorSignUpRequest;
import swm.hkcc.LGTM.app.modules.auth.dto.signUp.SignUpResponse;
import swm.hkcc.LGTM.app.modules.auth.exception.DuplicateNickName;
import swm.hkcc.LGTM.app.modules.auth.exception.InvalidTechTag;
import swm.hkcc.LGTM.app.modules.auth.service.AuthService;
import swm.hkcc.LGTM.app.modules.auth.utils.GithubUserInfoProvider;
import swm.hkcc.LGTM.app.modules.member.exception.InvalidBankName;
import swm.hkcc.LGTM.app.modules.member.exception.InvalidCareerPeriod;
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
public class SignupSeniorTest {

    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private GithubUserInfoProvider githubUserInfoProvider;

    // given request boy
    SeniorSignUpRequest seniorSignUpRequest;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        seniorSignUpRequest = SeniorSignUpRequest.builder()
                .githubId("testGithubId")
                .githubOauthId(12345)
                .nickName("test-nickname")
                .deviceToken("Test_DeviceToken")
                .profileImageUrl("http://test.ProfileImageUrl.com/img.png")
                .introduction("Test Introduction")
                .isAgreeWithEventInfo(true)
                .tagList(Arrays.asList("JAVA", "Python", "JavaScript"))
                .companyInfo("(주)TestCompany")
                .careerPeriod(36)
                .position("서버 개발자")
                .accountNumber("111-11-1111111")
                .bankName("국민은행")
                .build();
    }

    @Test
    @DisplayName("시니어 회원가입 테스트")
    void seniorSignup() throws Exception {
        // given
        SignUpResponse expectedResponse = SignUpResponse.builder()
                .memberId(1L)
                .githubId("testGithubId")
                .accessToken("testAccessToken")
                .refreshToken("testRefreshToken")
                .memberType("SENIOR")
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
                .andExpect(jsonPath("$.data.refreshToken").value("testRefreshToken"))
                .andExpect(jsonPath("$.data.memberType").value("SENIOR"));

        // document
        perform
                .andDo(document("post-signup-senior",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .tag("Authorization")
                                .summary("[회원인증] 시니어 회원가입")
                                .description(
                                        CustomMDGenerator.builder()
                                                .h1("[Descriptions]")
                                                .h3("시니어 회원가입 정보 입력 후, 회원가입 정보를 반환한다.")
                                                .h3("View : 회원가입 화면")
                                                .h1("[Request values]")
                                                .table(
                                                        tableHead("Request values", "Data Type", "Description"),
                                                        tableRow("githubId", "String", "Github 아이디"),
                                                        tableRow("githubOauthId", "Integer", "Github의 사용자 식별 번호. 해당 id 이용하여 LGTM의 서비스 이용자를 식별한다."),
                                                        tableRow("nickName", "String", "닉네임, 1자 이상 10자 이하, 클라이언트에서 trim()처리하여 보낸다, 동일한 닉네임이 있을 경우 400 에러 반환"),
                                                        tableRow("deviceToken", "String", "디바이스 토큰, \n device token이 추출이 안되는 기기일 경우 null값을 허용한다"),
                                                        tableRow("profileImageUrl", "String", "프로필 이미지 URL"),
                                                        tableRow("introduction", "String", "나의 한줄 소개, 최대 500자, 클라이언트에서 trim()처리하여 보낸다"),
                                                        tableRow("agreeWithEventInfo", "boolean", "이벤트, 광고성 정보 안내 수신 여부"),
                                                        tableRow("tagList", "List<String>", "태그 리스트, 텍스트의 리스트로 전달한다. 1개 이상이어야 한다. 선택가능한 태그 외의 문자열이 전달될 경우 400에러 반환"),
                                                        tableRow("companyInfo", "String", "회사 정보, 법인명에 해당하는 이름"),
                                                        tableRow("careerPeriod", "Integer", "경력 기간, 개월 단위로 입력, 1 이상의 정수, 12개월 이상이어야 한다."),
                                                        tableRow("position", "String", "직급, 1자 이상 10자 이하, 클라이언트에서 trim()처리하여 보낸다"),
                                                        tableRow("accountNumber", "String", "계좌번호, 숫자와 '-'로만 이루어져야 한다."),
                                                        tableRow("bankName", "String", "은행명, 등록되지 않은 이름일 경우 400 에러 반환")
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
                                                                ResponseCode.INVALID_TECH_TAG.getMessage()),
                                                        tableRow(
                                                                ResponseCode.INVALID_CAREER_PERIOD.getHttpStatus().toString(),
                                                                ResponseCode.INVALID_CAREER_PERIOD.getCode().toString(),
                                                                ResponseCode.INVALID_CAREER_PERIOD.getMessage()),
                                                        tableRow(
                                                                ResponseCode.INVALID_BANK_NAME.getHttpStatus().toString(),
                                                                ResponseCode.INVALID_BANK_NAME.getCode().toString(),
                                                                ResponseCode.INVALID_BANK_NAME.getMessage())
                                                )
                                                .build()
                                )
                                .requestFields(
                                        fieldWithPath("githubId").type(JsonFieldType.STRING).description("Github 아이디"),
                                        fieldWithPath("githubOauthId").type(JsonFieldType.NUMBER).description("Github Oauth ID"),
                                        fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                        fieldWithPath("deviceToken").type(JsonFieldType.STRING).optional().description("디바이스 토큰"),
                                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("프로필 이미지 URL"),
                                        fieldWithPath("agreeWithEventInfo").type(JsonFieldType.BOOLEAN).description("이벤트, 광고성 정보 안내 수신 여부"),
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
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
                                        fieldWithPath("data.memberType").type(JsonFieldType.STRING).description("회원 타입")
                                )
                                .build())
                ));
    }

    @Test
    @DisplayName("시니어 회원가입 실패 테스트 - 닉네임 중복")
    void seniorSignupDuplicatedNickname() throws Exception {
        // given

        // when
        Mockito.when(authService.signupSenior(seniorSignUpRequest)).thenThrow(new DuplicateNickName());

        // then
        ResponseCode expectedResponseCode = ResponseCode.DUPLICATE_NICK_NAME;
        ResultActions perform = mockMvc.perform(post("/v1/signup/senior")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(seniorSignUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        perform
                .andDo(document("post-signup-senior-duplicated-nickname",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                                )
                                .build())));
    }


    @Test
    @DisplayName("시니어 회원가입 실패 테스트 - 부적절한 태그")
    void seniorSignupInvalidTag() throws Exception {
        // given

        // when
        Mockito.when(authService.signupSenior(seniorSignUpRequest)).thenThrow(new InvalidTechTag());

        // then
        ResponseCode expectedResponseCode = ResponseCode.INVALID_TECH_TAG;
        ResultActions perform = mockMvc.perform(post("/v1/signup/senior")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(seniorSignUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        perform
                .andDo(document("post-signup-senior-invalid-tag",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                                )
                                .build())));
    }

    @Test
    @DisplayName("시니어 회원가입 실패 테스트 - 부적절한 경력사항")
    void seniorSignupInvalidCareer() throws Exception {
        // given

        // when
        Mockito.when(authService.signupSenior(seniorSignUpRequest)).thenThrow(new InvalidCareerPeriod());

        // then
        ResponseCode expectedResponseCode = ResponseCode.INVALID_CAREER_PERIOD;
        ResultActions perform = mockMvc.perform(post("/v1/signup/senior")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(seniorSignUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        perform
                .andDo(document("post-signup-senior-invalid-career",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                                )
                                .build())));
    }

    @Test
    @DisplayName("시니어 회원가입 실패 테스트 - 부적절한 은행명")
    void seniorSignupInvalidBankName() throws Exception {
        // given

        // when
        Mockito.when(authService.signupSenior(seniorSignUpRequest)).thenThrow(new InvalidBankName());

        // then
        ResponseCode expectedResponseCode = ResponseCode.INVALID_BANK_NAME;
        ResultActions perform = mockMvc.perform(post("/v1/signup/senior")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(seniorSignUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        perform
                .andDo(document("post-signup-senior-invalid-bank",      // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                                )
                                .build())));
    }

}
