package ouraid.ouraidback.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ouraid.ouraidback.domain.enums.ParticipantStatus;
import ouraid.ouraidback.domain.enums.ParticipantType;
import ouraid.ouraidback.domain.enums.PartyStatus;
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
    public Party findOneParty(Long id) { return em.find(Party.class, id); }

    // 파티원 단일 검색
    public PartyParticipant findOneParticipant(Long id) { return em.find(PartyParticipant.class, id); }


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

    // find PartyParticipant by Party/Member id
    public List<PartyParticipant> findPartyParticipantByMember(Long pId, Long mId) {
        return em.createQuery("select pp from PartyParticipant pp where pp.joinedParty.id = :pId and pp.joinedPartyCharacter.characterOwner.id = :mId", PartyParticipant.class)
                .setParameter("pId", pId)
                .setParameter("mId", mId)
                .getResultList();
    }

    // find all participant of specific party
    public List<PartyParticipant> findAllParticipant(Long pId) {
        return em.createQuery("select pp from PartyParticipant pp where pp.joinedParty.id = :pId", PartyParticipant.class)
                .setParameter("pId", pId)
                .getResultList();
    }

    // find all party participant based on status
    public List<PartyParticipant> findPartyParticipantWithStatus(Long pId, ParticipantStatus status) {
        return em.createQuery("select pp from PartyParticipant pp where pp.joinedParty.id = :pId and pp.participantStatus = :status", PartyParticipant.class)
                .setParameter("pId", pId)
                .setParameter("status", status)
                .getResultList();
    }

    public List<PartyParticipant> findPartyParticipantWithType(Long pId, ParticipantType type) {
        return em.createQuery("select pp from PartyParticipant pp where pp.joinedParty.id = :pId and pp.participantType = :type", PartyParticipant.class)
                .setParameter("pId", pId)
                .setParameter("type", type)
                .getResultList();
    }

    // 한 파티의 등록된 멤버가 지닌 캐릭터들의 상태를 기반 검색
    // 이미 ACCEPTED 캐릭이 있는 확인하기 위함
    public List<PartyParticipant> findPartyParticipantByMemberWithStatus(Long pId, Long mId, ParticipantStatus status) {
        return em.createQuery("select pp from PartyParticipant pp where pp.joinedParty.id = :pId and pp.joinedPartyCharacter.characterOwner.id = :mId and pp.participantStatus = :status", PartyParticipant.class)
                .setParameter("pId", pId)
                .setParameter("mId", mId)
                .setParameter("status", status)
                .getResultList();
    }




    //SELECT * FROM movie.ticket t where valid_time <= SYSDATE();

//    public List<Party> findAllByDate(LocalDateTime time) {
//        return em.createQuery("select p from Party p where  ", Party.class)
//                .getResultList();
//    }
}
