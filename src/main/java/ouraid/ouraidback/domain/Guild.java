package ouraid.ouraidback.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guild {
    @Id @GeneratedValue @Column(name="guild_id") private Long id;

    @NotNull private String name;

    @NotNull private int level;

    @Enumerated(EnumType.STRING) @NotNull private Server server;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "community_id") private Community joinedCommunity;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "member_id") @NotNull private Member guildMaster;

    @OneToMany(mappedBy="member") private List<GuildMember> guildMembers = new ArrayList<>();

    @OneToMany(mappedBy = "joinedGuild") private List<Characters> guildCharacters = new ArrayList<>();

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

    // 길드 멤버 가입
    public void addGuildMember(GuildMember guildMember) {
        guildMembers.add(guildMember);
        guildMember.setGuild(this);
    }

    // 길드 캐릭터 가입
    public void addGuildCharacter(Characters character) {
        this.guildCharacters.add(character);
    }


    // 길드 캐릭터 탈퇴
    public void leaveGuildByCharacter(Characters character) {
        this.guildCharacters.remove(character);
    }

    // 길드 멤버 탈퇴
    public void leaveGuildByMember(GuildMember guildMember) {
        this.guildMembers.remove(guildMember);
        //member.leaveJoinedGuild(this);
    }

    // 길드 마스터 변경
    public void changeGuildMaster(Member member) {
        this.guildMaster = member;
    }

    // 커뮤니티 설정
    public void joinNewCommunity(Community community) {
        this.joinedCommunity = community;
    }

    // 커뮤니티 해지
    public void leaveJoinedCommunity() {
        this.joinedCommunity = null;
    }

}
