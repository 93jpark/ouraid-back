package ouraid.ouraidback.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ouraid.ouraidback.Exception.DuplicateResourceException;
import ouraid.ouraidback.Exception.ResourceNotFoundException;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.dto.ApiResponse;
import ouraid.ouraidback.dto.characters.requestDto.CreateCharacterListRequest;
import ouraid.ouraidback.dto.characters.requestDto.CreateCharacterRequest;
import ouraid.ouraidback.dto.characters.requestDto.SearchCharacterRequest;
import ouraid.ouraidback.dto.characters.requestDto.ValidateCharacterRequest;
import ouraid.ouraidback.dto.characters.responseDto.SearchCharacterResponse;
import ouraid.ouraidback.dto.characters.responseDto.SearchCharactersListResponse;
import ouraid.ouraidback.dto.enums.ResponseMessage;
import ouraid.ouraidback.service.CharacterService;
import ouraid.ouraidback.service.GuildService;
import ouraid.ouraidback.service.MemberService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
        try {
            // validate character name before creation
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
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.CREATE_USER_FAIL, e.getMessage());
        }
    }

    @PostMapping("/createChars")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> registerNewCharList(@RequestBody @Valid CreateCharacterListRequest request) {
        List<CreateCharacterRequest> charList = request.getCharList();
        Member member;
        try {
            Long memberId = request.getCharList().get(0).getOwnerId();
            member = memberService.findMemberById(memberId);
            for(CreateCharacterRequest cr : charList) {
                characterService.validateDuplicateCharacter(cr.getName());
            }
        } catch (DuplicateResourceException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_RES, e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER, e.getMessage());
        }

        try {
            for(CreateCharacterRequest cr : charList) {
                characterService.registerCharacter(
                        Characters.create(
                            cr.getServer(),
                            cr.getName(),
                            cr.getMainClass(),
                            cr.getSubClass(),
                            cr.getAbility(),
                            member
                        )
                );
            }
            return ApiResponse.of(HttpStatus.CREATED, ResponseMessage.CREATED_CHAR, request.getCharList().size() + " character has been registered");

        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.CREATE_CHAR_FAIL, e.getMessage());
        }
    }

    @PostMapping("/validate")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> validateCharName(@RequestBody @Valid ValidateCharacterRequest request) {
        try {
            String newName = request.getNewName();
            characterService.validateDuplicateCharacter(newName);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.RESOURCE_AVAILABLE, newName);
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_RES, e.getMessage());
        }
    }

    @GetMapping("/searchByCharId")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> searchByCharId(@RequestBody @Valid SearchCharacterRequest request) {
        try{
            Long charId = request.getId();
            Characters findChar = characterService.findOne(charId);
            return ApiResponse.of(HttpStatus.FOUND, ResponseMessage.FOUND_CHAR, SearchCharacterResponse.toDto(findChar));
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_CHAR, e.getMessage());
        }
    }

    @GetMapping("/searchByCharName")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> searchByCharName(@RequestBody @Valid SearchCharacterRequest request) {
        try {
            String charName = request.getCharName();
            Characters findChar = characterService.findCharByName(charName);
            return ApiResponse.of(HttpStatus.FOUND, ResponseMessage.FOUND_CHAR, SearchCharacterResponse.toDto(findChar));
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_CHAR, e.getMessage());
        }
    }

    @GetMapping("/searchByOwnerNickname")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> searchByOwnerNickname(@RequestBody @Valid SearchCharacterRequest request) {
        try {
            String ownerName = request.getMemberName();
            List<Characters> findCharList = characterService.findCharactersByOwnerNickname(ownerName);

            return ApiResponse.of(HttpStatus.FOUND, ResponseMessage.FOUND_CHAR,
                    SearchCharactersListResponse.builder().charList(SearchCharactersListResponse.toDto(findCharList)).build());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_CHAR, e.getMessage());
        }
    }

    @GetMapping("/searchByGuildName")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> searchByGuildName(@RequestBody @Valid SearchCharacterRequest request) {
        try {
            String guildName = request.getGuildName();
            List<Characters> findCharList = characterService.findCharactersByGuildName(guildName);
            return ApiResponse.of(HttpStatus.FOUND, ResponseMessage.FOUND_CHAR,
                    SearchCharactersListResponse.builder().charList(SearchCharactersListResponse.toDto(findCharList)).build());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_CHAR, e.getMessage());
        }
    }

    @GetMapping("/searchByCommunityName")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> searchByCommunityName(@RequestBody @Valid SearchCharacterRequest request) {
        try {
            String comName = request.getCommunityName();
            List<Characters> findCharList = characterService.findCharactersByCommunityName(comName);
            return ApiResponse.of(HttpStatus.FOUND, ResponseMessage.FOUND_CHAR,
                    SearchCharactersListResponse.builder().charList(SearchCharactersListResponse.toDto(findCharList)).build());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_CHAR, e.getMessage());
        }
    }
}
