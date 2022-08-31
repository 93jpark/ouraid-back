package ouraid.ouraidback.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Guild;
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
    @Autowired GuildRepository guildRepository;
    @Autowired MemberService memberService;

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
        guildService.changeGuildMaster(findGuild.getId(), findMember);

        //then
        assertEquals(newGuild.getGuildMaster().getNickname(), "블링벨");
    }
}
