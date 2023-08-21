package swm.hkcc.LGTM.app.modules.mission.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import swm.hkcc.LGTM.app.modules.member.domain.Authority;
import swm.hkcc.LGTM.app.modules.member.domain.Bank;
import swm.hkcc.LGTM.app.modules.member.domain.Member;
import swm.hkcc.LGTM.app.modules.member.domain.Senior;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.repository.SeniorRepository;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionStatus;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionScrapRepository;
import swm.hkcc.LGTM.app.modules.mission.service.CreateMissionServiceImpl;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMissionRepository;
import swm.hkcc.LGTM.utils.CustomMDGenerator;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
public class GetMissionTest {
    private MockMvc mockMvc;

    @MockBean
    private MissionRepository missionRepository;

    @MockBean
    private SeniorRepository seniorRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MissionScrapRepository missionScrapRepository;

    @MockBean
    private TechTagPerMissionRepository techTagPerMissionRepository;

    @MockBean
    private MissionRegistrationRepository missionRegistrationRepository;

    @MockBean
    private MemberService memberService;

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
    @DisplayName("미션 상세 조회 테스트")
    void createMission() throws Exception {
        // given
        Mission mockMission = createMockMission();
        Senior mockSenior = createMockSenior();
        Member mockMember = createMockMember();
        List<TechTag> mockTechTags = createMockTechTags();

        given(missionRepository.findById(anyLong())).willReturn(Optional.of(mockMission));
        given(seniorRepository.findByMember_MemberId(anyLong())).willReturn(Optional.of(mockSenior));
        given(missionScrapRepository.existsByScrapper_MemberIdAndMission_MissionId(anyLong(), anyLong())).willReturn(true);
        given(techTagPerMissionRepository.findTechTagsByMissionId(anyLong())).willReturn(mockTechTags);
        given(missionRegistrationRepository.countByMission_MissionId(anyLong())).willReturn(5);
        given(memberService.getMemberType(anyLong())).willReturn("JUNIOR");
        given(memberRepository.findOneByGithubId(anyString())).willReturn(Optional.ofNullable(mockMember));

        // when

        // then
        ResultActions actions = mockMvc.perform(get("/v1/mission/detail")
                        .header(
                                "Authorization",
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJnaXRodWJJZCI6InRlc3QtdG9rZW4tc2VuaW9yIiwiaWF0IjoxNjkwNTAyNzI0LCJleHAiOjE3ODUxMTA3MjR9.gKBXkTs-71pdu6wGE3_aP5oSXaAeO8tkN-tYi_mB0es"
                        )
                        .queryParam("missionId", "27")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.missionId").value(27))
                .andExpect(jsonPath("$.data.missionStatus").value("RECRUITING"))
                .andExpect(jsonPath("$.data.missionTitle").value("당근마켓 리드가 직접 알려주는 당근마켓 인프라 찍먹하기"))
                .andExpect(jsonPath("$.data.techTagList", hasSize(3)))
                .andExpect(jsonPath("$.data.techTagList[0].techTagId").value(7))
                .andExpect(jsonPath("$.data.techTagList[0].name").value("Android"))
                .andExpect(jsonPath("$.data.techTagList[1].techTagId").value(12))
                .andExpect(jsonPath("$.data.techTagList[1].name").value("Kotlin"))
                .andExpect(jsonPath("$.data.techTagList[2].techTagId").value(13))
                .andExpect(jsonPath("$.data.techTagList[2].name").value("iOS"))
                .andExpect(jsonPath("$.data.missionRepositoryUrl").value("https://www.github.com/kxxhyorim"))
                .andExpect(jsonPath("$.data.registrationDueDate").value("2100-01-01"))
                .andExpect(jsonPath("$.data.maxPeopleNumber").value(10))
                .andExpect(jsonPath("$.data.currentPeopleNumber").value(5))
                .andExpect(jsonPath("$.data.price").value(10000))
                .andExpect(jsonPath("$.data.description").value("content"))
                .andExpect(jsonPath("$.data.recommendTo").value("ReomnnandTo"))
                .andExpect(jsonPath("$.data.notRecommendTo").value("notReomnnandTo"))
                .andExpect(jsonPath("$.data.memberType").value("JUNIOR"))
                .andExpect(jsonPath("$.data.memberProfile.memberId").value(48))
                .andExpect(jsonPath("$.data.memberProfile.nickName").value("test-token-senior"))
                .andExpect(jsonPath("$.data.memberProfile.profileImageUrl").value("https://avatars.githubusercontent.com/u/899645?v=4"))
                .andExpect(jsonPath("$.data.memberProfile.githubId").value("test-token-senior"))
                .andExpect(jsonPath("$.data.memberProfile.company").value("(주)TestCompany"))
                .andExpect(jsonPath("$.data.scraped").value(true));

        // document
        actions
                .andDo(document("get-mission-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .summary("[미션] 미션 상세 조회")
                                .description(
                                        CustomMDGenerator.builder()
                                                .h1("[Descriptions]")
                                                .h3("특정 미션의 상세 정보를 조회한다.")
                                                .h1("[Request values]")
                                                .table(
                                                        tableHead("Header", "Data Type", "Description"),
                                                        tableRow("Authorization", "String", "Bearer token for authentication")
                                                )
                                                .line()
                                                .h1("[Errors]")
                                                .table(
                                                        tableHead("HTTP Status", "Response Code", "Message"),
                                                        tableRow(
                                                                ResponseCode.NOT_EXIST_MISSION.getHttpStatus().toString(),
                                                                ResponseCode.NOT_EXIST_MISSION.getCode().toString(),
                                                                ResponseCode.NOT_EXIST_MISSION.getMessage()),
                                                        tableRow(
                                                                ResponseCode.NOT_EXIST_MEMBER.getHttpStatus().toString(),
                                                                ResponseCode.NOT_EXIST_MEMBER.getCode().toString(),
                                                                ResponseCode.NOT_EXIST_MEMBER.getMessage())
                                                )
                                                .build()
                                )
                                .tag("미션")
                                .requestHeaders(
                                        headerWithName("Authorization").description("access token")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.missionId").type(JsonFieldType.NUMBER).description("미션 ID"),
                                        fieldWithPath("data.missionStatus").type(JsonFieldType.STRING).description("미션 상태"),
                                        fieldWithPath("data.missionTitle").type(JsonFieldType.STRING).description("미션 제목"),
                                        fieldWithPath("data.techTagList").type(JsonFieldType.ARRAY).description("기술 태그 리스트"),
                                        fieldWithPath("data.techTagList[].techTagId").type(JsonFieldType.NUMBER).description("기술 태그 ID"),
                                        fieldWithPath("data.techTagList[].name").type(JsonFieldType.STRING).description("기술 태그 이름"),
                                        fieldWithPath("data.missionRepositoryUrl").type(JsonFieldType.STRING).description("미션 저장소 URL"),
                                        fieldWithPath("data.registrationDueDate").type(JsonFieldType.STRING).description("모집 마감일"),
                                        fieldWithPath("data.maxPeopleNumber").type(JsonFieldType.NUMBER).description("최대 참가 인원"),
                                        fieldWithPath("data.currentPeopleNumber").type(JsonFieldType.NUMBER).description("현재 참가 인원 수"),
                                        fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("미션 가격"),
                                        fieldWithPath("data.description").type(JsonFieldType.STRING).description("미션 설명"),
                                        fieldWithPath("data.recommendTo").type(JsonFieldType.STRING).description("이런 사람에게 추천"),
                                        fieldWithPath("data.notRecommendTo").type(JsonFieldType.STRING).description("이런 사람에게 추천하지 않음"),
                                        fieldWithPath("data.memberType").type(JsonFieldType.STRING).description("회원 유형 (시니어/주니어)"),
                                        fieldWithPath("data.memberProfile").type(JsonFieldType.OBJECT).description("회원 프로필 정보"),
                                        fieldWithPath("data.memberProfile.memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                        fieldWithPath("data.memberProfile.nickName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.memberProfile.profileImageUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지 URL"),
                                        fieldWithPath("data.memberProfile.githubId").type(JsonFieldType.STRING).description("회원의 Github ID"),
                                        fieldWithPath("data.memberProfile.company").type(JsonFieldType.STRING).description("회원의 회사명"),
                                        fieldWithPath("data.scraped").type(JsonFieldType.BOOLEAN).description("미션을 스크랩했는지 여부")
                                )
                                .build())));

    }

    private List<TechTag> createMockTechTags() {
        return List.of(
                TechTag.builder()
                        .techTagId(7L)
                        .name("Android")
                        .build(),
                TechTag.builder()
                        .techTagId(12L)
                        .name("Kotlin")
                        .build(),
                TechTag.builder()
                        .techTagId(13L)
                        .name("iOS")
                        .build()
        );
    }


    private Mission createMockMission() {
        Member mockWriter = createMockWriter();

        return Mission.builder()
                .missionId(27L)
                .writer(mockWriter)
                .missionRepositoryUrl("https://www.github.com/kxxhyorim")
                .title("당근마켓 리드가 직접 알려주는 당근마켓 인프라 찍먹하기")
                .missionStatus(MissionStatus.RECRUITING)
                .description("content")
                .recommendTo("ReomnnandTo")
                .notRecommendTo("notReomnnandTo")
                .registrationDueDate(LocalDate.of(2100, 1, 1))
                .price(10000)
                .maxPeopleNumber(10)
                .build();
    }

    private Member createMockWriter() {
        Senior mockSenior = createMockSenior();

        return Member.builder()
                .memberId(48L)
                .githubId("test-token-senior")
                .nickName("test-token-senior")
                .profileImageUrl("https://avatars.githubusercontent.com/u/899645?v=4")
                .introduction("Test Senior Developer at (주)TestCompany")
                .senior(mockSenior)
                .build();
    }

    private Senior createMockSenior() {
        return Senior.builder()
                .seniorId(123L)
                .companyInfo("(주)TestCompany")
                .careerPeriod(24)
                .position("Senior Developer")
                .accountNumber("1234-5678-910")
                .bank(Bank.K_BANK)
                .member(createMockMember())
                .build();
    }

    private Member createMockMember() {
        Member member = Member.builder()
                .memberId(48L)
                .githubId("test-token-senior")
                .nickName("test-token-senior")
                .profileImageUrl("https://avatars.githubusercontent.com/u/899645?v=4")
                .introduction("Test Junior Developer")
                .build();

        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        return member;
    }
}
