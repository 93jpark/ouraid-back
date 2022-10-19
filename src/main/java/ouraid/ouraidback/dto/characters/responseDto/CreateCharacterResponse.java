package ouraid.ouraidback.dto.characters.responseDto;

import lombok.Builder;
import lombok.Data;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.enums.MainClass;
import ouraid.ouraidback.domain.enums.Server;
import ouraid.ouraidback.domain.enums.SubClass;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Data
@Builder
public class CreateCharacterResponse {
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Server server;
    @Enumerated(EnumType.STRING)
    private MainClass mainClass;
    @Enumerated(EnumType.STRING)
    private SubClass subClass;
    private BigDecimal ability;
    private Long ownerId;
    private String ownerNickname;

    public static CreateCharacterResponse toDto(Characters newChar) {
        return CreateCharacterResponse.builder()
                .id(newChar.getId())
                .name(newChar.getName())
                .ability(newChar.getAbility())
                .server(newChar.getServer())
                .mainClass(newChar.getMainClass())
                .subClass(newChar.getSubClass())
                .ownerId(newChar.getCharacterOwner().getId())
                .ownerNickname(newChar.getCharacterOwner().getNickname())
                .build();
    }
}
