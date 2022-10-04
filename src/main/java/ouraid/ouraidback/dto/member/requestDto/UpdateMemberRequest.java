package ouraid.ouraidback.dto.member.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateMemberRequest {
    private Long id;
    private String newNickname;
    private String newPassword;
}
