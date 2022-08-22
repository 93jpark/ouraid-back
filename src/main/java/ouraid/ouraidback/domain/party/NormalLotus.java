package ouraid.ouraidback.domain.party;

import lombok.Getter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("N_Lotus")
@Getter
public class NormalLotus extends Party {
    @Id @GeneratedValue
    private Long id;

    private int partyCapacity = 6;
}
