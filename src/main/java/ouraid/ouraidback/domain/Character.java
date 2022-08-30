package ouraid.ouraidback.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.enums.MainClass;
import ouraid.ouraidback.domain.enums.Server;
import ouraid.ouraidback.domain.enums.SubClass;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Character {

    @Id @GeneratedValue @Column(name="character_id") private Long id;

    @NotNull private String name;

    @Enumerated(EnumType.STRING) @NotNull private Server server;

    @Enumerated(EnumType.STRING) @NotNull private MainClass mainClass;

    @Enumerated(EnumType.STRING) @NotNull private SubClass subClass;

    @NotNull private Double ability;

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
    public static Character create(Server server, String name, MainClass mainClass, SubClass subClass, Double ability, Member characterOwner) {
        Character character = new Character();
        character.server = server;
        character.name = name;
        character.mainClass = mainClass;
        character.subClass = subClass;
        character.ability = ability;
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
    public static Character create(Server server, String name, MainClass mainClass, SubClass subClass, Double ability, Member characterOwner, Guild joinedGuild, Community joinedCommunity) {
        Character character = new Character();

        character.server = server;
        character.name = name;
        character.mainClass = mainClass;
        character.subClass = subClass;
        character.ability = ability;
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
    public void changeAbility(Double ab) {
        this.ability = ab;
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
