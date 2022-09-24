package ouraid.ouraidback.domain.party;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.PartyStatus;
import ouraid.ouraidback.domain.enums.PartyType;
import ouraid.ouraidback.domain.enums.RecruitType;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Instant;
import java.util.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter @Slf4j
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
    @NotNull protected int acceptedMemberSize = 0;

    // 파티 현 업둥 인원
    @Nullable protected int acceptedRiderSize;

    // 업둥이 정원
    @NotNull protected int freeRiderCapacity;

    // 최소 항마컷
    @Nullable protected BigDecimal minAbility;

    @NotNull Instant reservedTime;

    @NotNull Instant createdTime;

    @ManyToOne(fetch = LAZY, cascade = PERSIST) @JoinColumn(name = "member_id") @NotNull
    protected Member partyHolderMember;

    @ManyToOne(fetch = LAZY, cascade = PERSIST) @JoinColumn(name = "character_id") @NotNull
    protected Characters partyHolderCharacter;

    @OneToMany(mappedBy="joinedPartyCharacter", cascade = ALL)
    private List<PartyParticipant> partyParticipants = new ArrayList<>();

    /* 파티 정보 수정 */

    // 업둥이 정원 수정
    public void designateFreeRiderCapacity(int capacity) {
        this.freeRiderCapacity = capacity;
    }

    // 파티 타입 수정
    public void updatePartyType(PartyType type) {
        this.partyType = type;
    }

    // 파티 상태 수정
    public void updatePartyStatus(PartyStatus status) { this.partyStatus = status; }

    // 모집타입 수정
    public void updateRecruitType(RecruitType type) {
        this.recruitType = type;
    }

    // 최소항마컷 수정
    public void updateMinAbility(double ability) {
        this.minAbility = BigDecimal.valueOf(ability);
    }

    // 파티예정시각 수정
    public void updateReservedTime(Instant time) {
        this.reservedTime = time;
    }

    // 파티 참가원 추가
    public void addPartyParticiapnt(PartyParticipant pp) {
        this.getPartyParticipants().add(pp);
    }

    // 파티 참가원 삭제
    public void removePartyParticipant(PartyParticipant pp) { this.getPartyParticipants().remove(pp); }

    // 파티 참가원 승인
    public void acceptParticipant() {
        this.acceptedMemberSize++;
    }

    // 파티 참가원 추방
    public void repelAcceptedParticipant() {
        this.acceptedMemberSize--;
    }

    // 파티원 중 라이더 추가
    public void incRiderCharacterSize() {
        this.acceptedRiderSize++;
    }

    // 파티원 중 라이더 추방
    public void decRiderCharacterSize() {
        if(this.acceptedRiderSize>1) {
            this.acceptedRiderSize--;
        }
    }


}
