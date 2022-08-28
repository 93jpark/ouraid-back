package ouraid.ouraidback.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.Repository.CharacterRepository;
import ouraid.ouraidback.Repository.MemberRepository;
import ouraid.ouraidback.Service.CharacterService;
import ouraid.ouraidback.Service.MemberService;
import ouraid.ouraidback.domain.*;
import ouraid.ouraidback.domain.Character;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CreateCharacter {

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
        Character newCharacter = Character.create(Server.SHUSIA, "유니처", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, (float) 2.1, newMember);
        Long savedCharId = characterRepository.register(newCharacter);

        //when
        Character findCharacter = characterRepository.findByName("유니처");

        //then
        assertEquals(savedCharId, findCharacter.getId());
    }
}
