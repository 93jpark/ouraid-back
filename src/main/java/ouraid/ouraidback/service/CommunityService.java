package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Community;
import ouraid.ouraidback.domain.Guild;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.repository.CharacterRepository;
import ouraid.ouraidback.repository.CommunityRepository;
import ouraid.ouraidback.repository.GuildRepository;
import ouraid.ouraidback.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service @Slf4j
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    @PersistenceContext
    EntityManager em;

    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final GuildRepository guildRepository;
    private final CharacterRepository characterRepository;

    // 연합 생성
    @Transactional
    public Long registerCommunity(Community community) {
        validateDuplicateCommunity(community.getName());
        Long comId = communityRepository.register(community);
        community.getCommunityMaster().setJoinedCommunity(community);
        return comId;
    }

        /* 연합 정보 업데이트 - 이름, 연합장, 소속멤버, 소속길드 */
    // 연합 이름 변경
    @Transactional
    public void changeCommunityName(Long comId, String newName) {
        validateDuplicateCommunity(newName);
        Community findCom = communityRepository.findOne(comId);
        findCom.changeName(newName);
    }

    // 연합마스터 수정
    @Transactional
    public void changeMaster(Long comId, Member newMaster) {
        Community findCom = communityRepository.findOne(comId);
        Member exComMaster = findCom.getCommunityMaster();
        exComMaster.unsetCommunityMaster();
        findCom.changeMaster(newMaster);
        newMaster.setCommunityMaster(findCom);
    }

    // 연합 캐릭터 추가
    @Transactional
    public void addCharacter(Long comId, Long charId) {
        Community findCom = communityRepository.findOne(comId);
        Characters findChar = characterRepository.findOne(charId);

        findCom.addJoinedCharacter(findChar);
        findChar.joinNewCommunity(findCom);
    }

    // 연합 멤버 추가
    @Transactional
    public void addMember(Long comId, Long memberId) {
        Community findCom = communityRepository.findOne(comId);
        Member findMember = memberRepository.findOne(memberId);
        findCom.addJoinedMember(findMember);
        findMember.setJoinedCommunity(findCom);
    }

    // 연합 길드 추가
    @Transactional
    public void addGuild(Long comId, Long gId) {
        Guild findGuild = guildRepository.findOne(gId);
        Community findCom = communityRepository.findOne(comId);

        findCom.addJoinedGuild(findGuild);
        findGuild.joinNewCommunity(findCom);
    }


    // 연합 해체(삭제)
    @Transactional
    public void removeCommunity(Long comId) {
        Community findCom = communityRepository.findOne(comId);
        List<Guild> joinedGuilds = findCom.getJoinedGuilds();
        List<Member> joinedMembers = findCom.getJoinedMembers();
        List<Characters> joinedCharacters = findCom.getJoinedCharacters();
        for(Guild g : joinedGuilds) {
            g.leaveJoinedCommunity();
        }
        for(Member m : joinedMembers) {
            m.unsetJoinedCommunity();
        }
        for(Characters c : joinedCharacters) {
            c.leaveJoinedCommunity();
        }
        findCom = null;
    }

    /* 연합 조회 */
    // ID를 통한 연합 조회
    @Transactional(readOnly = true) public Community findByComId(Long id) { return communityRepository.findOne(id); }

    // 연합 이름을 통한 조회
    @Transactional(readOnly = true) public List<Community> findComByName(String name) { return communityRepository.findByComName(name); }

    // 연합 이름 중복 조회
    @Transactional(readOnly = true)
    public void validateDuplicateCommunity(String communityName) {
        List<Community> findCom = communityRepository.findByComName(communityName);
        if(!findCom.isEmpty()) {
            throw new IllegalStateException("중복된 연합명 존재");
        }
        log.info("{} can be registered", communityName);
    }
}
