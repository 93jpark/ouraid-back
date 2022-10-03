package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.*;
import ouraid.ouraidback.domain.party.Party;
import ouraid.ouraidback.domain.party.PartyParticipant;
import ouraid.ouraidback.repository.PartyRepository;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import static ouraid.ouraidback.domain.enums.ParticipantStatus.ACCEPTED;
import static ouraid.ouraidback.domain.enums.ParticipantStatus.DECLINED;
import static ouraid.ouraidback.domain.enums.ParticipantType.*;
import static ouraid.ouraidback.domain.enums.PartyStatus.*;
import static ouraid.ouraidback.domain.enums.PartyType.ASSIST;

@Service @Slf4j
@RequiredArgsConstructor
public class PartyService {

    private final PartyRepository partyRepository;
    private final CharacterService characterService;
    private final MemberService memberService;

    // 파티 생성
    @Transactional
    public void registerParty(Party party) throws Exception {
        partyRepository.registerParty(party);
        this.joinCharacterOnPartyWithType(party.getId(), party.getPartyHolderCharacter().getId(), HOLDER);
        PartyParticipant pp = partyRepository.findPartyParticipant(party.getId(), party.getPartyHolderCharacter().getId()).get(0);
        pp.updateStatus(ParticipantStatus.HOLDER);
    }

    // 업둥 파티 생성
    @Transactional
    public void registerAssistParty(Party party) throws Exception {
        partyRepository.registerParty(party);
        this.joinCharacterOnPartyWithType(party.getId(), party.getPartyHolderCharacter().getId(), DRIVER);
        PartyParticipant pp = partyRepository.findPartyParticipant(party.getId(), party.getPartyHolderCharacter().getId()).get(0);
        pp.updateStatus(ParticipantStatus.HOLDER);
    }

    // 파티 삭제
    @Transactional
    public void removeParty(Party party) {
        partyRepository.removeParty(party);
    }

    // 파티에 파티원 추가
    @Transactional
    public Long joinCharacterOnParty(Long pId, Long cId) throws Exception {
        Party findParty = partyRepository.findOneParty(pId);
        Characters findChar = characterService.findOne(cId);
        Member charOwner = findChar.getCharacterOwner();

        if(!findParty.getPartyType().equals(ASSIST)) {
            if(!validateAbility(findParty, findChar)) {
                throw new Exception("character's ability is not enough to satisfy required party minimum ability.");
            }
        }

        /** 이미 해당 캐릭터가 참여되어있는지 확인 필요 */
        if(partyRepository.findPartyParticipant(findParty.getId(), findChar.getId()).isEmpty()) {
            PartyParticipant pp = PartyParticipant.createPartyParticipant(findParty, charOwner, findChar);
            findParty.addPartyParticiapnt(pp);
            partyRepository.registerPartyParticipant(pp);
            return pp.getId();
        } else {
            throw new Exception("Duplicated character registered");
        }
    }

    // 파티 파티원 추가 + 파티원 타입
    @Transactional
    public Long joinCharacterOnPartyWithType(Long pId, Long cId, ParticipantType type) throws Exception {
        Party findParty = partyRepository.findOneParty(pId);

        Characters findChar = characterService.findOne(cId);
        Member charOwner = findChar.getCharacterOwner();

        if(type == RIDER) {
            if(!findParty.getPartyType().equals(ASSIST)) {
                throw new Exception("non-assist party cannot take rider character");
            }

            if(!validateRiderJoinable(findParty)) {
                throw new Exception("party's rider capacity is full.");
            }
        }
        if(!findParty.getPartyType().equals(ASSIST)) {
            if(!validateAbility(findParty, findChar)) {
                throw new Exception("character's ability is not enough to satisfy required party minimum ability.");
            }
        }

        /** 이미 해당 멤버의 캐릭터가 참여되어있는지 확인 필요 */
        if(partyRepository.findPartyParticipant(findParty.getId(), findChar.getId()).isEmpty()) {
            PartyParticipant pp = PartyParticipant.createPartyParticipant(findParty, charOwner, findChar, type);
            findParty.addPartyParticiapnt(pp);
            partyRepository.registerPartyParticipant(pp);
            return pp.getId();
        } else {
            throw new Exception("Duplicated character registered");
        }

    }

