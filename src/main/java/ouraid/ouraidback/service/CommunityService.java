package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Community;
import ouraid.ouraidback.domain.Guild;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.repository.CommunityRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service @Slf4j
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    @PersistenceContext
    EntityManager em;

    private final CommunityRepository communityRepository;

    // 연합 생성
    @Transactional
    public Long registerCommunity(Community community) {
        validateDuplicateCommunity(community);
        return communityRepository.register(community);
    }

    /* 연합 정보 업데이트 - 연합장, 소속멤버, 소속길드 */
    // 연합마스터 수정
    @Transactional
    public void changeMaster(Long comId, Member newMaster) {
        Community findCom = communityRepository.findOne(comId);
        findCom.getCommunityMaster().unsetCommunityMaster();
        findCom.changeMaster(newMaster);
        newMaster.setCommunityMaster(findCom);
    }

    // 연합 멤버 추가
    @Transactional
    public void addMember(Long comId, Member member) {

    }

    // 연합 길드 추가
    @Transactional
    public void addGuild(Long comId, Guild guild) {

    }


    // 연합 해체
    @Transactional
    public void removeCommunity(Long comId) {

    }

    /* 연합 조회 */
    // ID를 통한 연합 조회
    @Transactional(readOnly = true) public Community findByComId(Long id) { return communityRepository.findOne(id); }

    // 연합 이름을 통한 조회
    @Transactional(readOnly = true) public List<Community> findComByName(String name) { return communityRepository.findByComName(name); }

    // 연합 이름 중복 조회
    @Transactional(readOnly = true)
    public void validateDuplicateCommunity(Community community) {
        List<Community> findCom = communityRepository.findByComName(community.getName());
        if(!findCom.isEmpty()) {
            throw new IllegalStateException("중복된 연합명 존재");
        }
        log.info("{} can be registered", community.getName());
    }
}
