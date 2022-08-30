package ouraid.ouraidback.domain.party;

import com.sun.istack.NotNull;
import lombok.Getter;
import org.springframework.lang.Nullable;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@DiscriminatorColumn(name = "ptype")
//@MappedSuperclass
public class Party {
    @Id @GeneratedValue @Column(name="party_id") private Long id;

    @Enumerated(EnumType.STRING) @NotNull
    protected Server server;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "member_id") @NotNull
    protected Member creatorMember;

    @NotNull private int partyCapacity;

    @NotNull
    protected Boolean acceptFreeRider = false;

    @NotNull
    protected int freeRiderCapacity = 0;

    @Nullable private double minAbility;

    @NotNull private Boolean fulfillment = false;

    @OneToMany(mappedBy="joinedMember") private List<PartyMember> joinedMembers = new ArrayList<>();

}
