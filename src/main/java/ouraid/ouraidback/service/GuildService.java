package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.*;
import ouraid.ouraidback.Exception.ResourceNotFoundException;
import ouraid.ouraidback.repository.CharacterRepository;
import ouraid.ouraidback.repository.GuildRepository;
import ouraid.ouraidback.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
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
        GuildMember.createGuildMember(guild, guild.getGuildMaster());
        return guildRepository.registerGuild(guild);
    }

    // 길드 멤버 관계 생성
    @Transactional
    public Long registerGuildMember(Long gId, Long mId) {
        Guild findGuild = guildRepository.findGuild(gId);
        return guildRepository.findGuildMember(gId, mId).get(0).getGuildMemberId();
    }

    // 길드 정보 업데이트 - 이름, 마스터, 레벨
    @Transactional
    public void changeGuildName(Long guildId, String newName) throws Exception {
        /**
         * 중복검사필요
         */
        Guild findGuild = guildRepository.findGuild(guildId);
        try {
            validateDuplicateGuild(findGuild);
        } catch (Exception e) {
            throw new Exception("Tried to chage guild name, but "+newName+" is already registered");
        }
        findGuild.changeGuildName(newName);


    }

    @Transactional
    public void changeGuildMaster(Long guildId, Long newMasterId) {
        Guild findGuild = guildRepository.findGuild(guildId);
        Member newMaster = memberRepository.findMember(newMasterId);
        findGuild.changeGuildMaster(newMaster);
        em.clear();
        em.flush();
    }

    // 길드 레벨 수정
    @Transactional
    public void updateGuildLevel(Long guildId, Integer newLevel) {
        Guild findGuild = guildRepository.findGuild(guildId);
        findGuild.updateGuildLevel(newLevel);
        em.clear();
        em.flush();
    }

    // 길드 캐릭터 가입
    public void joinNewCharacter(Long guildId, Long characterId) {
        Characters newChar = characterRepository.findOne(characterId);
        Member owner = newChar.getCharacterOwner();
        Guild guild = guildRepository.findGuild(guildId);
        Community gCom = guild.getJoinedCommunity();
        GuildMember gm = guildRepository.findByGuildMember(guild.getName(), owner.getNickname()).get(0);

        // 해당 길드에 특정 멤버가 최초로 가입하는 경우,
        if(gm==null) {
            //guild.joinGuildMember(owner);
            gm.setJoinedDate(Instant.now());
            owner.addJoinedGuild(gm);
        }

        // add character on Guild
        guild.joinGuildCharacter(newChar);
        newChar.joinNewGuild(guild);
        if(gCom != null) {
            newChar.joinNewCommunity(gCom);
        }

        // add char's owner Member on guild member list
        if(guildRepository.findByGuildMember(guild.getName(), newChar.getCharacterOwner().getNickname()).isEmpty()) {
            //guild.joinGuildMember(owner);
        }
    }

    // 길드 캐릭터 탈퇴
    public void leaveCharacter(Long guildId, Long characterId) {
        Guild findGuild = guildRepository.findGuild(guildId);
        Characters findChar = characterRepository.findOne(characterId);
        Member owner = findChar.getCharacterOwner();
        Community joinedCommunity = findChar.getJoinedCommunity();
        GuildMember gm = guildRepository.findByGuildMember(findGuild.getName(), owner.getNickname()).get(0);

        // 해당 캐릭터가 탈퇴하면 소속 캐릭터의 수가 0인경우, 길드 멤버리스트에서 해당 멤버를 제거
        if(characterRepository.findCharactersByMemberWithGuild(findGuild.getName(), owner.getNickname()).size() <= 1) {
            owner.leaveJoinedGuild(gm);
            em.remove(gm);
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
    @Transactional
    public void joinGuildMember(Long guildId, Long memberId) {
        Guild guild = guildRepository.findGuild(guildId);
        Member member = memberRepository.findMember(memberId);

        GuildMember gm = GuildMember.createGuildMember(guild, member);

        if(guildRepository.findGuildMember(guildId, memberId).isEmpty()) {
            gm.setJoinedDate(Instant.now());
            member.addJoinedGuild(gm);
        }
    }


    // 길드멤버 길드 탈퇴
    @Transactional
    public void leaveGuildMember(Long guildId, Long memberId) {
        Guild guild = guildRepository.findGuild(guildId);
        Member member = memberRepository.findMember(memberId);
        GuildMember gm = guildRepository.findGuildMember(guildId, memberId).get(0);

        if (guild!=null) {
            List<Characters> charList = characterService.findCharactersByMemberWithGuild(guild.getName(), member.getNickname());
            log.info("'{}' guild's {} characters found who owned by {}", guild.getName(), charList.size(), member.getNickname());
            // 멤버의 캐릭터들 모두 길드 탈퇴
            for(Characters c : charList) {
                if(guild.getGuildCharacters().contains(c)) {
                    //guild.getGuildCharacters().remove(c);
                    log.info("'{}' leave '{}' guild", c.getName(), guild.getName());
                    c.leaveJoinedGuild();
                } else {
                    log.info("'{}' guild doesn't have '{}' character.", guild.getName(), c.getName());
                }
            }
            if(gm!=null) {
                member.leaveJoinedGuild(gm);
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
    public Guild findById(Long id) { return guildRepository.findGuild(id); }

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

    // 길드와 멤버 id 기반 GuildMember 검색
    @Transactional(readOnly = true)
    public List<GuildMember> findGuildMemberByGuildMember(Long gId, Long mId) {
        return em.createQuery("select gm from GuildMember gm where gm.guild.id = :gId and gm.member.id = :mId", GuildMember.class)
                .setParameter("gId", gId)
                .setParameter("mId", mId)
                .getResultList();
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
