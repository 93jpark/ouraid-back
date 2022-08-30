package ouraid.ouraidback.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ouraid.ouraidback.domain.Community;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommunityRepository {

    private final EntityManager em;

    public Long register(Community community) {
        em.persist(community);
        return community.getId();
    }

    // 식별자를 통한 단일 커뮤니티 조회
    public Community findOne(Long id) { return em.find(Community.class, id); }

    // 등록된 모든 커뮤니티 조회
    public List<Community> findAll() {
        return em.createQuery("select c from Community c", Community.class)
                .getResultList();
    }

    // 커뮤니티 명에 따른 커뮤니티 조회
    public Community findByComName(String cName) {
        return em.createQuery("select c from Community c where c.name = :cName", Community.class)
                .setParameter("cName", cName)
                .getSingleResult();
    }

    // 서버 명에 따른 커뮤니티 조회
    public List<Community> findByServer(String sName) {
        return em.createQuery("select c from Community c where c.server = :sName", Community.class)
                .setParameter("sName", sName)
                .getResultList();
    }

    // 커뮤니티 주인멤버의 이름에 따른 커뮤니티 조회
    public Community findByComOwner(String memName) {
        return em.createQuery("select c from Community c " +
                "where c.communityMaster.nickname = :memName", Community.class)
                .getSingleResult();
    }



}
