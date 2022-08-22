package ouraid.ouraidback.domain.party;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@DiscriminatorValue("WorldBoss")
public class WorldBoss extends Party {
    @Id @GeneratedValue
    private Long id;

    private int partyCapacity = 3;
}
