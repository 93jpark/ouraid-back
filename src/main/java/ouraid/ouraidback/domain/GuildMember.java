package ouraid.ouraidback.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuildMember {
    @Id @GeneratedValue
    @Column(name = "guild_member_id")
    private Long guildMemberId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "guild_id")
    private Guild guild;

    @NotNull
    private Instant joinedDate;

    // 생성 메소드
    public static GuildMember createGuildMember(Guild guild, Member member) {
        GuildMember guildMember = new GuildMember();
        guildMember.guild = guild;
        guildMember.member = member;
        return guildMember;
    }

    public void setJoinedDate(Instant time) {
        this.joinedDate = time;
    }
}
