package ouraid.ouraidback.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ouraid.ouraidback.domain.Characters;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CharacterRepository {

    private final EntityManager em;

    public Long register(Characters character) {
        em.persist(character);
        return character.getId();
    }

    // 단일 캐릭터 조회
    public Characters findOne(Long id) {
        return em.find(Characters.class, id);
    }

    // 등록된 모든 캐릭터 조회
    public List<Characters> findAll() {
        return em.createQuery("select c from Characters c", Characters.class)
                .getResultList();
    }

    // 캐릭명으로 단일 캐릭터 조회
    public Characters findByCharName(String name) {
        return em.createQuery("select c from Characters c where c.name = :name", Characters.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    // 서버이름 기반 캐릭터 조회
    public List<Characters> findByServer(String serverName) {
        return em.createQuery("select c from Characters c where c.server = :serverName", Characters.class)
                .setParameter("serverName", serverName)
                .getResultList();
    }

    // 소속 길드 기반 캐릭터 조회
    public List<Characters> findByGuild(String guildName) {
        return em.createQuery("select c from Characters c where c.joinedGuild = :guildName", Characters.class)
                .setParameter("guildName", guildName)
                .getResultList();
    }

    // 소속 연합 기반 캐릭터 조회
    public List<Characters> findByCommunity(String cName) {
        return em.createQuery("select c from Characters c where c.joinedCommunity = :cName", Characters.class)
                .setParameter("cName", cName)
                .getResultList();
    }

    // 특정 메인직업 기반 캐릭터 조회
    public List<Characters> findByMainClass(String mainClass) {
        return em.createQuery("select c from Characters c where c.mainClass = :cName", Characters.class)
                .setParameter("cName", mainClass)
                .getResultList();
    }

    // 특정 서브직업 기반 캐릭터 조회
    public List<Characters> findBySubClass(String subClass) {
        return em.createQuery("select c from Characters c where c.subClass = :sName", Characters.class)
                .setParameter("sName", subClass)
                .getResultList();
    }

    // 특정 항마 이상 모두 조회
    public List<Characters> findByAbility(Double ab) {
        return em.createQuery("select c from Characters c where c.ability >= :ab", Characters.class)
                .setParameter("ab", ab)
                .getResultList();
    }


}
