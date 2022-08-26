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

    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Server server;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "community_id")
    @NotNull
    private Member communityMaster;

    @OneToMany(mappedBy = "joinedCommunity")
    private List<Member> joinedMembers = new ArrayList<>();

    @OneToMany(mappedBy = "joinedCommunity")
    private List<Guild> joinedGuilds = new ArrayList<>();

    @OneToMany(mappedBy = "joinedCommunity")
    private List<Character> joinedCharacters = new ArrayList<>();


    /**
     * basic Community creation
     * @param server
     * @param name
     * @param master
     * @return
     */
    public Community create(Server server, String name, Member master) {
        Community community = new Community();
        community.server = server;
        community.name = name;
        community.communityMaster = master;

        return community;
    }

    // 비즈니스 로직
    /**
     * 커뮤니티 마스터 변경
     */
    public void changeMaster() {

    }

}
