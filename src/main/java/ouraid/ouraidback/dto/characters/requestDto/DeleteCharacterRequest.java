package ouraid.ouraidback.dto.characters.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class DeleteCharacterRequest {
    private Long id;
    private String charName;
}
