package swm.hkcc.LGTM.app.modules.member.controller;

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
import swm.hkcc.LGTM.app.modules.auth.constants.TokenType;
import swm.hkcc.LGTM.app.modules.auth.utils.jwt.TokenProvider;
import swm.hkcc.LGTM.app.modules.member.domain.*;
import swm.hkcc.LGTM.app.modules.member.domain.custom.CustomUserDetails;
import swm.hkcc.LGTM.app.modules.member.repository.JuniorRepository;
import swm.hkcc.LGTM.app.modules.member.repository.MemberRepository;
import swm.hkcc.LGTM.app.modules.member.repository.SeniorRepository;
import swm.hkcc.LGTM.app.modules.member.service.MemberService;
import swm.hkcc.LGTM.app.modules.mission.domain.Mission;
import swm.hkcc.LGTM.app.modules.mission.domain.MissionStatus;
import swm.hkcc.LGTM.app.modules.mission.repository.MissionRepository;
import swm.hkcc.LGTM.app.modules.registration.domain.MissionRegistration;
import swm.hkcc.LGTM.app.modules.registration.domain.ProcessStatus;
import swm.hkcc.LGTM.app.modules.registration.repository.MissionRegistrationRepository;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTag;
import swm.hkcc.LGTM.app.modules.tag.domain.TechTagPerMember;
import swm.hkcc.LGTM.app.modules.tag.repository.TechTagPerMemberRepository;
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
class GetMemberProfileTest {

    private MockMvc mockMvc;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private TechTagPerMemberRepository techTagPerMemberRepository;

    @MockBean
    private MissionRepository missionRepository;

    @MockBean
    private MissionRegistrationRepository missionRegistrationRepository;

    @MockBean
    private TechTagPerMissionRepository techTagPerMissionRepository;

    @MockBean
    private SeniorRepository seniorRepository;

    @MockBean
    private JuniorRepository juniorRepository;

    @BeforeEach
    public void setUp(@Autowired WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();

        // mock user authentication
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
    }

    @Test
    @DisplayName("시니어 회원 프로필 조회")
    void getSeniorProfileTest() throws Exception {
        // given
        Senior mockSenior = createMockSenior();
        Member mockMember = createMockSeniorMember(mockSenior);
        String memberAccessToken = getMockToken(mockMember);
        List<TechTag> mockTechTags = createMockTechTags();
        Mission mockMission = createMockMission(mockMember);
        List<TechTagPerMember> mockMemberTechTags = createMockMemberTechTags(mockMember);

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(mockMember));
        given(techTagPerMemberRepository.findWithTechTagByMemberId(anyLong())).willReturn(mockMemberTechTags);
        given(missionRepository.findAllByWriter_MemberId(anyLong())).willReturn(List.of(mockMission));
        given(seniorRepository.findByMember_MemberId(anyLong())).willReturn(Optional.of(mockSenior));
        given(techTagPerMissionRepository.findTechTagsByMissionId(anyLong())).willReturn(mockTechTags);
        given(memberRepository.findOneByGithubId(anyString())).willReturn(Optional.of(mockMember));

        // when

