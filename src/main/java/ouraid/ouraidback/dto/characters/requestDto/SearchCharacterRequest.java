package ouraid.ouraidback.dto.characters.requestDto;

import lombok.Data;

@Data
public class SearchCharacterRequest {
    private Long id;
    private String charName;
    private String memberName;
    private String guildName;
    private String CommunityName;
}
