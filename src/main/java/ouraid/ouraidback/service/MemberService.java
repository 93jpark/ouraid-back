package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.repository.MemberRepository;
import ouraid.ouraidback.domain.Member;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional
    public Long registerMember(Member member) {
        if(validateDuplicatedMember(member)) {
            Long memberId = memberRepository.register(member);
            log.info(member.getNickname()+" has been registered");
            return memberId;
        } else {
            log.info(member.getNickname()+" cannot be registered");
            return null;

        }
    }

    // 회원탈퇴


    // 회원 정보 수정


    // 회원 검색
    @Transactional(readOnly = true) // 모두 조회
    public List<Member> findAllMembers() { return memberRepository.findAll(); }

    @Transactional(readOnly = true) // id 기반
    public Member findMemberById(Long memberId) { return memberRepository.findOne(memberId); }

    @Transactional(readOnly = true) // 닉네임 기반
    public Member findMemberByName(String name) { return memberRepository.findByNickname(name); }

    @Transactional(readOnly = true) // 서버 기반
    public List<Member> findMembersByServer(String serverName) { return memberRepository.findByServer(serverName); }

    @Transactional(readOnly = true) // 커뮤니티 기반
    public List<Member> findMembersByCommunity(String cName) { return memberRepository.findByCommunity(cName); }

    @Transactional(readOnly = true) // 길드 기반
    public List<Member> findMembersByGuild(String gName) { return memberRepository.findByGuild(gName); }

    // 중복회원 검증
    public Boolean validateDuplicatedMember(Member member) {
        try {
            Member findMember =  memberRepository.findByNickname(member.getNickname());
            if(findMember!=null) {
                throw new IllegalStateException("MemberService.validateDuplicateMember() : 이미 존재하는 회원");
            }
        } catch (EmptyResultDataAccessException e) {
            log.info("MemberService.validateDuplicateMember() : 사용가능한 회원정보");
            return true;
        }
        return false;
    }
}
