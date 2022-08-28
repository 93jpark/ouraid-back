package ouraid.ouraidback.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuildMember {
    @Id @GeneratedValue
    @Column(name = "guild_member_id")
    private Long guildMemberId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    private Guild guild;

    // 생성 메소드
    public static GuildMember createGuildMember() {
        GuildMember guildMember = new GuildMember();

        return guildMember;
    }
}
