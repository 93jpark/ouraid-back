package ouraid.ouraidback.domain.party;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.PartyStatus;
import ouraid.ouraidback.domain.enums.PartyType;
import ouraid.ouraidback.domain.enums.RecruitType;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Instant;

import static ouraid.ouraidback.domain.enums.PartyStatus.RECRUIT;
import static ouraid.ouraidback.domain.enums.PartyType.NORMAL;

@Entity
@DiscriminatorValue("N_Lotus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NormalLotus extends Party {
    @Id @GeneratedValue
    private Long id;

    private int partyCapacity = 6;

    /**
     * Default party type is normal
     * @param recruitType
     * @param server
     * @param partyHolderMember
     * @param partyHolderCharacter
     * @param reservedTime
     * @return
     */
    public static Party createNormalLotusParty(RecruitType recruitType,
                                    Server server,
                                    Member partyHolderMember,
                                    Characters partyHolderCharacter,
                                    Instant reservedTime)
    {
        NormalLotus party = new NormalLotus();
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
    public static Party createNormalLotusParty(
            RecruitType recruitType,
            PartyType partyType,
            Server server,
            Member partyHolderMember,
            Characters partyHolderCharacter,
            int freeRiderCapacity,
            double minAbility,
            Instant reservedTime)
    {
        NormalLotus party = new NormalLotus();
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
        this.registeredMemberSize++;
    }

}
