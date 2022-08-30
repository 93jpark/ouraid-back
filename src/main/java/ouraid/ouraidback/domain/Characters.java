package ouraid.ouraidback.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @NotNull private BigDecimal ability;

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

        return character;
    }

    /**
     * create character with joined guild and community
     * @param server
     * @param name
     * @param mainClass
     * @param subClass
     * @param ability
     * @param characterOwner
     * @param joinedGuild
     * @param joinedCommunity
     * @return
     */
    public static Characters create(Server server, String name, MainClass mainClass, SubClass subClass, Double ability, Member characterOwner, Guild joinedGuild, Community joinedCommunity) {
        Characters character = new Characters();

        character.server = server;
        character.name = name;
        character.mainClass = mainClass;
        character.subClass = subClass;
        character.ability = BigDecimal.valueOf(ability);
        character.characterOwner = characterOwner;
        character.joinedGuild = joinedGuild;
        character.joinedCommunity = joinedCommunity;

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

    // 캐릭터 길드 가입
    public void joinNewGuild(Guild guild) {
        this.joinedGuild = guild;
        guild.addGuildCharacter(this);
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
