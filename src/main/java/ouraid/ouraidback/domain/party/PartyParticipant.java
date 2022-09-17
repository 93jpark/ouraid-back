package ouraid.ouraidback.domain.party;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.ParticipantType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PartyParticipant {
    @Id @GeneratedValue
    @Column(name="party_participant_id")
    private Long id;

    @ManyToOne @JoinColumn(name = "party_id") @NotNull
    private Party joinedParty;

    @ManyToOne @JoinColumn(name="member_id") @NotNull
    private Member joinedPartyMember;

    @ManyToOne @JoinColumn(name = "character_id") @NotNull
    private Characters joinedPartyCharacter;

    @Enumerated(EnumType.STRING) @NotNull //
    private ParticipantType participantType; // DRIVER, RIDER, NORMAL

    public static PartyParticipant createPartyParticipant(Party joinedParty, Member partyMember, Characters character) {
        PartyParticipant pp = new PartyParticipant();
        pp.joinedParty = joinedParty;
        pp.joinedPartyMember = partyMember;
        pp.joinedPartyCharacter = character;
        return pp;
    }

    public static PartyParticipant createPartyParticipant(Party joinedParty, Member partyMember, Characters character, ParticipantType type) {
        PartyParticipant pp = new PartyParticipant();
        pp.joinedParty = joinedParty;
        pp.joinedPartyMember = partyMember;
        pp.joinedPartyCharacter = character;
        pp.participantType = type;
        return pp;
    }

    // 파티원 타입 지정
    public void designateType(ParticipantType type) {
        this.participantType = type;
    }


}
