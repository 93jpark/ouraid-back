package ouraid.ouraidback.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Community {
    @Id @GeneratedValue @Column(name="community_id") private Long id;

    @NotNull private String name;

    @Enumerated(EnumType.STRING) @NotNull private Server server;

    @OneToOne(fetch = LAZY, cascade = PERSIST)
    @JoinColumn(name = "community_id")
    @NotNull
    private Member communityMaster;

    @OneToMany(mappedBy = "joinedCommunity", cascade = PERSIST)
    private List<Characters> joinedCharacters = new ArrayList<>();

    @OneToMany(mappedBy = "joinedCommunity", cascade = PERSIST)
    private List<Member> joinedMembers = new ArrayList<>();

    @OneToMany(mappedBy = "joinedCommunity", cascade = PERSIST)
    private List<Guild> joinedGuilds = new ArrayList<>();

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

    // 커뮤니티 마스터 변경
    public void changeMaster(Member member) {
        this.communityMaster = member;
    }

    // 커뮤니티 이름 변경
    public void changeName(String newName) {
        this.name = newName;
    }

    // 소속 캐릭터 추가
    public void addJoinedCharacter(Characters newCharacter) {
        this.joinedCharacters.add(newCharacter);
    }

    // 소속 캐릭터 삭제
    public void removeJoinedCharacter(Characters character) {
        this.joinedCharacters.remove(character);
    }

    // 소속 멤버 추가
    public void addJoinedMember(Member member) {
        this.joinedMembers.add(member);
    }

    // 소속 멤버 삭제
    public void removeJoinedMember(Member member) {
        this.joinedMembers.remove(member);
    }

    // 소속 길드 추가
    public void addJoinedGuild(Guild guild) {
        this.joinedGuilds.add(guild);
        guild.joinNewCommunity(this);
    }

    // 소속 길드 삭제
    public void removeJoinedGuild(Guild guild) {
        this.joinedGuilds.remove(guild);
        guild.leaveJoinedCommunity();
    }


    //

}
