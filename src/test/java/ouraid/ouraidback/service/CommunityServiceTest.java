package ouraid.ouraidback.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Community;
import ouraid.ouraidback.domain.Guild;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.MainClass;
import ouraid.ouraidback.domain.enums.Server;
import ouraid.ouraidback.domain.enums.SubClass;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class CommunityServiceTest {

    @Autowired CommunityService communityService;
    @Autowired MemberService memberService;
    @Autowired CharacterService characterService;
    @Autowired GuildService guildService;

    @Test
    public void 단일_연합_생성() throws Exception {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "12345", Server.SHUSIA);
        memberService.registerMember(newMember);
        Community newCom = Community.create(Server.SHUSIA, "void", newMember);

        //when
        Long comId = communityService.registerCommunity(newCom);

        //then
        Community findCom = communityService.findByComId(comId);
        assertEquals(findCom.getName(), "void");
        assertEquals(findCom.getCommunityMaster().getNickname(), "유니츠");
        assertEquals(newMember.getJoinedCommunity().getName(), "void");
    }

    @Test
    public void 연합_마스터_변경() throws Exception {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "12345", Server.SHUSIA);
        memberService.registerMember(newMember);
        Member otherMember = Member.create("블링벨", "bling@gmail.com", "12345", Server.SHUSIA);
        memberService.registerMember(otherMember);
        Community newCom = Community.create(Server.SHUSIA, "void", newMember);
        Long comId = communityService.registerCommunity(newCom);

        //when
        newCom.changeMaster(otherMember);

        //then
        log.info(String.valueOf(newMember.getOwnCommunity() == null));
        assertEquals("블링벨", newCom.getCommunityMaster().getNickname());
    }

    @Test
    public void 연합_이름_변경() throws Exception {
        //given
        Member testMember = Member.create("블링벨", "bling@gmail.com", "12345", Server.SHUSIA);
        memberService.registerMember(testMember);
        Community newCom = Community.create(Server.SHUSIA, "void", testMember);
        Long comId = communityService.registerCommunity(newCom);

        //when
        newCom.changeName("voider");

        //then
        Assert.assertEquals(newCom.getName(), "voider");
        Assert.assertEquals(testMember.getJoinedCommunity().getName(), "voider");
    }

    @Test
    public void 연합해체() throws Exception {
        //given
        Member testMember = Member.create("테스트유저1", "93jpark@gmail.com", "123456", Server.SHUSIA);
        memberService.registerMember(testMember);

        Characters testCharacter = Characters.create(Server.SHUSIA, "테스트캐릭", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, testMember);
        characterService.registerCharacter(testCharacter);

        Guild testGuild1 = Guild.create(Server.SHUSIA, "테스트길드1", 1, testMember, null);
        guildService.registerGuild(testGuild1);
        Guild testGuild2 = Guild.create(Server.SHUSIA, "테스트길드2", 1, testMember, null);
        guildService.registerGuild(testGuild2);

        Community testCommunity = Community.create(Server.SHUSIA, "테스트연합", testMember);
        communityService.registerCommunity(testCommunity);

        communityService.addGuild(testCommunity.getId(), testGuild1.getId());
        communityService.addGuild(testCommunity.getId(), testGuild2.getId());
        communityService.addMember(testCommunity.getId(), testMember.getId());
        communityService.addCharacter(testCommunity.getId(), testCharacter.getId());


        //when
        communityService.removeCommunity(testCommunity.getId());

        //then
        Assert.assertEquals(null, testGuild1.getJoinedCommunity());
        Assert.assertEquals(null, testGuild2.getJoinedCommunity());
        Assert.assertEquals(null, testMember.getJoinedCommunity());
        Assert.assertEquals(null, testCharacter.getJoinedCommunity());

    }

}
