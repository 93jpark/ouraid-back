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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
            LocalDate reservedTime)
    {
        HardLotus party = new HardLotus();
        party.recruitType = recruitType;
        party.partyStatus = RECRUIT; // default party status
        party.partyType = NORMAL; // default party type
        party.server = server;
        party.partyHolderMember = partyHolderMember;
        party.partyHolderCharacter = partyHolderCharacter;
        party.reservedTime = reservedTime;
        party.createdTime = LocalDate.now();
        party.minAbility = BigDecimal.ZERO;
        party.freeRiderCapacity = 0;
        return party;
    }

    /**
     * If partyType is specified, require param for freeRiderCapacity, minAbility;
     * @param recruitType
     * @param server
     * @param partyHolderMember
     * @param partyHolderCharacter
     * @param reservedTime
     * @param partyType
     * @param freeRiderCapacity
     * @param minAbility
     * @return
     */
    public static HardLotus createHardLotusParty(
            RecruitType recruitType,
            Server server,
            Member partyHolderMember,
            Characters partyHolderCharacter,
            LocalDate reservedTime,
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
        party.acceptedRiderSize = 0;
        party.minAbility = BigDecimal.valueOf(minAbility);
        party.reservedTime = reservedTime;
        party.createdTime = LocalDate.now();

        return party;
    }

}
