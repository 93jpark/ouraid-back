package ouraid.ouraidback.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ouraid.ouraidback.Exception.DuplicateMemberException;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.dto.member.requestDto.DeleteMemberRequest;
import ouraid.ouraidback.dto.member.requestDto.JoinMemberRequest;
import ouraid.ouraidback.dto.member.requestDto.SearchMemberRequest;
import ouraid.ouraidback.dto.member.requestDto.UpdateMemberRequest;
import ouraid.ouraidback.dto.member.responseDto.DeleteMemberResponse;
import ouraid.ouraidback.dto.member.responseDto.JoinMemberResponse;
import ouraid.ouraidback.dto.member.responseDto.SearchMemberResponse;
import ouraid.ouraidback.dto.member.responseDto.UpdateMemberResponse;
import ouraid.ouraidback.service.MemberService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController @Slf4j
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/join")
    public JoinMemberResponse joinNewMember(@RequestBody @Valid JoinMemberRequest request) {
        try {
            Member newMember = Member.create(
                    request.getNickname(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getServer()
            );

            Long memberId = memberService.registerMember(newMember);
            return new JoinMemberResponse(memberId);
        } catch (Exception e) {
            throw e;
        }


    }

    @PostMapping("/withdraw")
    public DeleteMemberResponse withdrawMember(@RequestBody @Valid DeleteMemberRequest request) {
        Member findMember = memberService.findMemberById(request.getId());
        memberService.removeMemberOwnCharacters(findMember);
        memberService.memberWithdraw(findMember);

        return null;
    }

    @PutMapping("/updateNickname")
    public UpdateMemberResponse updateMemberNickname(@RequestBody @Valid UpdateMemberRequest request) {
        Member findMember = memberService.findMemberById(request.getId());
        memberService.updateMemberNickname(findMember.getId(), request.getNewNickname());

        return null;
    }

    @PutMapping("/updatePassword")
    public UpdateMemberResponse updateMemberPassword(@RequestBody @Valid UpdateMemberRequest request) {
        Member findMember = memberService.findMemberById(request.getId());
        memberService.changeMemberPassword(findMember.getId(), request.getNewPassword());
        return null;
    }

    @GetMapping("/search")
    public SearchMemberResponse searchMemberById(@RequestBody @Valid SearchMemberRequest request) {
        return null;
    }




}
