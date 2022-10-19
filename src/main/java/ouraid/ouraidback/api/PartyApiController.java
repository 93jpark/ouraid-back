package ouraid.ouraidback.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ouraid.ouraidback.service.CharacterService;
import ouraid.ouraidback.service.GuildService;
import ouraid.ouraidback.service.MemberService;

import java.text.SimpleDateFormat;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/party")
public class PartyApiController {

    private final MemberService memberService;
    private final GuildService guildService;
    private final CharacterService characterService;

    //p.135
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(java.time.LocalDate.class, new CustomDateEditor(dateFormat, false));
    }
}
