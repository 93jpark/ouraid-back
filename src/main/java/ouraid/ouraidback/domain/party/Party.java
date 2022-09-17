package ouraid.ouraidback.domain.party;

import lombok.Getter;
import org.springframework.lang.Nullable;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.PartyStatus;
import ouraid.ouraidback.domain.enums.PartyType;
import ouraid.ouraidback.domain.enums.RecruitType;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static javax.persistence.CascadeType.ALL;
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

    // 파티 타입
    @Enumerated(EnumType.STRING) @NotNull
    protected PartyType partyType;

    // 모집 타입
    @Enumerated(EnumType.STRING) @NotNull
    protected RecruitType recruitType;

    // 파티 상태
    @Enumerated(EnumType.STRING) @NotNull
    protected PartyStatus partyStatus;

    // 파티 정원
    @NotNull protected int partyCapacity;

    // 파티 현 인원
    @NotNull protected int registeredMemberSize = 1;

    // 업둥이 정원
    @NotNull protected int freeRiderCapacity;

    // 최소 항마컷
    @Nullable protected BigDecimal minAbility;

    @NotNull LocalDateTime reservedTime;

    @ManyToOne(fetch = LAZY, cascade = ALL) @JoinColumn(name = "member_id") @NotNull
    protected Member partyHolderMember;

    @ManyToOne(fetch = LAZY, cascade = ALL) @JoinColumn(name = "character_id") @NotNull
    protected Characters partyHolderCharacter;

    @OneToMany(mappedBy="joinedPartyCharacter", cascade = ALL)
    private List<PartyParticipant> partyParticipants = new ArrayList<>();

}
