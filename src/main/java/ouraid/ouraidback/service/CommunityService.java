package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Community;
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

    // 연합 정보 업데이트 - 연합장, 소속멤버, 소속길드

    // 연합 해체

    /* 연합 조회 */
    @Transactional(readOnly = true) public Community findByComId(Long id) { return communityRepository.findOne(id); }
    @Transactional(readOnly = true) public List<Community> findComByName(String name) { return communityRepository.findByComName(name); }

    @Transactional(readOnly = true)
    public void validateDuplicateCommunity(Community community) {
        List<Community> findCom = communityRepository.findByComName(community.getName());
        if(!findCom.isEmpty()) {
            throw new IllegalStateException("중복된 연합명 존재");
        }
        log.info("{} can be registered", community.getName());
    }
}
