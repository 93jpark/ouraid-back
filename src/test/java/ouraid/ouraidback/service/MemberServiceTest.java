package ouraid.ouraidback.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.Repository.MemberRepository;
import ouraid.ouraidback.Service.MemberService;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.Server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CreateMember {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    //@Rollback(false)
    public void createMember() throws Exception {

        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        Long savedMemberId = memberService.registerMember(newMember);

        //when
        Member findMember = memberRepository.findByNickname("유니츠");

        //then
        assertEquals(savedMemberId, findMember.getId());
    }

    @Test
    //Rollback(false)
    public void validateDuplicatedMember() throws Exception {
        //given
        Member newMember = Member.create("유니츠", "93jpark@gmail.com", "123", Server.SHUSIA);
        Long savedMemberId = memberService.registerMember(newMember);

        //when
        Member findMember = memberRepository.findByNickname("유니츠");

        //then
        fail("Member/CreateMember/ 'validation for duplicated member failed'");
    }
}
