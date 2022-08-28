package ouraid.ouraidback.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.Repository.MemberRepository;
import ouraid.ouraidback.domain.Member;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    // 회원가입
    @Transactional
    public Long registerMember(Member member) {
        validateDuplicatedMember(member);
        memberRepository.register(member);
        return member.getId();
    }

    public void validateDuplicatedMember(Member member) {
        // EXCEPTION
        List<Member> findMembers =  memberRepository.findByNickname(member.getNickname());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
