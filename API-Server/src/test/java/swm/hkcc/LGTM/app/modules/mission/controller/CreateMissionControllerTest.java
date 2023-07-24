package swm.hkcc.LGTM.app.modules.mission.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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
import swm.hkcc.LGTM.app.modules.member.domain.Authority;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionStatus;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionRequest;
import swm.hkcc.LGTM.app.modules.mission.dto.CreateMissionResponse;
import swm.hkcc.LGTM.app.modules.mission.service.CreateMissionServiceImpl;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class CreateMissionControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private CreateMissionServiceImpl createMissionService;

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
                .missionStatus(MissionStatus.RECRUITING.getValue())
                .tagList(List.of("JAVA", "Spring"))
                .thumbnailImageUrl("https://abc.com/aa.png")
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
    void createMission() throws Exception {
        // given
        //// mock user authentication
        Member member = (Member.builder()
                .memberId(1L)
                .build());
        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        SecurityContextHolder
                .getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                customUserDetails, "", customUserDetails.getAuthorities()
                        ));

        //// mock service
        String token = "token";
        Mockito.when(createMissionService.createMission(1L, createMissionRequest))
                .thenReturn(CreateMissionResponse.builder()
                        .missionId(1L)
                        .writerId(1L)
                        .build());

        // when

        // then
        ResultActions actions = mockMvc.perform(post("/v1/mission")
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
                                                .h1("미션 생성")
                                                .build()
                                )
                                .tag("미션")
                                .requestFields(
                                        // {"missionRepositoryUrl":"https://github.com/abcabc","title":"title","missionStatus":"참가자 모집중","tagList":["tag1","tag2"],"thumbnailImageUrl":"https://abc.com/aa.png","description":"content","reomnnandTo":"ReomnnandTo","notReomnnandTo":"notReomnnandTo","registrationDueDate":[2100,1,1,1,1,1],"assignmentDueDate":[2100,1,1,1,1,1],"reviewCompletationDueDate":[2100,1,1,1,1,1],"price":1000,"maxPeopleNumber":10}
                                        fieldWithPath("missionRepositoryUrl").type(JsonFieldType.STRING).description("미션 저장소 URL"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("미션 제목"),
                                        fieldWithPath("missionStatus").type(JsonFieldType.STRING).description("미션 상태"),
                                        fieldWithPath("tagList").type(JsonFieldType.ARRAY).description("미션 태그 리스트"),
                                        fieldWithPath("thumbnailImageUrl").type(JsonFieldType.STRING).description("미션 썸네일 이미지 URL"),
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

}
