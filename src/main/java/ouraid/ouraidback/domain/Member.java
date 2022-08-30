package ouraid.ouraidback.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue @Column(name="member_id") private Long id;

    @NotNull @Column(unique = true) private String nickname;

    @NotNull @Column(unique = true) private String email;

    @NotNull private String password;

    @NotNull private Boolean availability = true;

    @Enumerated(EnumType.STRING) @NotNull private Server server;

    @OneToOne(mappedBy = "communityMaster", fetch = LAZY, cascade = PERSIST) @Nullable private Community ownCommunity;

    @ManyToOne(fetch = LAZY) @JoinColumn(name = "community_id") private Community joinedCommunity;

    @OneToMany(mappedBy = "guildMaster", cascade = PERSIST) @Nullable private List<Guild> ownGuilds = new ArrayList<>();

    @OneToMany(mappedBy="guild", cascade = PERSIST) @Nullable private List<GuildMember> joinedGuilds = new ArrayList<>();

    // 소유캐릭 삭제 시 캐릭 엔티티 삭제
    @OneToMany(mappedBy = "characterOwner", cascade = ALL, orphanRemoval = true) private List<Character> ownCharacters = new ArrayList<>();


    // 생성 메소드

    /**
     * 기본정보 바탕 Member 생성
     * @param nickname
     * @param email
     * @param password
     * @param server
     * @return
     */
    public static Member create(String nickname, String email, String password, Server server) {
        Member member = new Member();

        member.nickname = nickname;
        member.email = email;
        member.password = password;
        member.server = server;

        return member;
    }

    public Member create(String nickname, String email, String password, Server server, Object dummy) {
        Member member = new Member();

        member.nickname = nickname;
        member.email = email;
        member.password = password;
        member.server = server;

        return member;
    }

    // 비즈니스 로직

    /**
     * 소유 캐릭터 추가
     */
    public void addOwnCharacter(Character character) {
        this.ownCharacters.add(character);

    }

    public void addOwnCharacters(List<Character> characters) {
        for(Character c : characters) {
            this.ownCharacters.add(c);
        }
    }

    /**
     * 소유 캐릭터 삭제
     */
    public void removeOwnCharacter(Character character) {
        this.ownCharacters.remove(character);
    }

    /**
     * 소유길드 추가
     */
    public void addOwnGuild(Guild guild) {
        this.ownGuilds.add(guild);
    }

    /**
     * 소유길드 삭제
     */
    public void removeOwnGuild(Guild guild) {
        this.ownGuilds.remove(guild);
    }

    /**
     * 회원상태 변경
     */
    public void changeMemberStatus() {
        this.availability = !this.availability;
    }

    // 멤버 길드 탈퇴


    // 커뮤니티 마스터 멤버 설정
    public void setCommunityMaster(Community community) {
        this.ownCommunity = community;
        community.changeMaster(this);
    }

    // 커뮤니티 마스터 멤버 해제
    public void unsetCommunityMaster() {
        this.ownCommunity = null;
    }


    // 연관관계 편입 메소드
    public void addJoinedGuild(GuildMember guildMember) {
        joinedGuilds.add(guildMember);
        guildMember.setMember(this);
    }

    public void leaveJoinedGuild(GuildMember guildMember) {
        this.joinedGuilds.remove(guildMember);
    }


}
