package swm.hkcc.LGTM.app.modules.userLog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.modules.member.domain.Authority;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;
import swm.hkcc.LGTM.app.modules.userLog.dto.UserLogRequest;
import swm.hkcc.LGTM.app.modules.userLog.producer.IntervalLogProducer;
import swm.hkcc.LGTM.app.modules.userLog.producer.StampLogProducer;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.util.Collections;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableHead;
import static swm.hkcc.LGTM.utils.CustomMDGenerator.tableRow;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class UserLogControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private StampLogProducer stampLogProducer;

    @MockBean
    private IntervalLogProducer intervalLogProducer;

    @MockBean
    private MemberRepository memberRepository;


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
    @DisplayName("timestamp 형식 로깅 동작 테스트")
    void timestampLoggingTest() throws Exception {
        // given
        Member member = (Member.builder()
                .memberId(1L)
                .build());
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

        Mockito.when(memberRepository.findOneByGithubId(Mockito.anyString()))
                .thenReturn(java.util.Optional.ofNullable(member));

        UserLogRequest userLogRequest = new UserLogRequest();
        userLogRequest.setEventLogType("missionClick");
        userLogRequest.setScreenName("home");
        userLogRequest.setTarget("ongoing");
        userLogRequest.setMissionId(123L);


        Mockito.when(stampLogProducer.sendMessage(Mockito.any()))
                .thenReturn("topic-name");
        Mockito.when(intervalLogProducer.sendMessage(Mockito.any()))
                .thenReturn("topic-name");

        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/log")
                        .header(
                                "Authorization",
                                // todo : 토큰 생성
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tanVuaW9yIiwiaWF0IjoxNjkwNTAyNzYzLCJleHAiOjE3ODUxMTA3NjN9.7mRkcMUMazWl5qK3wnS5f3fCRcu287FJRWYFa-d3EFk"
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                new ObjectMapper().writeValueAsString(userLogRequest)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").value("topic-name"));

        // document
        actions
                .andDo(document("user-log-timestamp",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .summary("사용자 활동 로깅")
                                        .description("timestamp 형식 로깅 동작 테스트") // todo : 설명 추가
                                        .tag("Log")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("access Token")
                                        )
                                        .requestFields(
                                                fieldWithPath("eventLogType").type(JsonFieldType.STRING).description("이벤트 로그 타입"),
                                                fieldWithPath("screenName").type(JsonFieldType.STRING).description("화면 이름"),
                                                fieldWithPath("target").type(JsonFieldType.STRING).description("진입 타겟"),
                                                fieldWithPath("stayIntervalMs").ignored(),
                                                fieldWithPath("missionId").type(JsonFieldType.NUMBER).description("미션 아이디")
                                        )
                                        .responseFields(
                                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                                fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data").type(JsonFieldType.STRING).description("응답 데이터")
                                        )
                                        .build()

                        )));
    }


    // interval 형식 로깅 동작 테스트
    @Test
    @DisplayName("interval 형식 로깅 동작 테스트")
    void timestampLoggingTest_interval() throws Exception {
        // given
        Member member = (Member.builder()
                .memberId(1L)
                .build());
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

        Mockito.when(memberRepository.findOneByGithubId(Mockito.anyString()))
                .thenReturn(java.util.Optional.ofNullable(member));

        UserLogRequest userLogRequest = new UserLogRequest();
        userLogRequest.setEventLogType("missionClick");
        userLogRequest.setScreenName("home");
        userLogRequest.setTarget("ongoing");
        userLogRequest.setStayIntervalMs(1000L);
        userLogRequest.setMissionId(123L);


        Mockito.when(stampLogProducer.sendMessage(Mockito.any()))
                .thenReturn("topic-name");
        Mockito.when(intervalLogProducer.sendMessage(Mockito.any()))
                .thenReturn("topic-name");

        // when
        // then
        ResultActions actions = mockMvc.perform(post("/v1/log")
                        .header(
                                "Authorization",
                                // todo : 토큰 생성
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tanVuaW9yIiwiaWF0IjoxNjkwNTAyNzYzLCJleHAiOjE3ODUxMTA3NjN9.7mRkcMUMazWl5qK3wnS5f3fCRcu287FJRWYFa-d3EFk"
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                new ObjectMapper().writeValueAsString(userLogRequest)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").value("topic-name"));

        // document
        actions
                .andDo(document("user-log-interval",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("Log")
                                        .requestHeaders(
                                                headerWithName("Authorization").description("access Token")
                                        )
                                        .requestFields(
                                                fieldWithPath("eventLogType").type(JsonFieldType.STRING).description("이벤트 로그 타입"),
                                                fieldWithPath("screenName").type(JsonFieldType.STRING).description("화면 이름"),
                                                fieldWithPath("target").type(JsonFieldType.STRING).description("진입 타겟"),
                                                fieldWithPath("stayIntervalMs").type(JsonFieldType.NUMBER).description("머무른 시간"),
                                                fieldWithPath("missionId").type(JsonFieldType.NUMBER).description("미션 아이디")
                                        )
                                        .responseFields(
                                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                                fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                                fieldWithPath("data").type(JsonFieldType.STRING).description("응답 데이터")
                                        )
                                        .build()
                        )));
    }
}