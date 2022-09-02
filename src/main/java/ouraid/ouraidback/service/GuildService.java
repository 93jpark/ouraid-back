package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Guild;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.repository.CharacterRepository;
import ouraid.ouraidback.repository.GuildRepository;
import ouraid.ouraidback.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuildService {

    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;
    private final GuildRepository guildRepository;

    @PersistenceContext
    EntityManager em;

    // 길드 생성
    @Transactional
    public Long registerGuild(Guild guild) {
        validateDuplicateGuild(guild);
        return guildRepository.register(guild);
    }

    // 길드 정보 업데이트 - 마스터, 레벨
    @Transactional
    public void changeGuildMaster(Long guildId, Member newMaster) {
        Guild findGuild = guildRepository.findOne(guildId);
        findGuild.changeGuildMaster(newMaster);
        em.clear();
        em.flush();
    }

    @Transactional
    public void updateGuildLevel(Long guildId, Integer newLevel) {
        Guild findGuild = guildRepository.findOne(guildId);
        findGuild.updateGuildLevel(newLevel);
        em.clear();
        em.flush();
    }

    // 길드원 탈퇴
    @Transactional
    public void leaveGuildMember(Long guildId, Member member) {

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
