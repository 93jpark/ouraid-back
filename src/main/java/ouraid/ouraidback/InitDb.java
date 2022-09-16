package ouraid.ouraidback;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.Characters;
import ouraid.ouraidback.domain.Community;
import ouraid.ouraidback.domain.Guild;
import ouraid.ouraidback.domain.Member;
import ouraid.ouraidback.domain.enums.MainClass;
import ouraid.ouraidback.domain.enums.Server;
import ouraid.ouraidback.domain.enums.SubClass;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

//@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initDb();
    }


    @Transactional
    @Component
    @RequiredArgsConstructor
    private class InitService {

        private final EntityManager em;

        public void initDb() {
//            Member member = createMember("userA", "서울", "1", "1111");
//            em.persist(member);
//
//            Book book1 = createBook("JPA1 BOOK", 10000, 100);
//            em.persist(book1);
//
//            Book book2 = createBook("JPA2 BOOK", 20000, 100);
//            em.persist(book2);
//
//            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
//            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
//
//            Delivery delivery = createDelivery(member.getAddress());
//
//            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
//            em.persist(order);
            Member mA = Member.create("userA", "userA@test.com", "1111", Server.SHUSIA);
            Member mB = Member.create("userB", "userB@test.com", "1111", Server.SHUSIA);
            Member mC = Member.create("userC", "userC@test.com", "1111", Server.SHUSIA);
            Member mD = Member.create("userD", "userD@test.com", "1111", Server.SHUSIA);
            em.persist(mA);
            em.persist(mB);
            em.persist(mC);
            em.persist(mD);

            Characters ca1 = Characters.create(Server.SHUSIA, "charA1", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.2, mA);
            Characters ca2 = Characters.create(Server.SHUSIA, "charA2", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.6, mA);
            Characters ca3 = Characters.create(Server.SHUSIA, "charA3", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.4, mA);
            Characters ca4 = Characters.create(Server.SHUSIA, "charA4", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.3, mA);
            em.persist(ca1);
            em.persist(ca2);
            em.persist(ca3);
            em.persist(ca4);

            Characters cb1 = Characters.create(Server.SHUSIA, "charB1", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 2.0, mB);
            Characters cb2 = Characters.create(Server.SHUSIA, "charB2", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.9, mB);
            Characters cb3 = Characters.create(Server.SHUSIA, "charB3", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, mB);
            Characters cb4 = Characters.create(Server.SHUSIA, "charB4", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.6, mB);
            em.persist(cb1);
            em.persist(cb2);
            em.persist(cb3);
            em.persist(cb4);

            Characters cc1 = Characters.create(Server.SHUSIA, "charC1", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.1, mC);
            Characters cc2 = Characters.create(Server.SHUSIA, "charC2", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.7, mC);
            Characters cc3 = Characters.create(Server.SHUSIA, "charC3", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 3.2, mC);
            Characters cc4 = Characters.create(Server.SHUSIA, "charC4", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 2.2, mC);
            em.persist(cc1);
            em.persist(cc2);
            em.persist(cc3);
            em.persist(cc4);

            Characters cd1 = Characters.create(Server.SHUSIA, "charD1", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.1, mD);
            Characters cd2 = Characters.create(Server.SHUSIA, "charD2", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.7, mD);
            Characters cd3 = Characters.create(Server.SHUSIA, "charD3", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 3.2, mD);
            Characters cd4 = Characters.create(Server.SHUSIA, "charD4", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 2.2, mD);
            em.persist(cd1);
            em.persist(cd2);
            em.persist(cd3);
            em.persist(cd4);

            Community comA = Community.create(Server.SHUSIA, "comA", mA);

            Guild gA = Guild.create(Server.SHUSIA, "guild_A", 3, mA, comA);
            Guild gB = Guild.create(Server.SHUSIA, "guild_B", 3, mA, comA);
            Guild gC = Guild.create(Server.SHUSIA, "guild_C", 3, mA, comA);

            ca1.joinNewGuild(gA);
            ca2.joinNewGuild(gA);
            ca3.joinNewGuild(gB);
            ca4.joinNewGuild(gB);

            cb1.joinNewGuild(gA);
            cb2.joinNewGuild(gA);
            cb3.joinNewGuild(gC);
            cb4.joinNewGuild(gC);

            cc1.joinNewGuild(gB);
            cc2.joinNewGuild(gB);
            cc3.joinNewGuild(gC);
            cc4.joinNewGuild(gC);

            cd1.joinNewGuild(gA);
            cd2.joinNewGuild(gA);
            cd3.joinNewGuild(gB);
            cd4.joinNewGuild(gB);


















        }

    }
}
