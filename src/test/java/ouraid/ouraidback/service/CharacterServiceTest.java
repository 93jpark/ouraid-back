package ouraid.ouraidback.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CharacterServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CharacterService characterService;

    @Autowired
    CharacterRepository characterRepository;

    @Test
    @Rollback(false)
    public void createCharacter() throws Exception {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        Long savedMemberId = memberService.registerMember(newMember);
        Characters newCharacter = Characters.create(Server.SHUSIA, "유니처", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 2.1, newMember);
        Long savedCharId = characterRepository.register(newCharacter);

        //when
        Characters findCharacter = characterRepository.findByCharName("유니처");

        //then
        assertEquals(savedCharId, findCharacter.getId());
    }
}
