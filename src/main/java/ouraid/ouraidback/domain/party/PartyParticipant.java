package ouraid.ouraidback.domain.party;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.Characters;
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

    @ManyToOne
    @JoinColumn(name = "character_id")
    private Characters joinedCharacter;

    public PartyMember(Party joinedParty, Member joinedMember) {
        this.joinedParty = joinedParty;
        this.joinedMember = joinedMember;
    }
}
