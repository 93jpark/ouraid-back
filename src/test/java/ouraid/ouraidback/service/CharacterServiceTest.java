package ouraid.ouraidback.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.enums.MainClass;
import ouraid.ouraidback.domain.enums.Server;
import ouraid.ouraidback.domain.enums.SubClass;
import ouraid.ouraidback.repository.CharacterRepository;
import ouraid.ouraidback.repository.MemberRepository;
import ouraid.ouraidback.domain.*;
import ouraid.ouraidback.domain.Characters;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional @Slf4j
public class CharacterServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CharacterService characterService;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 캐릭터_기본정보_생성() {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        Long savedMemberId = memberService.registerMember(newMember);
        Characters newCharacter = Characters.create(Server.SHUSIA, "유니처", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        Long savedCharId = characterService.registerCharacter(newCharacter);

        //when
        Characters findCharacter = characterRepository.findOne(savedCharId);

        //then
        assertEquals(savedCharId, findCharacter.getId());
        assertEquals(findCharacter.getName(), "유니처");
        assertEquals(findCharacter.getAbility(), BigDecimal.valueOf(1.8));
        assertEquals(findCharacter.getServer(), Server.SHUSIA);

    }

    @Test(expected = IllegalStateException.class)
    public void 캐릭터_생성_중복이름감지() {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        Long savedMemberId = memberService.registerMember(newMember);
        Characters newCharacter = Characters.create(Server.SHUSIA, "유니처", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        Characters dupCharacter = Characters.create(Server.SHYLOCK, "유니처", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);

        //when
        characterService.registerCharacter(newCharacter);
        characterService.registerCharacter(dupCharacter);

        //then
        fail("FAIL: Duplicated character name is registered");

    }

    @Test
    public void 캐릭터_이름_변경() throws Exception {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        Long savedMemberId = memberService.registerMember(newMember);
        Characters newCharacter = Characters.create(Server.SHUSIA, "유니처", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        Long savedCharId = characterService.registerCharacter(newCharacter);

        //when
        characterService.updateCharacterName(savedCharId, "유닛츠");
        Characters findChar = characterRepository.findOne(savedCharId);

        //then
        assertEquals(findChar.getName(), "유닛츠");
    }

    @Test
    public void 캐릭터_항마_변경() throws Exception {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        Long savedMemberId = memberService.registerMember(newMember);
        Characters newCharacter = Characters.create(Server.SHUSIA, "유니처", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, newMember);
        Long savedCharId = characterService.registerCharacter(newCharacter);

        //when
        characterService.updateCharacterAbility(savedCharId, 9.9);
        Characters findChar = characterRepository.findOne(savedCharId);
        log.info(findChar.getAbility().getClass().toString());

        //then
        assertEquals(findChar.getAbility().setScale(2), BigDecimal.valueOf(9.90).setScale(2));
    }
}
