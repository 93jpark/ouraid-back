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

    public Character(Server server, MainClass mainClass, SubClass subClass, Float ability, Member characterOwner) {
        this.server = server;
        this.mainClass = mainClass;
        this.subClass = subClass;
        this.ability = ability;
        this.characterOwner = characterOwner;
    }

    public Character(Server server, MainClass mainClass, SubClass subClass, Float ability, Member characterOwner, Guild joinedGuild, Community joinedCommunity) {
        this.server = server;
        this.mainClass = mainClass;
        this.subClass = subClass;
        this.ability = ability;
        this.characterOwner = characterOwner;
        this.joinedGuild = joinedGuild;
        this.joinedCommunity = joinedCommunity;
    }

    @Id @GeneratedValue
    @Column(name="character_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Server server;

    @Enumerated(EnumType.STRING)
    @NotNull
    private MainClass mainClass;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SubClass subClass;

    @NotNull
    private Float ability;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member characterOwner;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    private Guild joinedGuild;


    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community joinedCommunity;

}
