package ouraid.ouraidback.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.dto.ApiResponse;
import ouraid.ouraidback.dto.characters.responseDto.SearchCharacterResponse;
import ouraid.ouraidback.dto.enums.ResponseMessage;
import ouraid.ouraidback.dto.member.requestDto.*;
import ouraid.ouraidback.dto.member.responseDto.DeleteMemberResponse;
import ouraid.ouraidback.dto.member.responseDto.JoinMemberResponse;
import ouraid.ouraidback.dto.member.responseDto.SearchMemberResponse;
import ouraid.ouraidback.dto.member.responseDto.UpdateMemberResponse;
import ouraid.ouraidback.service.CharacterService;
import ouraid.ouraidback.service.GuildService;
import ouraid.ouraidback.service.MemberService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController @Slf4j
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;
    private final GuildService guildService;
    private final CharacterService characterService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.FORBIDDEN) // exception is thrown : 403 code
    public ResponseEntity<ApiResponse> joinNewMember(@RequestBody @Valid JoinMemberRequest request) {
        Member newMember = Member.create(
                request.getNickname(),
                request.getEmail(),
                request.getPassword(),
                request.getServer()
        );

        try {
            Long memberId = memberService.registerMember(newMember);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_USER,
                    JoinMemberResponse.builder()
                            .memberId(newMember.getId())
                            .server(newMember.getServer())
                            .email(newMember.getEmail())
                            .build());

        } catch (Exception e) {
            log.warn(e.getMessage());
            return ApiResponse.of(HttpStatus.FORBIDDEN, ResponseMessage.CREATED_USER_FAIL+":"+e.getMessage());
        }

    }

    @PostMapping("/withdraw")
    public ResponseEntity<ApiResponse> withdrawMember(@RequestBody @Valid DeleteMemberRequest request) {
        Member findMember;
        try {
            findMember = memberService.findMemberById(request.getId());
            Long delUserId = findMember.getId();
            String delUserName = findMember.getNickname();
            memberService.removeMemberOwnCharacters(findMember);
            memberService.memberWithdraw(findMember);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_USER,
                    DeleteMemberResponse.builder()
                            .id(delUserId)
                            .msg("User "+delUserName + " is removed.")
                            .build());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER);
        }
    }

    @PostMapping("/nicknameValidate")
    public ResponseEntity<ApiResponse> validateNickname(@RequestBody @Valid EmailValidationRequest request) {
        try {
            memberService.validateDuplicatedMemberEmail(request.getEmail());
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.RESOURCE_AVAILABLE, request.getEmail());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_EMAIL, request.getEmail());
        }
    }

    @PostMapping("/emailValidate")
    public ResponseEntity<ApiResponse> validateEmail(@RequestBody @Valid EmailValidationRequest request) {
        try {
            memberService.validateDuplicatedMemberEmail(request.getEmail());
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.RESOURCE_AVAILABLE, request.getEmail());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_EMAIL, request.getEmail());
        }
    }

    @PutMapping("/updateNickname")
    public ResponseEntity<ApiResponse> updateMemberNickname(@RequestBody @Valid UpdateMemberRequest request) {
        Member findMember;
        try {
            findMember = memberService.findMemberById(request.getId());
            String prevName = findMember.getNickname();
            memberService.updateMemberNickname(findMember.getId(), request.getNewNickname());
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_USER,
                    UpdateMemberResponse.builder()
                            .msg(prevName+" is changed to "+request.getNewNickname())
                            .build());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.DUPLICATE_NICKNAME, request.getNewNickname());
        }

    }

    @PutMapping("/updatePassword")
    public ResponseEntity<ApiResponse> updateMemberPassword(@RequestBody @Valid UpdateMemberRequest request) {
        try {
            Member findMember = memberService.findMemberById(request.getId());
            memberService.changeMemberPassword(findMember.getId(), request.getNewPassword());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_USER,
                    UpdateMemberResponse.builder()
                            .msg(findMember.getNickname()+"'s password is updated.")
                            .build());

        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // search and return Member DTO without password, Guild/Community Info.
    // only include extra data, own characters
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchMemberById(@RequestBody @Valid SearchMemberRequest request) {
        try {
            Member findMember = memberService.findMemberById(request.getId());
            log.warn(request.toString());
            List<SearchCharacterResponse> ownChars = findMember.getOwnCharacters().stream()
                    .map(character -> new SearchCharacterResponse(character))
                    .collect(Collectors.toList());
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.READ_USER,
                    SearchMemberResponse.builder()
                            .id(findMember.getId())
                            .nickname(findMember.getNickname())
                            .email(findMember.getEmail())
                            .availability(findMember.getAvailability())
                            .server(findMember.getServer())
                            .ownChars(ownChars)
                            .build());

        } catch(Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
