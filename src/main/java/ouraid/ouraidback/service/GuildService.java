package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.*;
import ouraid.ouraidback.domain.Exception.ResourceNotFoundException;
import ouraid.ouraidback.repository.CharacterRepository;
import ouraid.ouraidback.repository.GuildRepository;
import ouraid.ouraidback.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuildService {

    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;
    //private final MemberService memberService;
    private final GuildRepository guildRepository;
    private final CharacterService characterService;

    @PersistenceContext
    EntityManager em;

    // 길드 생성
    @Transactional
    public Long registerGuild(Guild guild) {
        validateDuplicateGuild(guild);
        return guildRepository.register(guild);
    }

    // 길드 정보 업데이트 - 이름, 마스터, 레벨
    @Transactional
    public void changeGuildName(Long guildId, String newName) {
        /**
         * 중복검사필요
         */
    }

    @Transactional
    public void changeGuildMaster(Long guildId, Long newMasterId) {
        Guild findGuild = guildRepository.findOne(guildId);
        Member newMaster = memberRepository.findOne(newMasterId);
        findGuild.changeGuildMaster(newMaster);
        em.clear();
        em.flush();
    }

    // 길드 레벨 수정
    @Transactional
    public void updateGuildLevel(Long guildId, Integer newLevel) {
        Guild findGuild = guildRepository.findOne(guildId);
        findGuild.updateGuildLevel(newLevel);
        em.clear();
        em.flush();
    }

    // 길드 캐릭터 가입
    public void joinNewCharacter(Long guildId, Long characterId) {
        Characters newChar = characterRepository.findOne(characterId);
        Member owner = newChar.getCharacterOwner();
        Guild guild = guildRepository.findOne(guildId);
        Community gCom = guild.getJoinedCommunity();
        GuildMember gm = memberRepository.findByGuildMember(guild.getName(), owner.getNickname()).get(0);

        // 해당 길드에 특정 멤버가 최초로 가입하는 경우,
        if(gm==null || !guild.getGuildMembers().contains(gm)) {
            guild.joinGuildMember(owner);
            owner.addJoinedGuild(gm);
        }

        // add character on Guild
        guild.joinGuildCharacter(newChar);
        newChar.joinNewGuild(guild);
        if(gCom != null) {
            newChar.joinNewCommunity(gCom);
        }

        // add char's owner Member on guild member list
        if(memberRepository.findByGuildMember(guild.getName(), newChar.getCharacterOwner().getNickname()).isEmpty()) {
            guild.joinGuildMember(owner);
        }
    }

    // 길드 캐릭터 탈퇴
    public void leaveCharacter(Long guildId, Long characterId) {
        Guild findGuild = guildRepository.findOne(guildId);
        Characters findChar = characterRepository.findOne(characterId);
        Member owner = findChar.getCharacterOwner();
        Community joinedCommunity = findChar.getJoinedCommunity();
        GuildMember gm = memberRepository.findByGuildMember(findGuild.getName(), owner.getNickname()).get(0);

        // 해당 캐릭터가 탈퇴하면 소속 캐릭터의 수가 0인경우, 길드 멤버리스트에서 해당 멤버를 제거
        if(characterRepository.findCharactersByMemberWithGuild(findGuild.getName(), owner.getNickname()).size() <= 1) {
            owner.leaveJoinedGuild(findGuild);
            findGuild.leaveGuildByMember(gm);
        }

        if(findChar.getJoinedGuild()!=null) {
            findChar.leaveJoinedGuild();
        }

        if(joinedCommunity != null) {
            joinedCommunity.getJoinedCharacters().remove(findChar);
            findChar.leaveJoinedCommunity();
        }

    }



    // 길드 멤버 가입



    // 길드멤버 길드 탈퇴
    @Transactional
    public void leaveGuildMember(Long guildId, Long memberId) {
        Guild guild = guildRepository.findOne(guildId);
        Member member = memberRepository.findOne(memberId);

        if (guild!=null) {
            List<Characters> charList = characterService.findCharactersByMemberWithGuild(guild.getName(), member.getNickname());
            for(Characters c : charList) {
                if(guild.getGuildCharacters().contains(c)) {
                    guild.getGuildCharacters().remove(c);
                    c.leaveJoinedGuild();
                } else {
                    log.info("{} doesn't have character name {}.", guild.getName(), c.getName());
                }
            }
            if(guild.getGuildMembers().contains(member)) {
                guild.getGuildMembers().remove(member);
                member.leaveJoinedGuild(guild);
            } else {
                log.info("{} doesn't have member nickname {}.", guild.getName(), member.getNickname());
            }

        } else {
            throw new ResourceNotFoundException("Guild", "id", guildId);
        }
    }


        /* 길드 조회 */

    // 길드 ID로 길드 단건 조회
    @Transactional(readOnly = true)
    public Guild findById(Long id) { return guildRepository.findOne(id); }

    // 길드 이름 조회
    @Transactional(readOnly = true)
    public List<Guild> findGuildByName(String guildName) {
        return guildRepository.findByGuildName(guildName);
    }

    // 특정 커뮤니티 소속 길드 조회
    @Transactional(readOnly = true)
    public List<Guild> findGuildByJoinedCommunity(String comName) {
        return guildRepository.findByJoinedCommunityName(comName);
    }



    // 길드 이름 중복 조회
    @Transactional(readOnly = true)
    public void validateDuplicateGuild(Guild guild) {
        List<Guild> findGuild = guildRepository.findByGuildName(guild.getName());
        if(!findGuild.isEmpty()) {
            throw new IllegalStateException("중복된 길드명");
        }
    }
}
