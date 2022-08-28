package ouraid.ouraidback.domain.party;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@DiscriminatorValue("H_Lotus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HardLotus extends Party {
    @Id @GeneratedValue
    private Long id;

    private int partyCapacity = 3;

    public Party createParty(Server server, Member partyCreator, Boolean acceptFreeRider, int freeRiderCapacity) {
        NormalLotus party = new NormalLotus();
        party.server = server;
        party.creatorMember = creatorMember;
        party.acceptFreeRider = acceptFreeRider;
        party.freeRiderCapacity = freeRiderCapacity;
        return party;
    }
}
