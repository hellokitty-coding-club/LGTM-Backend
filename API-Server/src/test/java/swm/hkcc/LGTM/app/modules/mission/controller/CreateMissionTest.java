package swm.hkcc.LGTM.app.modules.mission.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import swm.hkcc.LGTM.app.global.constant.ResponseCode;
import swm.hkcc.LGTM.app.modules.auth.exception.InvalidTechTag;
import swm.hkcc.LGTM.app.modules.member.domain.Authority;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.exception.NotExistMember;
import swm.hkcc.LGTM.app.modules.member.exception.NotSeniorMember;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.service.CreateMissionServiceImpl;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
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

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class CreateMissionTest {
    private MockMvc mockMvc;

    @MockBean
    private CreateMissionServiceImpl createMissionService;

    @MockBean
    private MemberRepository memberRepository;

    CreateMissionRequest createMissionRequest;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        LocalDate referenceDate = LocalDate.of(2100, 1, 1);
        createMissionRequest = CreateMissionRequest.builder()
                .missionRepositoryUrl("https://github.com/abcabc")
                .title("title")
                .tagList(List.of("JAVA", "Spring"))
                .description("content")
                .reomnnandTo("ReomnnandTo")
                .notReomnnandTo("notReomnnandTo")
                .registrationDueDate(referenceDate)
                .assignmentDueDate(referenceDate)
                .reviewCompletationDueDate(referenceDate)
                .price(1000)
                .maxPeopleNumber(10)
                .build();
    }

    @Test
    @DisplayName("미션 생성 동작 테스트")
    void createMission() throws Exception {
        // given
        Member member = (Member.builder()
                .memberId(1L)
                .build());
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

        Mockito.when(createMissionService.createMission(1L, createMissionRequest))
                .thenReturn(
                        Mission.builder()
                                .missionId(1L)
                                .writer(Member.builder().memberId(1L).build())
                                .build());
        Mockito.when(memberRepository.findOneByGithubId(Mockito.anyString()))
                .thenReturn(java.util.Optional.ofNullable(member));

        // when

        // then
        ResultActions actions = mockMvc.perform(post("/v1/mission")
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                new ObjectMapper()
                                        .registerModule(new JavaTimeModule())
                                        .writeValueAsString(createMissionRequest)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.missionId").value(1L))
                .andExpect(jsonPath("$.data.writerId").value(1L));

        // document
        actions
                .andDo(document("post-create-mission",  // 문서의 고유 id
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력
                        resource(ResourceSnippetParameters.builder()
                                .summary("[미션] 미션 생성")
                                .description(
                                        CustomMDGenerator.builder()
                                                .h1("[Descriptions]")
                                                .h3("시니어가 새로운 미션을 생성한다.")
                                                .h1("[Request values]")
                                                .table(
                                                        tableHead("Request values", "Data Type", "Description"),
                                                        tableRow("missionRepositoryUrl", "String", "미션 저장소 URL. URL 형식이 아니면 Validation 에러 발생"),
                                                        tableRow("title", "String", "미션 제목. 최대 길이 = 100"),
                                                        tableRow("tagList", "List<String>", "미션 태그 리스트"),
                                                        tableRow("description", "String", "미션 설명. 최대 길이 = 1000"),
                                                        tableRow("reomnnandTo", "String", "이런 사람에게 추천해요. 최대 길이 = 1000"),
                                                        tableRow("notReomnnandTo", "String", "이런 사람에게는 추천하지 않아요. 최대 길이 = 1000"),
                                                        tableRow("registrationDueDate", "LocalDate", "모집 및 입금완료 마감일. yyyy-MM-dd 형식, 미션 등록일보다 현재 혹은 미래가 아닐 경우 Validation 에러 발생"),
                                                        tableRow("assignmentDueDate", "LocalDate", "pr 제출 마감일. yyyy-MM-dd 형식, 미션 등록일보다 현재 혹은 미래가 아닐 경우 Validation 에러 발생"),
                                                        tableRow("reviewCompletationDueDate", "LocalDate", "미션 리뷰 완료 마감일. yyyy-MM-dd 형식, 미션 등록일보다 현재 혹은 미래가 아닐 경우 Validation 에러 발생"),
                                                        tableRow("price", "Integer", "미션 가격"),
                                                        tableRow("maxPeopleNumber", "Integer", "미션 최대 참가 인원")
                                                )
                                                .line()
                                                .h1("[Errors]")
                                                .table(
                                                        tableHead("HTTP Status", "Response Code", "Message"),
                                                        tableRow(
                                                                ResponseCode.NOT_EXIST_MEMBER.getHttpStatus().toString(),
                                                                ResponseCode.NOT_EXIST_MEMBER.getCode().toString(),
                                                                ResponseCode.NOT_EXIST_MEMBER.getMessage()),
                                                        tableRow(
                                                                ResponseCode.INVALID_TECH_TAG.getHttpStatus().toString(),
                                                                ResponseCode.INVALID_TECH_TAG.getCode().toString(),
                                                                ResponseCode.INVALID_TECH_TAG.getMessage()),
                                                        tableRow(
                                                                ResponseCode.NOT_SENIOR_MEMBER.getHttpStatus().toString(),
                                                                ResponseCode.NOT_SENIOR_MEMBER.getCode().toString(),
                                                                ResponseCode.NOT_SENIOR_MEMBER.getMessage())
                                                )
                                                .build()
                                )
                                .tag("미션")
                                .requestFields(
                                        // {"missionRepositoryUrl":"https://github.com/abcabc","title":"title","missionStatus":"참가자 모집중","tagList":["tag1","tag2"],"description":"content","reomnnandTo":"ReomnnandTo","notReomnnandTo":"notReomnnandTo","registrationDueDate":[2100,1,1,1,1,1],"assignmentDueDate":[2100,1,1,1,1,1],"reviewCompletationDueDate":[2100,1,1,1,1,1],"price":1000,"maxPeopleNumber":10}
                                        fieldWithPath("missionRepositoryUrl").type(JsonFieldType.STRING).description("미션 저장소 URL"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("미션 제목"),
                                        fieldWithPath("tagList").type(JsonFieldType.ARRAY).description("미션 태그 리스트"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("미션 설명"),
                                        fieldWithPath("reomnnandTo").type(JsonFieldType.STRING).description("미션 추천 대상"),
                                        fieldWithPath("notReomnnandTo").type(JsonFieldType.STRING).description("미션 비추천 대상"),
                                        // todo: 마감일 형식!!!
                                        fieldWithPath("registrationDueDate").type(JsonFieldType.STRING).description("미션 참가 신청 마감일"),
                                        fieldWithPath("assignmentDueDate").type(JsonFieldType.STRING).description("미션 과제 제출 마감일"),
                                        fieldWithPath("reviewCompletationDueDate").type(JsonFieldType.STRING).description("미션 리뷰 제출 마감일"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("미션 가격"),
                                        fieldWithPath("maxPeopleNumber").type(JsonFieldType.NUMBER).description("미션 최대 참가 인원")
                                )
                                .requestHeaders(
                                        headerWithName("Authorization").description("access token")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                                        fieldWithPath("data.missionId").type(JsonFieldType.NUMBER).description("미션 ID")
                                )
                                .build())));
    }


    @Test
    @DisplayName("미션 생성 실패 테스트 - 존재하지 않는 회원")
    void createMissionNotExistMember() throws Exception {
        // given
        Member member = (Member.builder()
                .memberId(1L)
                .build());
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        Mockito.when(createMissionService.createMission(1L, createMissionRequest))
                .thenThrow(new NotExistMember());

        Mockito.when(memberRepository.findOneByGithubId(Mockito.anyString()))
                .thenReturn(java.util.Optional.ofNullable(member));

        // when

        // then
        ResponseCode expectedResponseCode = ResponseCode.NOT_EXIST_MEMBER;
        ResultActions actions = mockMvc.perform(post("/v1/mission")
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                new ObjectMapper()
                                        .registerModule(new JavaTimeModule())
                                        .writeValueAsString(createMissionRequest)
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions
                .andDo(document("post-create-mission-not-exist-member",
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")

                                ).build())
                ));
    }


    @Test
    @DisplayName("미션 생성 실패 테스트 - 시니어가 아닌 회원")
    void createMissionNotSenior() throws Exception {
        // given
        Member member = (Member.builder()
                .memberId(1L)
                .build());
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        Mockito.when(createMissionService.createMission(1L, createMissionRequest))
                .thenThrow(new NotSeniorMember());

        Mockito.when(memberRepository.findOneByGithubId(Mockito.anyString()))
                .thenReturn(java.util.Optional.ofNullable(member));

        // when

        // then
        ResponseCode expectedResponseCode = ResponseCode.NOT_SENIOR_MEMBER;
        ResultActions actions = mockMvc.perform(post("/v1/mission")
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                new ObjectMapper()
                                        .registerModule(new JavaTimeModule())
                                        .writeValueAsString(createMissionRequest)
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions
                .andDo(document("post-create-mission-not-senior-member",
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")

                                ).build())
                ));
    }

    @Test
    @DisplayName("미션 생성 실패 테스트 - 부적절한 태그")
    void createMissionInvalidTechTag() throws Exception {
        // given
        Member member = (Member.builder()
                .memberId(1L)
                .build());
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        Mockito.when(createMissionService.createMission(1L, createMissionRequest))
                .thenThrow(new InvalidTechTag());

        Mockito.when(memberRepository.findOneByGithubId(Mockito.anyString()))
                .thenReturn(java.util.Optional.ofNullable(member));

        // when

        // then
        ResponseCode expectedResponseCode = ResponseCode.INVALID_TECH_TAG;
        ResultActions actions = mockMvc.perform(post("/v1/mission")
                        .header(
                                "Authorization",
                                // todo : mock member로부터 토큰 생성해서 넣기
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                new ObjectMapper()
                                        .registerModule(new JavaTimeModule())
                                        .writeValueAsString(createMissionRequest)
                        ))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseCode").value(expectedResponseCode.getCode()))
                .andExpect(jsonPath("$.message").value(expectedResponseCode.getMessage()));

        // document
        actions
                .andDo(document("post-create-mission-invalid-tech-tag",
                        preprocessRequest(prettyPrint()),        // request JSON 정렬하여 출력
                        preprocessResponse(prettyPrint()),       // response JSON 정렬하여 출력

                        resource(ResourceSnippetParameters.builder()
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")

                                ).build())
                ));

    }

}
