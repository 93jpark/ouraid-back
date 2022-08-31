package ouraid.ouraidback;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

//@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @Transactional
    //@Component
    @RequiredArgsConstructor
    private class InitService {
        private final EntityManager em;

        public void initDb() {

        }

    }
}
