package ouraid.ouraidback.domain.party;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Dungeon")
@Getter
public class Dungeon extends Party {
    private int partyCapacity = 3;
}
