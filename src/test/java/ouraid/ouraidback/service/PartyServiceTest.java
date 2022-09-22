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
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.MainClass;
import ouraid.ouraidback.domain.enums.RecruitType;
import ouraid.ouraidback.domain.enums.Server;
import ouraid.ouraidback.domain.enums.SubClass;
import ouraid.ouraidback.domain.party.*;

import java.time.LocalDateTime;
import java.time.Instant;
import java.util.*;

import static org.junit.Assert.fail;
import static ouraid.ouraidback.domain.enums.RecruitType.OPEN;
import static ouraid.ouraidback.domain.enums.Server.SHUSIA;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional @Slf4j
public class PartyServiceTest {
    @Autowired PartyService partyService;
    @Autowired MemberService memberService;
    @Autowired CharacterService characterService;
    @Autowired GuildService guildService;
    @Autowired CommunityService communityService;

    @Test
    public void 일반_로터스_생성() throws Exception {
        //given
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);
        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);

        //when
        Party nlParty = NormalLotus.createNormalLotusParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(nlParty);
        List<Party> partyList = partyService.findAllParty();

        //then
        Assert.assertEquals(1, partyList.size());
    }

    @Test
    public void 하드_로터스_생성() throws Exception {
        //given
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);
        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);

        //when
        Party hlParty = HardLotus.createHardLotusParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(hlParty);
        List<Party> partyList = partyService.findAllParty();

        //then
        Assert.assertEquals(1, partyList.size());
    }

    @Test
    public void 던전_파티_생성() throws Exception {
        //given
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);
        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);

        //when
        Party dParty = Dungeon.createDungeonParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(dParty);
        List<Party> partyList = partyService.findAllParty();

        //then
        Assert.assertEquals(1, partyList.size());
    }

    @Test
    public void 월보_파티_생성() throws Exception {
        //given
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);
        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);

        //when
        Party wParty = WorldBoss.createWorldBossParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(wParty);
        List<Party> partyList = partyService.findAllParty();

        //then
        Assert.assertEquals(1, partyList.size());
    }

    @Test
    public void 파티_전체검색_테스트() throws Exception {
        //given
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);
        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);

        //when
        Party nlParty = NormalLotus.createNormalLotusParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(nlParty);

        Party hlParty = HardLotus.createHardLotusParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(hlParty);

        Party dParty = Dungeon.createDungeonParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(dParty);


        List<Party> partyList = partyService.findAllParty();

        //then
        Assert.assertEquals(3, partyList.size());
    }

    @Test//(expected = NullPointerException.class)
    public void 파티삭제() throws Exception {
        //given
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);
        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);
        Party nlParty = NormalLotus.createNormalLotusParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(nlParty);

        // when
        partyService.removeParty(nlParty);

        // then
        try{
            // 영속성전이 체크
            Member findMember = memberService.findMemberById(member.getId());
            Assert.assertEquals(findMember.getId(), member.getId());
        } catch (Exception e) {
            log.info(e.getMessage());
        } finally {
            Party findParty = partyService.findByPartyId(nlParty.getId());
            Assert.assertEquals(null, findParty);
        }

    }

    @Test
    public void 파티_참여자_검색() throws Exception {
        //given
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);
        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);

        Member newMember = Member.create("바우", "bau@gmail.com", "123", SHUSIA);
        memberService.registerMember(newMember);
        Characters newChar = Characters.create(SHUSIA, "바우", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        characterService.registerCharacter(newChar);

        Party wParty = WorldBoss.createWorldBossParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(wParty);

        //when
        PartyParticipant pp = PartyParticipant.createPartyParticipant(wParty, newMember, newChar);
        partyService.joinCharacterOnParty(wParty.getId(), newChar.getId());


        //then
        Assert.assertEquals(1, partyService.findPartyParticipant(wParty.getId(), newChar.getId()).size());
    }

    @Test(expected = Exception.class)
    public void 멤버_중복캐릭_참여() throws Exception {
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);
        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);

        Member newMember = Member.create("바우", "bau@gmail.com", "123", SHUSIA);
        memberService.registerMember(newMember);
        Characters newChar = Characters.create(SHUSIA, "바우", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        characterService.registerCharacter(newChar);

        Characters otherChar = Characters.create(SHUSIA, "바우", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        characterService.registerCharacter(otherChar);

        Party wParty = WorldBoss.createWorldBossParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(wParty);

        //when
        PartyParticipant pp = PartyParticipant.createPartyParticipant(wParty, newMember, newChar);
        partyService.joinCharacterOnParty(wParty.getId(), newChar.getId());
        partyService.joinCharacterOnParty(wParty.getId(), otherChar.getId());

        //then
        //Assert.assertEquals(1, partyService.findPartyParticipant(wParty.getId(), newChar.getId()).size());
        fail("duplicated member's character registered");
    }

}
