package ouraid.ouraidback.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ouraid.ouraidback.domain.party.*;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PartyRepository {

    private final EntityManager em;

    public Long registerParty(Party party) {
        em.persist(party);
        return party.getId();
    }

    public Long registerPartyParticipant(PartyParticipant pp) {
        em.persist(pp);
        return pp.getId();
    }

    public void removeParty(Party party) {
        log.info("id:{} created by {} has been removed.", party.getId(), party.getPartyHolderMember().getNickname());
        em.remove(party);
    }

    // 파티 단일 검색
    public Party findOne(Long id) { return em.find(Party.class, id); }

    // 전체 파티 검색
    public List<Party> findAll() {
        return em.createQuery("select p from Party p", Party.class)
                .getResultList();
    }

    // 하드로터스 전체 파티 조회
    public List<Party> findAllHardLotusParty() {
        return em.createQuery("select hl from HardLotus hl", Party.class)
                .getResultList();
    }

    // 노말로터스 전체 파티 조회
    public List<NormalLotus> findAllNormalLotusParty() {
        return em.createQuery("select nl from NormalLotus nl", NormalLotus.class)
                .getResultList();
    }

    // 월드보스 전체 파티 조회
    public List<WorldBoss> findAllWorldBossParty() {
        return em.createQuery("select wb from WorldBoss wb", WorldBoss.class)
                .getResultList();
    }

    // 던전 전체 파티 조회
    public List<Dungeon> findAllDungeonParty() {
        return em.createQuery("select d from Dungeon d", Dungeon.class)
                .getResultList();
    }

    // 커뮤니티 기반 파티 조회
    public List<Party> findAllByCommunity(String cName) {
        return em.createQuery("select p from Party p where p.partyHolderMember.joinedCommunity.name = :cName", Party.class)
                .getResultList();
    }

    // find PartyParticipant by Party/Character id
    public List<PartyParticipant> findPartyParticipant(Long pId, Long cId) {
        return em.createQuery("select pp from PartyParticipant pp where pp.joinedParty.id = :pId and pp.joinedPartyCharacter.id = :cId", PartyParticipant.class)
                .setParameter("pId", pId)
                .setParameter("cId", cId)
                .getResultList();
    }


    /*
    LocalDateTime a = LocalDateTime.of(2017, 2, 13, 15, 56);
    System.out.println(a.get(ChronoField.DAY_OF_WEEK));
    System.out.println(a.get(ChronoField.DAY_OF_YEAR));
    System.out.println(a.get(ChronoField.DAY_OF_MONTH));
    System.out.println(a.get(ChronoField.HOUR_OF_DAY));
    System.out.println(a.get(ChronoField.MINUTE_OF_DAY));
     */

    //SELECT * FROM movie.ticket t where valid_time <= SYSDATE();

//    public List<Party> findAllByDate(LocalDateTime time) {
//        return em.createQuery("select p from Party p where  ", Party.class)
//                .getResultList();
//    }
}
