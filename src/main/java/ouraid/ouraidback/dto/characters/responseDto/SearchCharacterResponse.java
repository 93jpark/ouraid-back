package ouraid.ouraidback.dto.characters.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.enums.MainClass;
import ouraid.ouraidback.domain.enums.Server;
import ouraid.ouraidback.domain.enums.SubClass;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class SearchCharactersResponse {
    private Long id;
    private String name;
    private Server server;
    private MainClass mainClass;
    private SubClass subClass;
    private BigDecimal ability;


    private Integer expHardLotus;
    private Integer expDungeon;
    private Integer expNormalLotus;
    private Integer expWorldBoss;

    public SearchCharactersResponse(Characters character) {
        this.id = character.getId();
        this.name = character.getName();
        this.server = character.getServer();
        this.mainClass = character.getMainClass();
        this.subClass = character.getSubClass();
        this.ability = character.getAbility();

        this.expHardLotus = character.getExpHardLotus();
        this.expDungeon = character.getExpDungeon();
        this.expNormalLotus = character.getExpNormalLotus();
        this.expWorldBoss = character.getExpWorldBoss();
    }
}
