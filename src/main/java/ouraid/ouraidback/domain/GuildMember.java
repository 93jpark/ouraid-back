package ouraid.ouraidback.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
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
}
