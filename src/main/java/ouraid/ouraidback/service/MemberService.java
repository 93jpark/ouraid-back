package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        validateDuplicatedMember(member.getNickname());
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
            validateDuplicatedMember(newName);
            Member member = memberRepository.findOne(memberId);
            member.updateMemberNickname(newName);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    @Transactional
    public void changeMemberPassword(Long memberId, String newPassword) {
        try {
            Member findMember = memberRepository.findOne(memberId);
        } catch (Exception e) {
            log.info(e.getMessage());
        }


    }

    // Member의 모든 캐릭터 삭제
    @Transactional
    public void removeMemberOwnCharacters(Member member) {
        List<Characters> charList = characterRepository.findOwnCharactersByMember(member.getNickname());
        for (Characters c : charList) {
            log.info("{} is removed", c.getName());
            characterService.removeCharacter(c.getId());
        }
    }

    // Member의 길드 탈퇴
    @Transactional
    public void leaveMemberJoinedGuild(Member member, Guild guild) {

    }

    // 회원 검색
    @Transactional(readOnly = true) // 모두 조회
    public List<Member> findAllMembers() { return memberRepository.findAll(); }

    @Transactional(readOnly = true) // id 기반
    public Member findMemberById(Long memberId) { return memberRepository.findOne(memberId); }

    @Transactional(readOnly = true) // 닉네임 기반
    public List<Member> findMemberByName(String name) { return memberRepository.findByNickname(name); }

    @Transactional(readOnly = true) // 서버 기반
    public List<Member> findMembersByServer(String serverName) { return memberRepository.findByServer(serverName); }

    @Transactional(readOnly = true) // 커뮤니티 기반
    public List<Member> findMembersByCommunity(String cName) { return memberRepository.findByCommunity(cName); }

    @Transactional(readOnly = true) // 길드 기반
    public List<Member> findMembersByGuild(String gName) { return memberRepository.findByGuild(gName); }

    // 중복회원 검증
    public void validateDuplicatedMember(String newMemberName) {
        List<Member> findMember =  memberRepository.findByNickname(newMemberName);
        if(!findMember.isEmpty()){
            throw new IllegalStateException("MemberService.validateDuplicateMember() : 이미 존재하는 회원");
        }
        log.info("MemberService.validateDuplicateMember() : 사용가능한 회원정보");
    }
}
