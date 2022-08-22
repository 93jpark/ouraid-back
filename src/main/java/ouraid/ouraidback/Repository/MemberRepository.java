package ouraid.ouraidback.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ouraid.ouraidback.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    // @PersistenceContext
    private final EntityManager em;
    // 생성자 인젝션을 한 경우. 생성자는 생략되며 Spring에서 제공해준다.
    // 또한, PersistenceContext는 JPA의 기능인데, 스프링에서 처리해준다.

    public void register(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByNickname(String nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .setParameter("nickname", nickname)
                .getResultList();
    }

    public List<Member> findByServer(String serverName) {
        return em.createQuery("select m from Member m where m.server = :serverName", Member.class)
                .setParameter("serverName", serverName)
                .getResultList();
    }
}
