package ouraid.ouraidback.domain.party;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Dungeon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Dungeon extends Party {
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