        // then
        ResultActions actions = mockMvc.perform(get("/v1/member/profile")
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.memberId").value(48))
                .andExpect(jsonPath("$.data.memberType").value("SENIOR"))
                .andExpect(jsonPath("$.data.githubId").value("test-token-senior"))
                .andExpect(jsonPath("$.data.nickName").value("test-token-senior"))
                .andExpect(jsonPath("$.data.profileImageUrl").value("https://avatars.githubusercontent.com/u/899645?v=4"))
                .andExpect(jsonPath("$.data.introduction").value("Test Senior Developer"))
                .andExpect(jsonPath("$.data.techTagList", hasSize(1)))
                .andExpect(jsonPath("$.data.techTagList[0].techTagId").value(1))
                .andExpect(jsonPath("$.data.techTagList[0].name").value("Java"))
                .andExpect(jsonPath("$.data.memberDetailInfo.companyInfo").value("(주)TestCompany"))
                .andExpect(jsonPath("$.data.memberDetailInfo.careerPeriod").value("24"))
                .andExpect(jsonPath("$.data.memberDetailInfo.position").value("안드로이드 엔지니어"))
                .andExpect(jsonPath("$.data.memberMissionHistory", hasSize(1)))
                .andExpect(jsonPath("$.data.memberMissionHistory.[0].missionId").value(27))
                .andExpect(jsonPath("$.data.memberMissionHistory.[0].missionTitle").value("당근마켓 리드가 직접 알려주는 당근마켓 인프라 찍먹하기"))
                .andExpect(jsonPath("$.data.memberMissionHistory.[0].techTagList.[0].techTagId").value(7))
                .andExpect(jsonPath("$.data.memberMissionHistory.[0].techTagList.[0].name").value("Android"))
                .andExpect(jsonPath("$.data.agreeWithEventInfo").value(true));

        // document
        actions
                .andDo(document("get-senior-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .summary("[멤버] 시니어 프로필 조회")
                                .description(
                                        CustomMDGenerator.builder()
                                                .h1("[Descriptions]")
                                                .h3("시니어 프로필을 조회한다.")
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
                                .tag("Member")
                                .requestHeaders(
                                        headerWithName("Authorization").description("access token")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                        fieldWithPath("data.memberType").type(JsonFieldType.STRING).description("회원 타입 - SENIOR, JUNIOR"),
                                        fieldWithPath("data.githubId").type(JsonFieldType.STRING).description("회원의 Github ID"),
                                        fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지 URL"),
                                        fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("자기 소개"),
                                        fieldWithPath("data.techTagList").type(JsonFieldType.ARRAY).description("기술 태그 리스트"),
                                        fieldWithPath("data.techTagList[].techTagId").type(JsonFieldType.NUMBER).description("기술 태그 ID"),
                                        fieldWithPath("data.techTagList[].name").type(JsonFieldType.STRING).description("기술 태그 이름"),
                                        fieldWithPath("data.memberDetailInfo.companyInfo").type(JsonFieldType.STRING).description("회원의 회사 정보"),
                                        fieldWithPath("data.memberDetailInfo.careerPeriod").type(JsonFieldType.NUMBER).description("회원의 경력 기간"),
                                        fieldWithPath("data.memberDetailInfo.position").type(JsonFieldType.STRING).description("회원의 직책"),
                                        fieldWithPath("data.memberMissionHistory").type(JsonFieldType.ARRAY).description("회원의 미션 히스토리"),
                                        fieldWithPath("data.memberMissionHistory[].missionId").type(JsonFieldType.NUMBER).description("미션 ID"),
                                        fieldWithPath("data.memberMissionHistory[].missionTitle").type(JsonFieldType.STRING).description("미션 제목"),
                                        fieldWithPath("data.memberMissionHistory[].techTagList").type(JsonFieldType.ARRAY).description("미션에 사용된 기술 태그 리스트"),
                                        fieldWithPath("data.memberMissionHistory[].techTagList[].techTagId").type(JsonFieldType.NUMBER).description("기술 태그 ID"),
                                        fieldWithPath("data.memberMissionHistory[].techTagList[].name").type(JsonFieldType.STRING).description("기술 태그 이름"),
                                        fieldWithPath("data.agreeWithEventInfo").type(JsonFieldType.BOOLEAN).description("이벤트 정보 동의 여부")
                                )
                                .build())));


    }

