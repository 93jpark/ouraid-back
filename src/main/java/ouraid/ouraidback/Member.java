package ouraid.ouraidback;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Member {
    @Id
    private Long id;

    private String username;

    private Integer age;
}
