package ouraid.ouraidback.dto.member.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMemberRequest {
    private Long id;
    private String nickname;
    private String charName;
    private String guildName;
    private String communityName;
    private String serverName;
}
