package ouraid.ouraidback.domain.party;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.PartyType;
import ouraid.ouraidback.domain.enums.RecruitType;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Instant;

import static ouraid.ouraidback.domain.enums.PartyStatus.RECRUIT;
import static ouraid.ouraidback.domain.enums.PartyType.NORMAL;

@Entity
@DiscriminatorValue("H_Lotus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HardLotus extends Party {
    @Id @GeneratedValue
    private Long id;

    private int partyCapacity = 3;

    /**
     * Default party type is normal
     * @param recruitType
     * @param server
     * @param partyHolderMember
     * @param partyHolderCharacter
     * @param reservedTime
     * @return
     */
    public static HardLotus createHardLotusParty(
            RecruitType recruitType,
            Server server,
            Member partyHolderMember,
            Characters partyHolderCharacter,
            Instant reservedTime)
    {
        HardLotus party = new HardLotus();
        party.recruitType = recruitType;
        party.partyStatus = RECRUIT; // default party status
        party.partyType = NORMAL; // default party type
        party.server = server;
        party.partyHolderMember = partyHolderMember;
        party.partyHolderCharacter = partyHolderCharacter;
        party.reservedTime = reservedTime;
        party.createdTime = Instant.now();
        party.minAbility = BigDecimal.ZERO;
        party.freeRiderCapacity = 0;
        return party;
    }

    /**
     * If partyType is specified, require param for freeRiderCapacity, minAbility;
     * @param recruitType
     * @param partyType
     * @param server
     * @param partyHolderMember
     * @param partyHolderCharacter
     * @param freeRiderCapacity
     * @param minAbility
     * @param reservedTime
     * @return
     */
    public static HardLotus createHardLotusParty(
            RecruitType recruitType,
            Server server,
            Member partyHolderMember,
            Characters partyHolderCharacter,
            Instant reservedTime,
            PartyType partyType,
            int freeRiderCapacity,
            double minAbility
            )
    {
        HardLotus party = new HardLotus();
        party.recruitType = recruitType;
        party.partyStatus = RECRUIT;
        party.partyType = partyType;
        party.server = server;
        party.partyHolderMember = partyHolderMember;
        party.partyHolderCharacter = partyHolderCharacter;
        party.freeRiderCapacity = freeRiderCapacity;
        party.minAbility = BigDecimal.valueOf(minAbility);
        party.reservedTime = reservedTime;
        party.createdTime = Instant.now();

        return party;
    }


    // 업둥이 정원 수정
    public void designateFreeRiderCapacity(int capacity) {
        this.freeRiderCapacity = capacity;
    }

    // 파티타입 수정
    public void updatePartyType(PartyType type) {
        this.partyType = type;
    }

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
    public void addPartyCharacter(PartyParticipant pp) {
        this.getPartyParticipants().add(pp);
    }

    // 파티 참가원 승인
    public void acceptParticipant() {
        this.acceptedMemberSize++;
    }

    // 파티 참가원 추방
    public void repelAcceptedMember() {
        this.acceptedMemberSize--;
    }

}
