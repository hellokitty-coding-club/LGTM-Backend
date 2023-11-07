package swm.hkcc.LGTM.app.modules.userLog;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import swm.hkcc.LGTM.app.modules.auth.constants.TokenType;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.TokenProvider;
import swm.hkcc.LGTM.app.modules.member.domain.Authority;
import swm.hkcc.LGTM.app.modules.member.domain.Junior;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.registration.service.RegistrationService;
import swm.hkcc.LGTM.app.modules.userLog.dto.LogMessage;
import swm.hkcc.LGTM.app.modules.userLog.producer.LogProducer;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableHead;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableRow;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class UserLogControllerTest {
    private MockMvc mockMvc;


    @Autowired
    private TokenProvider tokenProvider;

    @MockBean
    private LogProducer logProducer;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private CustomUserDetails customUserDetails;

    private Member mockJunior;

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
    void userlog_동작_테스트() throws Exception {
        // given
        given(memberRepository.findOneByGithubId(Mockito.anyString())).willReturn(java.util.Optional.ofNullable(getMockJunior()));
        given(logProducer.sendMessage(any())).willReturn("test-topic");

        LogMessage logMessage = LogMessage.builder()
                .eventLogName("test-name")
                .screenName("test-screen")
                .logVersion("1")
                .sessionID("test-session")
                .userID("1")
                .osNameAndVersion("test-os")
                .deviceModel("test-model")
                .appVersion("test-version")
                .region("test-region")
                .logData(new ConcurrentHashMap<>())
                .build();
        System.out.println(new ObjectMapper().writeValueAsString(logMessage));

        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/log")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(logMessage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"));

        // document
        actions.andDo(document(
                "post-user-log",// 문서의 고유 id
                preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                resource(
                        ResourceSnippetParameters.builder()
                                .summary("[로깅] 사용자 로그 전송")
                                .tag("로깅")
                                .description(CustomMDGenerator.builder()
                                        .h1("[Descriptions]")
                                        .h3("사용자 로그를 전송한다.")
                                        .h1("[Request Headers]")
                                        .table(
                                                tableHead("Request values", "Data Type", "Description"),
                                                tableRow("Authorization", "String", "액세스 토큰")
                                        )
                                        .line()
                                        .h1("[Request Body]")
                                        .table(
                                                tableHead("Response values", "Data Type", "Description"),
                                                tableRow("eventLogName", "String", "이벤트 로그 이름"),
                                                tableRow("logVersion", "String", "로그 버전, 숫자를 문자로 변환하여 전송, name & version이 동일한 경우, logData의 값 구성은 동일해야 한다"),
                                                tableRow("screenName", "String", "화면 이름, 로그 전송 시점 사용자의 뷰 이름"),
                                                tableRow("", "", ""),
                                                tableRow("logData", "Map", "로그 데이터"),
                                                tableRow("", "", ""),
                                                tableRow("sessionID", "String", "세션 ID, 클라이언트측에서 UUID 생성"),
                                                tableRow("userID", "String", "사용자 ID 숫자를 문자로 변환하여 전송"),
                                                tableRow("osNameAndVersion", "String", "디바이스 OS"),
                                                tableRow("deviceModel", "String", "디바이스 모델"),
                                                tableRow("appVersion", "String", "앱 버전, ex) 1.0.0"),
                                                tableRow("region", "String", "지역, ex) KR")
                                        )
                                        .line()
                                        .h1("[Response Body]")
                                        .table(
                                                tableHead("Response values", "Data Type", "Description"),
                                                tableRow("success", "Boolean", "성공 여부"),
                                                tableRow("responseCode", "Integer", "응답 코드"),
                                                tableRow("message", "String", "응답 메시지"),
                                                tableRow("data", "String", "응답 데이터, topic")
                                        )
                                        .build()
                                )
                                .requestFields(
                                        fieldWithPath("eventLogName").description("이벤트 로그 이름"),
                                        fieldWithPath("screenName").description("화면 이름"),
                                        fieldWithPath("logVersion").description("로그 버전"),
                                        fieldWithPath("logData").description("로그 데이터"),
                                        fieldWithPath("sessionID").description("세션 ID"),
                                        fieldWithPath("userID").description("사용자 ID"),
                                        fieldWithPath("osNameAndVersion").description("디바이스 OS"),
                                        fieldWithPath("deviceModel").description("디바이스 모델"),
                                        fieldWithPath("appVersion").description("앱 버전"),
                                        fieldWithPath("region").description("지역")
                                )
                                .responseFields(
                                        fieldWithPath("success").description("성공 여부"),
                                        fieldWithPath("responseCode").description("응답 코드"),
                                        fieldWithPath("message").description("응답 메시지"),
                                        fieldWithPath("data").description("응답 데이터")
                                )
                                .build()
                )));
    }


    private Member getMockJunior() {
        if (mockJunior == null) {
            mockJunior = Member.builder()
                    .memberId(1L)
                    .githubId("junior")
                    .nickName("junior")
                    .junior(Junior.builder().build())
                    .build();
            mockJunior.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        }
        return mockJunior;
    }


    private String getMockToken(Member member) {
        return tokenProvider.createToken(member.getGithubId(), TokenType.ACCESS_TOKEN);
    }
}