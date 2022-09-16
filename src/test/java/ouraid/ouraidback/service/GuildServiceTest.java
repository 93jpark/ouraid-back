package ouraid.ouraidback.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Community;
import ouraid.ouraidback.domain.Guild;
import ouraid.ouraidback.domain.GuildMember;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.Server;
import ouraid.ouraidback.repository.GuildRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional @Slf4j
public class GuildServiceTest {

    @Autowired GuildService guildService;
    @Autowired MemberService memberService;
    @Autowired CommunityService communityService;

    @Test
    public void 길드_생성() throws Exception {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        memberService.registerMember(newMember);
        Guild newGuild = Guild.create(Server.SHUSIA, "void", 3, newMember, null);

        //when
        Long guildId = guildService.registerGuild(newGuild);
        Guild findGuild = guildService.findGuildByName("void").get(0);

        //then
        assertEquals(findGuild.getName(), "void");
        assertEquals(findGuild.getGuildMaster().getNickname(), "유니츠");
        assertEquals(findGuild.getServer(), Server.SHUSIA);
        assertEquals(findGuild.getLevel(), 3);
        assertEquals(findGuild.getJoinedCommunity(), null);
    }

    @Test(expected = IllegalStateException.class)
    public void 길드_중복_등록() {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        memberService.registerMember(newMember);
        Guild newGuild = Guild.create(Server.SHUSIA, "void", 3, newMember, null);
        Guild otherGuild = Guild.create(Server.SHUSIA, "void", 3, newMember, null);

        //when
        guildService.registerGuild(newGuild);
        guildService.registerGuild(otherGuild);

        //then
        fail("registered duplicated guild name");
    }

    @Test
    public void 길드_레벨_변경() throws Exception {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        memberService.registerMember(newMember);
        Guild newGuild = Guild.create(Server.SHUSIA, "void", 3, newMember, null);
        Long guildId = guildService.registerGuild(newGuild);

        //when
        Guild findGuild = guildService.findById(guildId);
        guildService.updateGuildLevel(findGuild.getId(), 10);

        //then
        assertEquals(findGuild.getLevel(), 10);
    }

    @Test
    public void 길드_마스터_변경() throws Exception {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        memberService.registerMember(newMember);
        Member otherMember = Member.create("블링벨", "bling@gmail.com", "123", Server.SHUSIA);
        memberService.registerMember(otherMember);
        Guild newGuild = Guild.create(Server.SHUSIA, "void", 3, newMember, null);
        Long guildId = guildService.registerGuild(newGuild);

        //when
        Member findMember = memberService.findMemberByName("블링벨").get(0);
        Guild findGuild = guildService.findGuildByName("void").get(0);
        guildService.changeGuildMaster(findGuild.getId(), findMember.getId());

        //then
        assertEquals(newGuild.getGuildMaster().getNickname(), "블링벨");
    }

    @Test
    public void 소속_연합명_길드_조회() throws Exception {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        memberService.registerMember(newMember);
        Guild guildA = Guild.create(Server.SHUSIA, "void", 3, newMember, null);
        Long guildAId = guildService.registerGuild(guildA);
        Guild guildB = Guild.create(Server.SHUSIA, "voider", 3, newMember, null);
        Long guildBId = guildService.registerGuild(guildB);
        Guild guildC = Guild.create(Server.SHUSIA, "탑클", 3, newMember, null);
        Long guildCId = guildService.registerGuild(guildC);
        Community community = Community.create(Server.SHUSIA, "void", newMember);
        communityService.registerCommunity(community);
        guildA.joinNewCommunity(community);
        guildB.joinNewCommunity(community);
        guildC.joinNewCommunity(community);

        //when
        int communitySize = guildService.findGuildByJoinedCommunity("void").size();

        //then
        assertEquals(3, communitySize);
    }

    @Test
    //@Rollback(false)
    public void 길드멤버_테스트() throws Exception {
        //given
        Member memberA = Member.create("블링벨", "blingbell@gmail.com", "123", Server.SHUSIA);
        memberService.registerMember(memberA);

        Member memberB = Member.create("떼바", "theva@gmail.com", "123", Server.SHUSIA);
        memberService.registerMember(memberB);

        Member memberC = Member.create("오수재", "o-sujae@gmail.com", "123", Server.SHUSIA);
        memberService.registerMember(memberC);

        Member memberD = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        memberService.registerMember(memberD);

        Guild guildA = Guild.create(Server.SHUSIA, "void", 3, memberD, null);
        Long guildAId = guildService.registerGuild(guildA);
        Guild guildB = Guild.create(Server.SHUSIA, "voider", 3, memberD, null);
        Long guildBId = guildService.registerGuild(guildB);
        Guild guildC = Guild.create(Server.SHUSIA, "탑클", 3, memberD, null);
        Long guildCId = guildService.registerGuild(guildC);


        Community community = Community.create(Server.SHUSIA, "void", memberD);
        communityService.registerCommunity(community);

        guildA.joinNewCommunity(community);
        guildB.joinNewCommunity(community);
        guildC.joinNewCommunity(community);

        guildA.joinGuildMember(memberA);
        guildA.joinGuildMember(memberB);
        guildA.joinGuildMember(memberC);
        guildA.joinGuildMember(memberD);

        //when


        //then
    }
}
