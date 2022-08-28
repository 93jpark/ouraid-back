package ouraid.ouraidback.domain.party;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.*;

@Entity
@DiscriminatorValue("N_Lotus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NormalLotus extends Party {
    @Id @GeneratedValue
    private Long id;

    private int partyCapacity = 6;

    public Party createParty(Server server, Member partyCreator, int partyCapacity, Boolean acceptFreeRider, int freeRiderCapacity) {
        NormalLotus party = new NormalLotus();
        party.server = server;
        party.creatorMember = creatorMember;
        party.partyCapacity = partyCapacity;
        party.acceptFreeRider = acceptFreeRider;
        party.freeRiderCapacity = freeRiderCapacity;
        return party;
    }
}
