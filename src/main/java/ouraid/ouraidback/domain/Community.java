package ouraid.ouraidback.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community {
    @Id @GeneratedValue
    @Column(name="community_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Server server;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "community_id")
    @NotNull
    private Member CommunityMaster;

    @OneToMany(mappedBy = "joinedCommunity")
    private List<Member> joinedMembers = new ArrayList<>();

    @OneToMany(mappedBy = "joinedCommunity")
    private List<Guild> joinedGuilds = new ArrayList<>();

    @OneToMany(mappedBy = "joinedCommunity")
    private List<Character> joinedCharacters = new ArrayList<>();




}
