package ouraid.ouraidback.domain.party;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.Server;

import javax.persistence.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "ptype")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Party {
    @Id
    @GeneratedValue
    @Column(name="party_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Server server;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @NotNull
    private Member creatorMember;

    @NotNull
    private int partyCapacity;

    @NotNull
    private Boolean acceptFreeRider = false;

    @NotNull
    private int freeRiderCapacity = 0;

    @Nullable
    private double minAbility;

    @NotNull
    private Boolean fulfillment = false;

    @OneToMany(mappedBy="joinedMember")
    private List<PartyMember> joinedMembers = new ArrayList<>();

    public Party(Server server, Member creatorMember, int partyCapacity) {
        this.server = server;
        this.creatorMember = creatorMember;
        this.partyCapacity = partyCapacity;
    }

    public Party(Server server, Member creatorMember, int partyCapacity, Boolean acceptFreeRider, int freeRiderCapacity) {
        this.server = server;
        this.creatorMember = creatorMember;
        this.partyCapacity = partyCapacity;
        this.acceptFreeRider = acceptFreeRider;
        this.freeRiderCapacity = freeRiderCapacity;
    }

    // GETTER
    public Long getId() {
        return id;
    }

    public Server getServer() {
        return server;
    }

    public Member getCreatorMember() {
        return creatorMember;
    }

    public int getPartyCapacity() {
        return partyCapacity;
    }

    public Boolean getAcceptFreeRider() {
        return acceptFreeRider;
    }

    public int getFreeRiderCapacity() {
        return freeRiderCapacity;
    }

    public void setMinAbility(double minAbility) {
        this.minAbility = minAbility;
    }
}
