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
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.Server;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
public class CommunityServiceTest {

    @Autowired CommunityService communityService;
    @Autowired MemberService memberService;
    @Autowired CharacterService characterService;

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
    }

    @Test
    @Rollback(false)
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

}
