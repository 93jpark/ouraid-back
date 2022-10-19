package ouraid.ouraidback.dto.characters.requestDto;

import lombok.Builder;
import lombok.Data;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.enums.MainClass;
import ouraid.ouraidback.domain.enums.Server;
import ouraid.ouraidback.domain.enums.SubClass;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class CreateCharacterRequest {
    private String name;
    @Enumerated(EnumType.STRING)
    private Server server;
    @Enumerated(EnumType.STRING)
    private MainClass mainClass;
    @Enumerated(EnumType.STRING)
    private SubClass subClass;
    private Double ability;
    private Long ownerId;

}
