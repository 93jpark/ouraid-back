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
public class SearchCharacterResponse {
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

    private String joinedGuildName;
    private String joinedCommunityName;
    private String ownMemberName;

    public SearchCharacterResponse(Characters character) {
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

    public static SearchCharacterResponse toDto(Characters findChar) {
        return SearchCharacterResponse.builder()
                .id(findChar.getId())
                .name(findChar.getName())
                .server(findChar.getServer())
                .mainClass(findChar.getMainClass())
                .subClass(findChar.getSubClass())
                .ability(findChar.getAbility())
                .expDungeon(findChar.getExpDungeon())
                .expNormalLotus(findChar.getExpNormalLotus())
                .expHardLotus(findChar.getExpHardLotus())
                .expWorldBoss(findChar.getExpWorldBoss())
                .ownMemberName(findChar.getCharacterOwner().getNickname())
                .joinedGuildName(findChar.getJoinedGuild().getName())
                .joinedCommunityName(findChar.getJoinedCommunity().getName())
                .build();
    }
}
