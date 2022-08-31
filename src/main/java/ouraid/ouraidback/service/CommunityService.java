package ouraid.ouraidback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.repository.CommunityRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    // 연합 생성

    // 연합 정보 업데이트 - 연합장, 소속멤버, 소속길드

    // 연합 해체
}
