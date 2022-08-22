package ouraid.ouraidback.Repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class GuildRepository {

    private final EntityManager em;

}
