package ouraid.ouraidback.dto.characters.requestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UpdateCharacterRequest {
    private Long id;
    private BigDecimal newAbility;
    private String newName;
}
