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
import ouraid.ouraidback.domain.enums.*;
import ouraid.ouraidback.domain.party.*;

import java.time.LocalDateTime;
import java.time.Instant;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static ouraid.ouraidback.domain.enums.ParticipantStatus.*;
import static ouraid.ouraidback.domain.enums.ParticipantType.DRIVER;
import static ouraid.ouraidback.domain.enums.ParticipantType.RIDER;
import static ouraid.ouraidback.domain.enums.PartyType.ASSIST;
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
        NormalLotus nlParty = NormalLotus.createNormalLotusParty(OPEN, SHUSIA, member, character, Instant.now());
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
        NormalLotus nlParty = NormalLotus.createNormalLotusParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(nlParty);

        HardLotus hlParty = HardLotus.createHardLotusParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(hlParty);

        Dungeon dParty = Dungeon.createDungeonParty(OPEN, SHUSIA, member, character, Instant.now());
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
    public void 파티삭제_잔여참여자_삭제확인() throws Exception {
        //given
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);

        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);

        Party nlParty = NormalLotus.createNormalLotusParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(nlParty);

        Member newMember = Member.create("바우", "bau@gmail.com", "123", SHUSIA);
        memberService.registerMember(newMember);

        Characters newChar = Characters.create(SHUSIA, "바우", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        characterService.registerCharacter(newChar);

        partyService.joinCharacterOnParty(nlParty.getId(), newChar.getId());

        // when
        partyService.removeParty(nlParty);

        // then
        Assert.assertEquals(0, partyService.findAllParticipant(nlParty.getId()).size());
        Characters findChar = characterService.findOne(character.getId());
        Member findMember = memberService.findMemberById(member.getId());
        Assert.assertEquals("유니츠", findChar.getName());
        Assert.assertEquals("유니츠", findMember.getNickname());

    }

    @Test
    public void 파티_대기_참여자_검색() throws Exception {
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
        Assert.assertEquals(1, partyService.findPartyParticipantWithStatus(wParty.getId(), WAIT).size());
        Assert.assertEquals(1, partyService.findPartyParticipantWithStatus(wParty.getId(), HOLDER).size());

    }

    @Test
    public void 한멤버_중복대기캐릭_참여() throws Exception {
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);
        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);

        Member newMember = Member.create("바우", "bau@gmail.com", "123", SHUSIA);
        memberService.registerMember(newMember);

        Characters newChar = Characters.create(SHUSIA, "바우", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        characterService.registerCharacter(newChar);

        Characters otherChar = Characters.create(SHUSIA, "톡찍", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        characterService.registerCharacter(otherChar);

        Party wParty = WorldBoss.createWorldBossParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(wParty);

        //when
        partyService.joinCharacterOnParty(wParty.getId(), newChar.getId());
        partyService.joinCharacterOnParty(wParty.getId(), otherChar.getId());

        //then
        assertEquals(3, partyService.findAllParticipant(wParty.getId()).size());
        assertEquals(1, partyService.findPartyParticipantWithStatus(wParty.getId(), HOLDER).size());
        assertEquals(2, partyService.findPartyParticipantWithStatus(wParty.getId(), WAIT).size());

    }

    @Test(expected = Exception.class)
    public void 한멤버_중복캐릭_승인() throws Exception {
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);
        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);

        Member newMember = Member.create("바우", "bau@gmail.com", "123", SHUSIA);
        memberService.registerMember(newMember);

        Characters newChar = Characters.create(SHUSIA, "바우", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        characterService.registerCharacter(newChar);

        Characters otherChar = Characters.create(SHUSIA, "톡찍", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        characterService.registerCharacter(otherChar);

        Party wParty = WorldBoss.createWorldBossParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(wParty);

        //when
        partyService.joinCharacterOnParty(wParty.getId(), newChar.getId());
        partyService.joinCharacterOnParty(wParty.getId(), otherChar.getId());

        PartyParticipant accept = partyService.findPartyParticipant(wParty.getId(), newChar.getId()).get(0);
        partyService.acceptParticipant(accept.getId());

        PartyParticipant accept2 = partyService.findPartyParticipant(wParty.getId(), otherChar.getId()).get(0);
        partyService.acceptParticipant(accept2.getId());

        //then
        fail("duplicated member's character has been accepted on one party");

    }

    @Test
    public void 파티_참여자_승인_거절() throws Exception {
        Member member = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(member);
        Characters character = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, member);
        characterService.registerCharacter(character);

        Member newMember = Member.create("바우", "bau@gmail.com", "123", SHUSIA);
        memberService.registerMember(newMember);

        Characters newChar = Characters.create(SHUSIA, "바우", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        characterService.registerCharacter(newChar);

        Characters otherChar = Characters.create(SHUSIA, "톡찍", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        characterService.registerCharacter(otherChar);

        Party wParty = WorldBoss.createWorldBossParty(OPEN, SHUSIA, member, character, Instant.now());
        partyService.registerParty(wParty);

        //when
        partyService.joinCharacterOnParty(wParty.getId(), newChar.getId());
        partyService.joinCharacterOnParty(wParty.getId(), otherChar.getId());

        PartyParticipant accept = partyService.findPartyParticipant(wParty.getId(), newChar.getId()).get(0);
        partyService.acceptParticipant(accept.getId());

        PartyParticipant repel = partyService.findPartyParticipant(wParty.getId(), otherChar.getId()).get(0);
        partyService.repelParticipant(repel.getId());

        //then
        assertEquals(3, partyService.findAllParticipant(wParty.getId()).size());
        assertEquals(1, partyService.findPartyParticipantWithStatus(wParty.getId(), HOLDER).size());
        assertEquals(1, partyService.findPartyParticipantWithStatus(wParty.getId(), ACCEPTED).size());
        assertEquals(1, partyService.findPartyParticipantWithStatus(wParty.getId(), DECLINED).size());

    }

    @Test(expected = Exception.class)
    public void 파티_참여자_승인_초과() throws Exception {
        Member holderMember = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(holderMember);
        Characters holderChar = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, holderMember);
        characterService.registerCharacter(holderChar);

        Member memberA = Member.create("바우", "bau@gmail.com", "123", SHUSIA);
        memberService.registerMember(memberA);

        Characters charA = Characters.create(SHUSIA, "바우", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, memberA);
        characterService.registerCharacter(charA);

        Member memberB = Member.create("블링벨", "bling@gmail.com", "123", SHUSIA);
        memberService.registerMember(memberB);

        Characters charB = Characters.create(SHUSIA, "블링블루", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, memberB);
        characterService.registerCharacter(charB);

        Member memberC = Member.create("아이리", "i-ri@gmail.com", "123", SHUSIA);
        memberService.registerMember(memberC);

        Characters charC = Characters.create(SHUSIA, "아이리크루", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, memberB);
        characterService.registerCharacter(charC);


        Party wParty = WorldBoss.createWorldBossParty(OPEN, SHUSIA, holderMember, holderChar, Instant.now());
        partyService.registerParty(wParty);

        //when
        partyService.joinCharacterOnParty(wParty.getId(), charA.getId());
        partyService.joinCharacterOnParty(wParty.getId(), charB.getId());
        partyService.joinCharacterOnParty(wParty.getId(), charC.getId());

        PartyParticipant accept = partyService.findPartyParticipant(wParty.getId(), charA.getId()).get(0);
        partyService.acceptParticipant(accept.getId());

        PartyParticipant accept2 = partyService.findPartyParticipant(wParty.getId(), charB.getId()).get(0);
        partyService.acceptParticipant(accept2.getId());

        PartyParticipant accept3 = partyService.findPartyParticipant(wParty.getId(), charC.getId()).get(0);
        partyService.acceptParticipant(accept3.getId());



        //then
        assertEquals(3, partyService.findAllParticipant(wParty.getId()).size());
        assertEquals(1, partyService.findPartyParticipantWithStatus(wParty.getId(), HOLDER).size());
        assertEquals(1, partyService.findPartyParticipantWithStatus(wParty.getId(), ACCEPTED).size());
        assertEquals(1, partyService.findPartyParticipantWithStatus(wParty.getId(), DECLINED).size());

    }

    @Test
    public void 업둥이_파티_생성_참여() throws Exception {
        //given
        Member holderMember = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(holderMember);
        Characters holderChar = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, holderMember);
        characterService.registerCharacter(holderChar);

        Member memberA = Member.create("바우", "bau@gmail.com", "123", SHUSIA);
        memberService.registerMember(memberA);

        Characters charA = Characters.create(SHUSIA, "바우", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, memberA);
        characterService.registerCharacter(charA);

        Characters charB = Characters.create(SHUSIA, "톡찍", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, memberA);
        characterService.registerCharacter(charB);

        //when
        Party hlParty = HardLotus.createHardLotusParty(OPEN, SHUSIA, holderMember, holderChar, Instant.now(), ASSIST, 1, 1.8);
        partyService.registerAssistParty(hlParty);

        partyService.joinCharacterOnPartyWithType(hlParty.getId(), charA.getId(), RIDER);
        partyService.joinCharacterOnPartyWithType(hlParty.getId(), charB.getId(), ParticipantType.NORMAL);

        //then
        assertEquals(3, partyService.findAllParticipant(hlParty.getId()).size());
        assertEquals(1, partyService.findPartyParticipantWithStatus(hlParty.getId(), HOLDER).size());
        assertEquals(1, partyService.findPartyParticipantWithType(hlParty.getId(), DRIVER).size());
        assertEquals(1, partyService.findPartyParticipantWithType(hlParty.getId(), RIDER).size());

    }

    @Test(expected = Exception.class)
    public void 업둥이_파티_생성_참여_초과() throws Exception {
        //given
        Member holderMember = Member.create("유니츠", "93jpark@gmail.com", "123", SHUSIA);
        memberService.registerMember(holderMember);
        Characters holderChar = Characters.create(SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, holderMember);
        characterService.registerCharacter(holderChar);

        Member memberA = Member.create("바우", "bau@gmail.com", "123", SHUSIA);
        memberService.registerMember(memberA);

        Characters charA = Characters.create(SHUSIA, "바우", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, memberA);
        characterService.registerCharacter(charA);

        Characters charB = Characters.create(SHUSIA, "톡찍", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, memberA);
        characterService.registerCharacter(charB);

        //when
        Party hlParty = HardLotus.createHardLotusParty(OPEN, SHUSIA, holderMember, holderChar, Instant.now(), ASSIST, 1, 1.8);
        partyService.registerAssistParty(hlParty);
        Long pp1 = partyService.joinCharacterOnPartyWithType(hlParty.getId(), charA.getId(), RIDER);
        Long pp2 = partyService.joinCharacterOnPartyWithType(hlParty.getId(), charB.getId(), RIDER);

        partyService.acceptParticipant(pp1);
        partyService.acceptParticipant(pp2);

        //then
        fail("exceed free rider capacity");
    }




}
