package ouraid.ouraidback.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guild {
    @Id @GeneratedValue @Column(name="guild_id") private Long id;

    @NotNull private String name;

    @NotNull private int level;

    @Enumerated(EnumType.STRING) @NotNull private Server server;

    @ManyToOne @JoinColumn(name = "community_id") private Community joinedCommunity;

    @ManyToOne @JoinColumn(name = "member_id") @NotNull private Member guildMaster;

    @OneToMany(mappedBy="member") private List<GuildMember> guildMembers = new ArrayList<>();

    @OneToMany(mappedBy = "joinedGuild") private List<Character> guildCharacters = new ArrayList<>();

    // 생성 메소드

    public Guild create(Server server, String name, int level, Member master, Community joinedCommunity) {
        Guild guild = new Guild();

        guild.name = name;
        guild.server = server;
        guild.level = level;
        guild.joinedCommunity = joinedCommunity;
        guild.guildMaster = master;

        return guild;
    }


}
