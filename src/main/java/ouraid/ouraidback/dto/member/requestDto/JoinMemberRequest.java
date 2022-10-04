package ouraid.ouraidback.dto.member.requestDto;

import com.sun.istack.NotNull;
import lombok.Data;
import org.springframework.lang.Nullable;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Community;
import ouraid.ouraidback.domain.Guild;
import ouraid.ouraidback.domain.GuildMember;
import ouraid.ouraidback.domain.enums.Server;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Data
public class JoinMemberRequest {

    private String nickname;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Server server;

}
