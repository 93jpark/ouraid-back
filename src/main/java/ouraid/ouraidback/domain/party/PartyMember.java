package ouraid.ouraidback.domain.party;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.Member;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyMember {
    @Id @GeneratedValue
    @Column(name="party_member_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party joinedParty;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member joinedMember;

    private Boolean freeRider = false;

    private void setJoinedParty(Party party) {
        this.joinedParty = party;
    }

    private void setJoinedMember(Member member) {
        this.joinedMember = member;
    }

    public static PartyMember createPartyMember(Party party, Member member) {
        PartyMember partyMember = new PartyMember();
        partyMember.setJoinedParty(party);
        partyMember.setJoinedMember(member);
        return partyMember;
    }

    public PartyMember(Party joinedParty, Member joinedMember) {
        this.joinedParty = joinedParty;
        this.joinedMember = joinedMember;
    }
}
