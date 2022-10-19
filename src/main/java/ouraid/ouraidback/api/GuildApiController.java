package ouraid.ouraidback.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ouraid.ouraidback.service.CharacterService;
import ouraid.ouraidback.service.GuildService;
import ouraid.ouraidback.service.MemberService;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/guild")
public class GuildApiController {

    private final MemberService memberService;
    private final GuildService guildService;
    private final CharacterService characterService;

}
