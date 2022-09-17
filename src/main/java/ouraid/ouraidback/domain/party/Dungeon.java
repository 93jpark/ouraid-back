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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static ouraid.ouraidback.domain.enums.PartyStatus.RECRUIT;
import static ouraid.ouraidback.domain.enums.PartyType.NORMAL;

@Entity
@DiscriminatorValue("Dungeon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Dungeon extends Party {

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
    public Party createDungeonParty(RecruitType recruitType,
                                    Server server,
                                    Member partyHolderMember,
                                    Characters partyHolderCharacter,
                                    LocalDateTime reservedTime)
    {
        Dungeon party = new Dungeon();
        party.recruitType = recruitType;
        party.partyStatus = RECRUIT; // default party status
        party.partyType = NORMAL; // default party type
        party.server = server;
        party.partyHolderMember = partyHolderMember;
        party.partyHolderCharacter = partyHolderCharacter;
        party.reservedTime = reservedTime;
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
    public static Party createDungeonParty(
            RecruitType recruitType,
            PartyType partyType,
            Server server,
            Member partyHolderMember,
            Characters partyHolderCharacter,
            int freeRiderCapacity,
            double minAbility,
            LocalDateTime reservedTime)
    {
        Dungeon party = new Dungeon();
        party.recruitType = recruitType;
        party.partyStatus = RECRUIT;
        party.partyType = partyType;
        party.server = server;
        party.partyHolderMember = partyHolderMember;
        party.partyHolderCharacter = partyHolderCharacter;
        party.freeRiderCapacity = freeRiderCapacity;
        party.minAbility = BigDecimal.valueOf(minAbility);
        party.reservedTime = reservedTime;
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
    public void updateReservedTime(LocalDateTime time) {
        this.reservedTime = time;
    }

    // 파티 참가원 추가
    public void addPartyCharacter(PartyParticipant pp) {
        this.getPartyParticipants().add(pp);
        this.registeredMemberSize++;
    }
}