    // 특정 파티의 파티원 가입 승인
    @Transactional
    public void acceptParticipant(Long ppId) throws Exception {
        PartyParticipant pp = partyRepository.findOneParticipant(ppId);
        Party party = pp.getJoinedParty();

        // ASSIST파티가 아닌데, 라이더 참가자가 신청하는 경우
        if( !party.getPartyType().equals(ASSIST) && pp.getParticipantType().equals(RIDER)) {
            throw new Exception("non-assist party can't take RIDER character");
        }

        // ASSIST파티가 아닌 경우, 항마력 체크 패스
        if(!party.getPartyType().equals(ASSIST)) {
            if(!validateAbility(party, pp.getJoinedPartyCharacter())) {
                throw new Exception("character's ability is not enough to satisfy required party minimum ability.");
            }
        }

        // 이미 같은 멤버의 캐릭터가 등록된 경우
        if(!partyRepository.findPartyParticipantByMemberWithStatus(party.getId(), pp.getJoinedPartyMember().getId(), ACCEPTED).isEmpty()) {
            throw new Exception(pp.getJoinedPartyMember().getNickname()+"'s character is already accepted");
        }
        // ASSIT 파티가 아닌데, 참여자의 상태가 RIDER인 경우
        if(pp.getParticipantType() == RIDER && party.getPartyType() != ASSIST) {
            throw new Exception(party.getId()+" party doesn't accept RIDER");
        }
        // 이미 승인된 경우
        if(pp.getParticipantStatus() == ACCEPTED) {
            throw new Exception(pp.getJoinedPartyCharacter().getName()+ "is already accepted");
        }
        // 파티에 빈 자리가 있는 경우에만 승인
        if(!validateJoinable(party)) {
            throw new Exception(party.getId()+" is alrady full");
        }
        // 클리어되지않았고,파티구인의 상태가 만원이 아닌경우
        if(party.getPartyStatus() != FULL && party.getPartyStatus() != COMPLETE) {
            if(pp.getParticipantType()==RIDER) {
                if(!validateRiderJoinable(party)) {
                    throw new Exception("rider capacity is full now");
                } else {
                    party.incRiderCharacterSize();
                    party.acceptParticipant();
                    pp.updateStatus(ACCEPTED);
                }
            } else {
                party.acceptParticipant();
                pp.updateStatus(ACCEPTED);
            }

        } else {
            throw new Exception(party.getId()+" party is already full");
        }
        // 파티가 가득 찼다면, 파티 상태 변경
        if(!validateJoinable(party)) {
            party.updatePartyStatus(FULL);
        }

    }

    // 특정 파티의 파티원 가입 거절
    @Transactional
    public void repelParticipant(Long ppId) throws Exception {
        PartyParticipant pp = partyRepository.findOneParticipant(ppId);
        Party party = pp.getJoinedParty();
        // 이미 거절된 경우
        if(pp.getParticipantStatus() == DECLINED) {
            throw new Exception(pp.getJoinedPartyCharacter().getName()+ "is already accepted");
        }// 이미 파티가 클리어된 경우, 참가자 상태 변경 불가
        else if(party.getPartyStatus() != COMPLETE) {
            // 승인되었던 유저라면 파티 잔존인원 수정
            if(pp.getParticipantStatus()==ACCEPTED) {
                party.repelAcceptedParticipant();
            }
            if(pp.getParticipantType()==RIDER) {
                party.decRiderCharacterSize();
            }

            // 승인된 유저로 가득 찼다면, 빠진 인원으로 인해 파티 구인 상태 변경
            if(party.getPartyStatus() == FULL) {
                party.updatePartyStatus(RECRUIT);
            }
            pp.updateStatus(DECLINED);
        } else {
            throw new Exception("Already cleared party.");
        }
    }

    //
    private boolean validateRiderJoinable(Party findParty) {
        return findParty.getAcceptedRiderSize() < findParty.getFreeRiderCapacity() ? true : false;
    }

    // 항마컷 제한 확인
    private boolean validateAbility(Party findParty, Characters findChar) {
        return findParty.getMinAbility().compareTo(findChar.getAbility()) <= 0 ? true : false;
    }

