package ouraid.ouraidback.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Character {

    @Id @GeneratedValue @Column(name="character_id") private Long id;

    @Enumerated(EnumType.STRING) @NotNull private Server server;

    @Enumerated(EnumType.STRING) @NotNull private MainClass mainClass;

    @Enumerated(EnumType.STRING) @NotNull private SubClass subClass;

    @NotNull private Float ability;

    @ManyToOne @JoinColumn(name = "member_id") private Member characterOwner;

    @ManyToOne @JoinColumn(name = "guild_id") private Guild joinedGuild;

    @ManyToOne @JoinColumn(name = "community_id") private Community joinedCommunity;

    /**
     * create character without any joined guild or community
     * @param server
     * @param mainClass
     * @param subClass
     * @param ability
     * @param characterOwner
     * @return
     */
    public Character create(Server server, MainClass mainClass, SubClass subClass, Float ability, Member characterOwner) {
        Character character = new Character();
        character.server = server;
        character.mainClass = mainClass;
        character.subClass = subClass;
        character.ability = ability;
        character.characterOwner = characterOwner;

        return character;
    }

    /**
     * create character with joined guild and community
     * @param server
     * @param mainClass
     * @param subClass
     * @param ability
     * @param characterOwner
     * @param joinedGuild
     * @param joinedCommunity
     * @return
     */
    public Character create(Server server, MainClass mainClass, SubClass subClass, Float ability, Member characterOwner, Guild joinedGuild, Community joinedCommunity) {
        Character character = new Character();

        character.server = server;
        character.mainClass = mainClass;
        character.subClass = subClass;
        character.ability = ability;
        character.characterOwner = characterOwner;
        character.joinedGuild = joinedGuild;
        character.joinedCommunity = joinedCommunity;

        return character;
    }



}
