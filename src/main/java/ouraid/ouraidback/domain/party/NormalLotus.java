package ouraid.ouraidback.domain.party;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.PartyType;
import ouraid.ouraidback.domain.enums.RecruitType;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

    public static NormalLotus createNormalLotusParty(
            RecruitType recruitType,
            Server server,
            Member partyHolderMember,
            Characters partyHolderCharacter,
            LocalDate reservedTime)
    {
        NormalLotus party = new NormalLotus();
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
     * @param partyType
     * @param server
     * @param partyHolderMember
     * @param partyHolderCharacter
     * @param freeRiderCapacity
     * @param minAbility
     * @param reservedTime
     * @return
     */
    public static NormalLotus createNormalLotusParty(
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
        NormalLotus party = new NormalLotus();
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
