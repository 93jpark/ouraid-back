package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.GuildMember;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.ParticipantType;
import ouraid.ouraidback.domain.enums.PartyStatus;
import ouraid.ouraidback.domain.enums.PartyType;
import ouraid.ouraidback.domain.enums.RecruitType;
import ouraid.ouraidback.domain.party.Party;
import ouraid.ouraidback.domain.party.PartyParticipant;
import ouraid.ouraidback.repository.PartyRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static ouraid.ouraidback.domain.enums.ParticipantType.HOLDER;
import static ouraid.ouraidback.domain.enums.ParticipantType.NORMAL;
import static ouraid.ouraidback.domain.enums.PartyStatus.*;

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
    }

    // 파티 삭제
    @Transactional
    public void removeParty(Party party) {
        partyRepository.removeParty(party);
    }

    // 파티 파티원 추가
    @Transactional
    public void joinCharacterOnParty(Long pId, Long cId) throws Exception {
        Party findParty = partyRepository.findOne(pId);
        Characters findChar = characterService.findOne(cId);
        Member charOwner = findChar.getCharacterOwner();

        /** 파티 정원 초과인지 확인 */
        if(validateJoinable(findParty)) {
            /** 이미 해당 멤버의 캐릭터가 참여되어있는지 확인 필요 */
            if(partyRepository.findPartyParticipant(pId, cId).isEmpty()) {
                PartyParticipant pp = PartyParticipant.createPartyParticipant(findParty, charOwner, findChar);
                pp.designateType(NORMAL);
                findParty.addPartyCharacter(pp);
            } else {
                log.info("{} party already other character who has Member'{}'.", findParty.getId(), charOwner.getNickname());
                throw new Exception("Duplicated character registered");
            }
        } else {
            log.info("{} party is already fulled.", findParty.getId());
            throw new Exception("party is already full");
        }

    }


    // 파티 파티원 추가
    @Transactional
    public void joinCharacterOnPartyWithType(Long pId, Long cId, ParticipantType type) throws Exception {
        Party findParty = partyRepository.findOne(pId);
        Characters findChar = characterService.findOne(cId);
        Member charOwner = findChar.getCharacterOwner();

        /** 파티 정원 초과인지 확인 */
        if(validateJoinable(findParty)) {
            /** 이미 해당 멤버의 캐릭터가 참여되어있는지 확인 필요 */
            if(partyRepository.findPartyParticipant(pId, cId).isEmpty()) {
                PartyParticipant pp = PartyParticipant.createPartyParticipant(findParty, charOwner, findChar, type);
                findParty.addPartyCharacter(pp);
            } else {
                log.info("{} party already other character who has Member'{}'.", findParty.getId(), charOwner.getNickname());
                throw new Exception("Duplicated character registered");
            }
        } else {
            log.info("{} party is already fulled.", findParty.getId());
            throw new Exception("party is already full");
        }

    }

    private boolean validateJoinable(Party findParty) {
        int capacity = findParty.getPartyCapacity(); // 정원
        int partyJoinedMemberNum = findParty.getRegisteredMemberSize(); // 현재인원

        return capacity > partyJoinedMemberNum ? true : false;
    }


    // 파티에 파티원 삭제
    public void expelCharacter(Long pId, Long cId) {

    }

    // 파티 상태 변경 - PartyStatus:RECRUIT,FULL,COMPLETE
    public void updatePartyStatus(Long pId, PartyStatus status) {
        Party findParty = partyRepository.findOne(pId);
        findParty.updatePartyStatus(status);
    }

    // 파티 타입 변경 - PartyType:TRIAL, REWARD, CHALLENGE, NORMAL, ASSIST
    public void updatePartyType(Long pId, PartyType type) {
        Party findParty = partyRepository.findOne(pId);
        findParty.updatePartyType(type);
    }

    // 파티 모집 변경 - RecruitType: OPEN, GUILD
    public void updatePartyRecruitType(Long pId, RecruitType type) {
        Party findParty = partyRepository.findOne(pId);
        findParty.updateRecruitType(type);
    }

    // 파티원 타입 지정 - DRIVER, RIDER, NORMAL
    public void updateParticipantType(Long pId, Long cId, ParticipantType type) {
        Party findParty = partyRepository.findOne(pId);
        PartyParticipant pp = partyRepository.findPartyParticipant(pId, cId).get(0);
        pp.designateType(type);
    }

    // 파티 업둥 정원 수정
    public void updatePartyFreeRiderCapacity(Long pId, int capacity) throws Exception {
        Party findParty = partyRepository.findOne(pId);
        if(findParty.getPartyType().equals(PartyType.ASSIST)) {
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
            Party findParty = partyRepository.findOne(pId);
            findParty.updateMinAbility(ability);
        }
    }

    // 파티 예정 시간 수정
    public void updatePartyReservedTime(Long pId, Instant time) {
        Party findParty = partyRepository.findOne(pId);
        findParty.updateReservedTime(time);
    }


        /* 파티 검색 */
    public Party findByPartyId(Long partyId) { return partyRepository.findOne(partyId); }


    // 모든 파티 검색
    public List<Party> findAllParty() {
        return partyRepository.findAll();
    }

    // 연합 단위 파티 검색
    public List<Party> findPartyByCommunity(String cName) {
        return partyRepository.findAllByCommunity(cName);
    }

    // 날짜별 파티 검색
    public List<Party> findPartyByDate(LocalDateTime time) {
        //return partyRepository.findAllByDate(time);
        return null;
    }

    // find PartyParticipant by Party/Character id
    public List<PartyParticipant> findPartyParticipant(Long pId, Long cId) {
        return partyRepository.findPartyParticipant(pId, cId);
    }

    // find all participant by party id
    public List<PartyParticipant> findAllParticipant(Long pId) {
        return partyRepository.findAllParticipant(pId);
    }


}
