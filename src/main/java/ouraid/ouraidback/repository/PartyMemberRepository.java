package ouraid.ouraidback.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.party.Party;
import ouraid.ouraidback.domain.party.PartyMember;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class PartyMemberRepository {

    private final EntityManager em;

    // 기존 파티 가입
    @Transactional
    public void create(Party party, Member member) {
        PartyMember partyMember = PartyMember.createPartyMember(party,member);
        em.persist(partyMember);
    }

    // 기존 파티 탈퇴




}
