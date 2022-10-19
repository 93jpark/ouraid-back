package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.Exception.DuplicateResourceException;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Guild;
import ouraid.ouraidback.domain.GuildMember;
import ouraid.ouraidback.repository.CharacterRepository;
import ouraid.ouraidback.repository.MemberRepository;
import ouraid.ouraidback.domain.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    //private final GuildService guildService;
    private final CharacterService characterService;
    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;

    // 회원가입
    @Transactional
    public Long registerMember(Member member) {
        validateDuplicatedMemberNickname(member.getNickname());
        validateDuplicatedMemberEmail(member.getEmail());
        Long memberId = memberRepository.register(member);
        log.info(member.getNickname()+" has been registered");
        return memberId;
    }

    // 회원 탈퇴
    @Transactional
    public void memberWithdraw(Member member) {
        if(member.getAvailability()) {
            member.changeMemberStatus();
        } else {
            log.info("{}'s account is already inactivated.", member.getNickname());
        }
    }

        /* 회원 정보 수정 - 이름, 패스워드 */
    // 회원 이름 수정
    @Transactional
    public void updateMemberNickname(Long memberId, String newName) {
        try {
            validateDuplicatedMemberNickname(newName);
            Member member = memberRepository.findMember(memberId);
            member.updateMemberNickname(newName);
        } catch (DuplicateResourceException e) {
            throw e;
        }
    }

    @Transactional
    public void changeMemberPassword(Long memberId, String newPassword) {
        try {
            Member findMember = memberRepository.findMember(memberId);
            findMember.updateMemberPassword(newPassword);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Transactional
    public void changeMemberEmail(Long memberId, String newEmail) {
        try {
            Member findMember = memberRepository.findMember(memberId);
            validateDuplicatedMemberEmail(newEmail);
            findMember.updateMemberEmail(newEmail);
        } catch (DuplicateResourceException e) {
            throw e;
        }
    }

    // Member의 모든 캐릭터 삭제
    @Transactional
    public void removeMemberOwnCharacters(Member member) {
        List<Characters> charList = characterRepository.findOwnCharactersByMemberName(member.getNickname());
        for (Characters c : charList) {
            log.info("{} is removed", c.getName());
            characterService.removeCharacter(c.getId());
        }
    }

    // 회원 검색
    @Transactional(readOnly = true) // 모두 조회
    public List<Member> findAllMembers() { return memberRepository.findAll(); }

    @Transactional(readOnly = true) // id 기반
    public Member findMemberById(Long memberId) { return memberRepository.findMember(memberId); }

    @Transactional(readOnly = true) // 닉네임 기반
    public List<Member> findMemberByName(String name) { return memberRepository.findByNickname(name); }

    @Transactional(readOnly = true) // 서버 기반
    public List<Member> findMembersByServer(String serverName) { return memberRepository.findByServer(serverName); }

    @Transactional(readOnly = true) // 커뮤니티 기반
    public List<Member> findMembersByCommunity(String cName) { return memberRepository.findByCommunity(cName); }

    @Transactional(readOnly = true) // 길드 기반
    public List<Member> findMembersByGuild(String gName) { return memberRepository.findByGuild(gName); }

    // 중복 닉네임 회원 검증
    public void validateDuplicatedMemberNickname(String newMemberName) {
        List<Member> findMember =  memberRepository.findByNickname(newMemberName);
        if(!findMember.isEmpty()){
            throw new DuplicateResourceException("nickname:"+newMemberName);
        }
        log.info("MemberService.validateDuplicateMemberNickname() : 사용가능한 닉네임");
    }

    // 중복 이메일 회원 검증
    public void validateDuplicatedMemberEmail(String newMemberEmail) {
        List<Member> findMember =  memberRepository.findbyEmail(newMemberEmail);
        if(!findMember.isEmpty()){
            throw new DuplicateResourceException("email:"+newMemberEmail);
        }
        log.info("MemberService.validateDuplicateMemberEmail() : 사용가능한 이메일");
    }

}
