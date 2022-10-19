package ouraid.ouraidback.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ouraid.ouraidback.Exception.DuplicateResourceException;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.dto.ApiResponse;
import ouraid.ouraidback.dto.characters.requestDto.CreateCharacterRequest;
import ouraid.ouraidback.dto.enums.ResponseMessage;
import ouraid.ouraidback.service.CharacterService;
import ouraid.ouraidback.service.GuildService;
import ouraid.ouraidback.service.MemberService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/character")
public class CharactersApiController {

    private final MemberService memberService;
    private final GuildService guildService;
    private final CharacterService characterService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> registerNewChar(@RequestBody @Valid CreateCharacterRequest request) {
        Characters newChar;
        /**
         * validate char name before creation
         */

        try {
            characterService.validateDuplicateCharacter(request.getName());

            newChar = Characters.create(
                    request.getServer(),
                    request.getName(),
                    request.getMainClass(),
                    request.getSubClass(),
                    request.getAbility(),
                    memberService.findMemberById(request.getOwnerId())
            );

            characterService.registerCharacter(newChar);


            return ApiResponse.of(HttpStatus.CREATED, ResponseMessage.CREATED_CHAR, newChar);
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_RES, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.CREATED_USER_FAIL, e.getMessage());
        }



    }
}
