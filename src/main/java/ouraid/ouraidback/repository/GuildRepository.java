package ouraid.ouraidback.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Guild;
import ouraid.ouraidback.domain.GuildMember;
import ouraid.ouraidback.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GuildRepository {

    private final EntityManager em;

    public Long registerGuild(Guild guild) {
        em.persist(guild);
        return guild.getId();
    }

    public Long registerGuildMember(GuildMember gm) {
        em.persist(gm);
        return gm.getGuildMemberId();
    }

    public void removeGuild(Guild guild) {
        em.remove(guild);
    }

    public void removeGuildMember(GuildMember gm) {
        em.remove(gm);
    }

    // 식별자를 통한 단일 길드 조회
    public Guild findGuild(Long id) { return em.find(Guild.class, id); }

    // 식별자를 통한 단일 길드-멤버 조회
    public GuildMember findGuildMember(Long id) { return em.find(GuildMember.class, id); }

    // 모든 길드 조회
    public List<Guild> findAll() {
        return em.createQuery("select g from Guild g", Guild.class)
                .getResultList();
    }

    // 길드명에 따른 길드 조회
    public List<Guild> findByGuildName(String gName) {
        return em.createQuery("select g from Guild g where g.name = :gName", Guild.class)
                .setParameter("gName", gName)
                .getResultList();
    }

    // 소속 연합명에 따른 길드 조회
    public List<Guild> findByJoinedCommunityName(String cName) {
        return em.createQuery("select g from Guild g where g.joinedCommunity.name = :cName", Guild.class)
                .setParameter("cName", cName)
                .getResultList();
    }

    // 서버명에 따른 길드 조회
    public List<Guild> findByServer(String sName) {
        return em.createQuery("select g from Guild g where g.server = :sName", Guild.class)
                .setParameter("sName", sName)
                .getResultList();
    }

    // 특정길드의 특정멤버 조회
    public List<GuildMember> findByGuildMember(String gName, String mName) {
        return em.createQuery("select gm from GuildMember gm where gm.member.nickname = :mName and gm.guild.name = :gName", GuildMember.class)
                .setParameter("gName", gName)
                .setParameter("mName", mName)
                .getResultList();
    }

    // 길드멤버
    public List<GuildMember> findGuildMember(Long gId, Long mId) {
        return em.createQuery("select gm from GuildMember gm where gm.guild.id = :gId and gm.member.id = :mId", GuildMember.class)
                .setParameter("gId", gId)
                .setParameter("mId", mId)
                .getResultList();
    }

    // 길드와 멤버 id 기반 GuildMember 검색
    @Transactional(readOnly = true)
    public List<GuildMember> findGuildMemberByGuildMember(Long gId, Long mId) {
        return em.createQuery("select gm from GuildMember gm where gm.guild.id = :gId and gm.member.id = :mId", GuildMember.class)
                .setParameter("gId", gId)
                .setParameter("mId", mId)
                .getResultList();
    }

    // 길드 소유 멤버의 id기반 길드 조회
    @Transactional(readOnly = true)
    public List<Guild> findGuildbyOwner(Long mId) {
        return em.createQuery("select g from Guild g where g.guildMaster.id = :mId", Guild.class)
                .setParameter("mId", mId)
                .getResultList();
    }


}
