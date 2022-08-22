package ouraid.ouraidback.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Member {

    public Member(String nickname, String email, String password, Server server) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.server = server;
    }

    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private Boolean availability = true;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Server server;

    @OneToOne(mappedBy = "CommunityMaster")
    @Nullable
    private Community ownCommunity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community joinedCommunity;

    @OneToMany(mappedBy = "guildMaster")
    @Nullable
    private List<Guild> ownGuilds = new ArrayList<>();

    @OneToMany(mappedBy="guild")
    private List<GuildMember> joinedGuilds = new ArrayList<>();

    @OneToMany(mappedBy = "characterOwner")
    private List<Character> ownCharacters = new ArrayList<>();



}
