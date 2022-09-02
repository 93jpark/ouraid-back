package ouraid.ouraidback.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ouraid.ouraidback.domain.Guild;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GuildRepository {

    private final EntityManager em;

    public Long register(Guild guild) {
        em.persist(guild);
        return guild.getId();
    }

    // 식별자를 통한 단일 길드 조회
    public Guild findOne(Long id) { return em.find(Guild.class, id); }

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

}
