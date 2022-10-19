package ouraid.ouraidback.domain;

import com.sun.istack.NotNull;
import lombok.*;
import ouraid.ouraidback.domain.enums.MainClass;
import ouraid.ouraidback.domain.enums.Server;
import ouraid.ouraidback.domain.enums.SubClass;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.FetchType.LAZY;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Characters {

    @Id @GeneratedValue @Column(name="character_id") private Long id;

    @NotNull private String name;

    @Enumerated(EnumType.STRING) @NotNull private Server server;

    @Enumerated(EnumType.STRING) @NotNull private MainClass mainClass;

    @Enumerated(EnumType.STRING) @NotNull private SubClass subClass;

    @NonNull private BigDecimal ability;

    private Integer expHardLotus;
    private Integer expDungeon;
    private Integer expNormalLotus;
    private Integer expWorldBoss;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "member_id") private Member characterOwner;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "guild_id") private Guild joinedGuild;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "community_id") private Community joinedCommunity;

    /**
     * create character without any joined guild or community
     * @param server
     * @param name
     * @param mainClass
     * @param subClass
     * @param ability
     * @param characterOwner
     * @return
     */
    public static Characters create(Server server, String name, MainClass mainClass, SubClass subClass, Double ability, Member characterOwner) {
        Characters character = new Characters();
        character.server = server;
        character.name = name;
        character.mainClass = mainClass;
        character.subClass = subClass;
        character.ability = BigDecimal.valueOf(ability);
        character.characterOwner = characterOwner;
        character.expDungeon = 0;
        character.expHardLotus = 0;
        character.expNormalLotus = 0;
        character.expWorldBoss = 0;

        return character;
    }

    // 캐릭명 변경
    public void changeName(String newName) {
        this.name = newName;
    }

    // 항마 변경
    public void changeAbility(Double ability) {
        this.ability = BigDecimal.valueOf(ability);
    }

    // 레이드 횟수 증가
    public void incExpDungeon() { this.expDungeon = this.expDungeon+1; }

    public void incExpNormalLotus() { this.expNormalLotus = this.expNormalLotus+1; }

    public void incExpHardLotus() { this.expHardLotus = this.expHardLotus+1; }

    public void incExpWorldBoss() { this.expWorldBoss = this.expWorldBoss+1; }

    // 캐릭터 길드 가입
    public void joinNewGuild(Guild guild) {
        this.joinedGuild = guild;
        guild.joinGuildCharacter(this);
    }

    // 캐릭터 커뮤니티 가입
    public void joinNewCommunity(Community community) {
        this.joinedCommunity = community;
        community.addJoinedCharacter(this);
    }

    // 가입된 길드 탈퇴
    public void leaveJoinedGuild() {
        this.joinedGuild.leaveGuildByCharacter(this);
        this.joinedGuild = null;
    }

    // 가입된 커뮤니티 탈퇴
    public void leaveJoinedCommunity() {
        this.joinedCommunity.removeJoinedCharacter(this);
        this.joinedCommunity = null;
    }

}