    private boolean validateJoinable(Party findParty) {
        int capacity = findParty.getPartyCapacity(); // 정원
        int acceptedMemberSize = findParty.getAcceptedMemberSize(); // 현재인원

        return capacity > acceptedMemberSize ? true : false;
    }



    // 파티 상태 변경 - PartyStatus:RECRUIT,FULL,COMPLETE
    public void updatePartyStatus(Long pId, PartyStatus status) {
        Party findParty = partyRepository.findOneParty(pId);
        findParty.updatePartyStatus(status);
    }

    // 파티 타입 변경 - PartyType:TRIAL, REWARD, CHALLENGE, NORMAL, ASSIST
    public void updatePartyType(Long pId, PartyType type) {
        Party findParty = partyRepository.findOneParty(pId);
        findParty.updatePartyType(type);
    }

    // 파티 모집 변경 - RecruitType: OPEN, GUILD
    public void updatePartyRecruitType(Long pId, RecruitType type) {
        Party findParty = partyRepository.findOneParty(pId);
        findParty.updateRecruitType(type);
    }

    // 파티원 타입 지정 - DRIVER, RIDER, NORMAL
    public void updateParticipantType(Long pId, Long cId, ParticipantType type) {
        Party findParty = partyRepository.findOneParty(pId);
        PartyParticipant pp = partyRepository.findPartyParticipant(pId, cId).get(0);
        pp.designateType(type);
    }

    // 파티 업둥 정원 수정
    public void updatePartyFreeRiderCapacity(Long pId, int capacity) throws Exception {
        Party findParty = partyRepository.findOneParty(pId);
        if(findParty.getPartyType().equals(ASSIST)) {
            throw new Exception("Non-assist party cannot set free rider capacity.");
        } else {
            if(capacity <= 0) {
                throw new Exception("Free rider capacity should be greater than 0");
            }
            findParty.designateFreeRiderCapacity(capacity);
        }
    }

   // 파티 정보 수정
    public void updatePartyReqAbility(Long pId, double ability) throws Exception {
        if(ability > 0){
            throw new Exception("ability cannot be negative");
        } else {
            Party findParty = partyRepository.findOneParty(pId);
            findParty.updateMinAbility(ability);
        }
    }

    // 파티 예정 시간 수정
    public void updatePartyReservedTime(Long pId, LocalDate time) {
        Party findParty = partyRepository.findOneParty(pId);
        findParty.updateReservedTime(time);
    }


        /* 파티 검색 */
    public Party findByPartyId(Long partyId) { return partyRepository.findOneParty(partyId); }


    // 모든 파티 검색
    public List<Party> findAllParty() {
        return partyRepository.findAll();
    }

    // 연합 단위 파티 검색
    public List<Party> findPartyByCommunity(String cName) {
        return partyRepository.findAllByCommunity(cName);
    }

    // 특정 날짜 파티 조회
    public List<Party> findPartyByDate(LocalDate time) throws ParseException {
        return partyRepository.findPartyByDate(time);
    }

    // 특정 날짜 이후 파티 조회
    public List<Party> findPartyAfterDate(LocalDate time) {
        return partyRepository.findPartyAfterDate(time);
    }

    // 현재 시간 이후 파티 조회
    public List<Party> findPartyAfterNow() {
        return partyRepository.findPartyAfterNow();
    }


    // find PartyParticipant by Party/Character id
    public List<PartyParticipant> findPartyParticipant(Long pId, Long cId) {
        return partyRepository.findPartyParticipant(pId, cId);
    }

    // find all participant by party id
    public List<PartyParticipant> findAllParticipant(Long pId) {
        return partyRepository.findAllParticipant(pId);
    }

    // find all specific status' party participants on specific party
    public List<PartyParticipant> findPartyParticipantWithStatus(Long pId, ParticipantStatus status) {
        return partyRepository.findPartyParticipantWithStatus(pId, status);
    }


    // find all participants of specific type on specific party
    public List<PartyParticipant> findPartyParticipantWithType(Long pId, ParticipantType type) {
        return partyRepository.findPartyParticipantWithType(pId, type);
    }

}
