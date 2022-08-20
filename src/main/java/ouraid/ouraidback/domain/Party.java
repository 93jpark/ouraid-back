package ouraid.ouraidback.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Party {
    @Id
    @GeneratedValue
    private Long id;

}