    @Test
    @DisplayName("주니어 회원 프로필 조회")
    void getJuniorProfileTest() throws Exception {
        // given
        Junior mockJunior = createMockJunior();
        Member mockMember = createMockJuniorMember(mockJunior);
        String memberAccessToken = getMockToken(mockMember);
        List<TechTag> mockTechTags = createMockTechTags();
        Mission mockMission = createMockMission(mockMember);
        List<MissionRegistration> mockMissionRegistrations = createMockMissionRegistrations(mockMission, mockMember);
        List<TechTagPerMember> mockMemberTechTags = createMockMemberTechTags(mockMember);

        given(memberRepository.findById(anyLong())).willReturn(Optional.of(mockMember));
        given(techTagPerMemberRepository.findWithTechTagByMemberId(anyLong())).willReturn(mockMemberTechTags);
        given(missionRegistrationRepository.findAllByJuniorMemberIdWithMission(anyLong())).willReturn(List.of());
        given(juniorRepository.findByMember_MemberId(anyLong())).willReturn(Optional.of(mockJunior));
        given(techTagPerMissionRepository.findTechTagsByMissionId(anyLong())).willReturn(mockTechTags);
        given(memberRepository.findOneByGithubId(anyString())).willReturn(Optional.of(mockMember));
        given(missionRegistrationRepository.findAllByJuniorMemberIdWithMission(anyLong())).willReturn(mockMissionRegistrations);

        // when

        // then
        ResultActions actions = mockMvc.perform(get("/v1/member/profile")
                        .header("Authorization", "Bearer " + memberAccessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseCode").value(0))
                .andExpect(jsonPath("$.message").value("Ok"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.memberId").value(48))
                .andExpect(jsonPath("$.data.memberType").value("JUNIOR"))
                .andExpect(jsonPath("$.data.githubId").value("test-token-junior"))
                .andExpect(jsonPath("$.data.nickName").value("test-token-junior"))
                .andExpect(jsonPath("$.data.profileImageUrl").value("https://avatars.githubusercontent.com/u/899645?v=4"))
                .andExpect(jsonPath("$.data.introduction").value("Test Junior Developer"))
                .andExpect(jsonPath("$.data.techTagList", hasSize(1)))
                .andExpect(jsonPath("$.data.techTagList[0].techTagId").value(1))
                .andExpect(jsonPath("$.data.techTagList[0].name").value("Java"))
                .andExpect(jsonPath("$.data.memberDetailInfo.educationalHistory").value("대학생"))
                .andExpect(jsonPath("$.data.memberMissionHistory", hasSize(1)))
                .andExpect(jsonPath("$.data.memberMissionHistory.[0].missionId").value(27))
                .andExpect(jsonPath("$.data.memberMissionHistory.[0].missionTitle").value("당근마켓 리드가 직접 알려주는 당근마켓 인프라 찍먹하기"))
                .andExpect(jsonPath("$.data.memberMissionHistory.[0].techTagList.[0].techTagId").value(7))
                .andExpect(jsonPath("$.data.memberMissionHistory.[0].techTagList.[0].name").value("Android"))
                .andExpect(jsonPath("$.data.agreeWithEventInfo").value(true));

        // document
        actions
                .andDo(document("get-junior-profile",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .summary("[멤버] 주니어 프로필 조회")
                                .description(
                                        CustomMDGenerator.builder()
                                                .h1("[Descriptions]")
                                                .h3("주니어 프로필을 조회한다.")
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
                                .tag("Member")
                                .requestHeaders(
                                        headerWithName("Authorization").description("access token")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("responseCode").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 ID"),
                                        fieldWithPath("data.memberType").type(JsonFieldType.STRING).description("회원 타입 - SENIOR, JUNIOR"),
                                        fieldWithPath("data.githubId").type(JsonFieldType.STRING).description("회원의 Github ID"),
                                        fieldWithPath("data.nickName").type(JsonFieldType.STRING).description("회원 닉네임"),
                                        fieldWithPath("data.profileImageUrl").type(JsonFieldType.STRING).description("회원 프로필 이미지 URL"),
                                        fieldWithPath("data.introduction").type(JsonFieldType.STRING).description("자기 소개"),
                                        fieldWithPath("data.techTagList").type(JsonFieldType.ARRAY).description("기술 태그 리스트"),
                                        fieldWithPath("data.techTagList[].techTagId").type(JsonFieldType.NUMBER).description("기술 태그 ID"),
                                        fieldWithPath("data.techTagList[].name").type(JsonFieldType.STRING).description("기술 태그 이름"),
                                        fieldWithPath("data.memberDetailInfo.educationalHistory").type(JsonFieldType.STRING).description("회원의 학력 정보"),
                                        fieldWithPath("data.memberMissionHistory").type(JsonFieldType.ARRAY).description("회원의 미션 히스토리"),
                                        fieldWithPath("data.memberMissionHistory[].missionId").type(JsonFieldType.NUMBER).description("미션 ID"),
                                        fieldWithPath("data.memberMissionHistory[].missionTitle").type(JsonFieldType.STRING).description("미션 제목"),
                                        fieldWithPath("data.memberMissionHistory[].techTagList").type(JsonFieldType.ARRAY).description("미션에 사용된 기술 태그 리스트"),
                                        fieldWithPath("data.memberMissionHistory[].techTagList[].techTagId").type(JsonFieldType.NUMBER).description("기술 태그 ID"),
                                        fieldWithPath("data.memberMissionHistory[].techTagList[].name").type(JsonFieldType.STRING).description("기술 태그 이름"),
                                        fieldWithPath("data.agreeWithEventInfo").type(JsonFieldType.BOOLEAN).description("이벤트 정보 동의 여부")
                                )
                                .build())));


    }

    private List<TechTag> createMockTechTags() {
        return List.of(
                TechTag.builder()
                        .techTagId(7L)
                        .name("Android")
                        .build()
        );
    }

    private Senior createMockSenior() {
        return Senior.builder()
                .seniorId(5L)
                .companyInfo("(주)TestCompany")
                .careerPeriod(24)
                .position("안드로이드 엔지니어")
                .accountNumber("1234-5678-910")
                .bank(Bank.K_BANK)
                .build();
    }

    private Junior createMockJunior() {
        return Junior.builder()
                .juniorId(5L)
                .educationalHistory("대학생")
                .realName("홍길동")
                .build();
    }

    private Member createMockSeniorMember(Senior senior) {
        Member member = Member.builder()
                .memberId(48L)
                .githubId("test-token-senior")
                .nickName("test-token-senior")
                .profileImageUrl("https://avatars.githubusercontent.com/u/899645?v=4")
                .introduction("Test Senior Developer")
                .isAgreeWithEventInfo(true)
                .senior(senior)
                .build();

        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        return member;
    }

    private Member createMockJuniorMember(Junior junior) {
        Member member = Member.builder()
                .memberId(48L)
                .githubId("test-token-junior")
                .nickName("test-token-junior")
                .profileImageUrl("https://avatars.githubusercontent.com/u/899645?v=4")
                .introduction("Test Junior Developer")
                .isAgreeWithEventInfo(true)
                .junior(junior)
                .build();

        member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));
        return member;
    }

    private Mission createMockMission(Member mockWriter) {
        return Mission.builder()
                .missionId(27L)
                .writer(mockWriter)
                .missionRepositoryUrl("https://www.github.com/kxxhyorim")
                .title("당근마켓 리드가 직접 알려주는 당근마켓 인프라 찍먹하기")
                .missionStatus(MissionStatus.RECRUITING)
                .description("content")
                .recommendTo("ReomnnandTo")
                .notRecommendTo("notReomnnandTo")
                .registrationDueDate(LocalDate.now().plusDays(7))
                .price(10000)
                .maxPeopleNumber(10)
                .build();
    }

    private List<TechTagPerMember> createMockMemberTechTags(Member mockMember) {
        return List.of(
                TechTagPerMember.builder()
                        .techTagPerMemberId(1L)
                        .member(mockMember)
                        .techTag(TechTag.builder()
                                .techTagId(1L)
                                .name("Java")
                                .build())
                        .build()
        );
    }

    private List<MissionRegistration> createMockMissionRegistrations(Mission mission, Member member) {
        return List.of(
                MissionRegistration.builder()
                        .registrationId(1L)
                        .status(ProcessStatus.MISSION_PROCEEDING)
                        .githubPullRequestUrl("test pr url")
                        .mission(mission)
                        .junior(member)
                        .build()
        );
    }

    private String getMockToken(Member member) {
        return tokenProvider.createToken(member.getGithubId(), TokenType.ACCESS_TOKEN);
    }
}