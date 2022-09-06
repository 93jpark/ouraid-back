package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.repository.CharacterRepository;
import ouraid.ouraidback.repository.MemberRepository;
import ouraid.ouraidback.domain.Characters;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class CharacterService {

    private final MemberRepository memberRepository;
    private final CharacterRepository characterRepository;

    @PersistenceContext
    EntityManager em;

    // 캐릭터 생성
    @Transactional
    public Long registerCharacter(Characters character) {
        validateDuplicateCharacter(character);
        characterRepository.register(character);
        return character.getId();
    }

    // 캐릭터 삭제

    /* 캐릭터 정보 업데이트 */
    @Transactional
    public void updateCharacterName(Long charId, String newName) {
        try {
            Characters findChar = characterRepository.findOne(charId);
            findChar.changeName(newName);
            em.flush();
            em.clear();
        } catch (Exception e) {
            log.warn("CharacterService.updateCharacterName() : 이름 변경 실패 - {}", e.getMessage());
        }
    }

    @Transactional
    public void updateCharacterAbility(Long charId, Double ability) {
        try {
            Characters findChar = characterRepository.findOne(charId);
            findChar.changeAbility(ability);
            em.flush();
            em.clear();
        } catch (Exception e) {
            log.warn("CharacterService.updateCharacterAbility() : 항마 변경 실패 - {}", e.getMessage());
        }
    }



        /* 캐릭터 조회 */

    // id기반 캐릭터 단일 조회
    @Transactional(readOnly = true)
    public Characters findOne(Long charId) { return characterRepository.findOne(charId); }

    // 등록된 캐릭터 모두 조회
    @Transactional(readOnly = true)
    public List<Characters> findAll() { return characterRepository.findAll(); }

    // 서버 기반 등록 캐릭터 모두 조회
    @Transactional(readOnly = true)
    public List<Characters> findByServer(String sName) {
        return characterRepository.findByServer(sName);
    }

    // 특정 길드의 멤버가 지닌 캐릭터 조회
    public List<Characters> findCharactersByMemberWithGuild(String gName, String mName) {
        return characterRepository.findCharactersByMemberWithGuild(gName, mName);
    }

    /* 캐릭명 중복 검사 */
    @Transactional(readOnly = true)
    public void validateDuplicateCharacter(Characters character) {
        List<Characters> findChar = characterRepository.findByCharName(character.getName());
        log.info("{} get ListCharacters from repository", findChar.size());

        if (!findChar.isEmpty()) {
            throw new IllegalStateException("MemberService.validateDuplicateMember() : 이미 존재하는 캐릭터 명");
        }
        log.info("{} can be registered", character.getName());
    }
}
