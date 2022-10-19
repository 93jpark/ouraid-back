package ouraid.ouraidback.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.Exception.DuplicateResourceException;
import ouraid.ouraidback.repository.MemberRepository;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.Server;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional @Slf4j
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 단일_멤버_생성() throws Exception {
        //given
        Member newMember = Member.create("유니츠1", "9321jpark@gmail.com", "123", Server.SHUSIA);
        Long savedMemberId = memberService.registerMember(newMember);

        //when
        Member findMember = memberRepository.findByNickname("유니츠1").get(0);

        //then
        assertEquals(savedMemberId, findMember.getId());
    }

    @Test(expected = DuplicateResourceException.class)
    public void 중복_회원_예외() {
        //given
        Member newMember = Member.create("유니츠테스트", "mock1@gmail.com", "123", Server.SHUSIA);
        Member dupMember = Member.create("유니츠테스트", "mock@gmail.com", "123", Server.SHUSIA);

        //when
        memberService.registerMember(newMember);
        memberService.registerMember(dupMember);

        //then
        fail("Member/CreateMember/ 'validation for duplicated member failed'");
    }


    @Test
    public void 멤버_정보_수정() throws Exception {
        //given
        Member newMember = Member.create("유니츠1", "9321jpark@gmail.com", "123", Server.SHUSIA);
        Long memberId = memberService.registerMember(newMember);
        //when
        memberService.updateMemberNickname(memberId, "유우니이츠으");
        Member findMember = memberService.findMemberByName("유우니이츠으").get(0);
        //then
        assertEquals(newMember.getNickname(), "유우니이츠으");
    }

    @Test
    public void 멤버_비활성화() {
        //given
        Member newMember = Member.create("유니츠1", "9321jpark@gmail.com", "123", Server.SHUSIA);
        Long memberId = memberService.registerMember(newMember);

        //when
        newMember.changeMemberStatus();
        Member findMember = memberRepository.findMember(memberId);

        //then
        assertEquals(findMember.getAvailability(), false);
    }

}
