package ouraid.ouraidback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ouraid.ouraidback.domain.*;
import ouraid.ouraidback.domain.enums.MainClass;
import ouraid.ouraidback.domain.enums.RecruitType;
import ouraid.ouraidback.domain.enums.Server;
import ouraid.ouraidback.domain.enums.SubClass;
import ouraid.ouraidback.domain.party.HardLotus;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
        log.info("initialize database with mock data");
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit() {
            Member mA = Member.create("유니츠", "units@test.com", "password", Server.SHUSIA);
            Member mB = Member.create("블링벨", "bling@test.com", "password", Server.SHUSIA);
            Member mC = Member.create("아이리", "ire@test.com", "password", Server.SHUSIA);
            Member mD = Member.create("떼바", "thevaD@test.com", "password", Server.SHUSIA);
            em.persist(mA);
            em.persist(mB);
            em.persist(mC);
            em.persist(mD);

            Characters ca1 = Characters.create(Server.SHUSIA, "유니츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.2, mA);
            Characters ca2 = Characters.create(Server.SHUSIA, "유니쯔", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.6, mA);
            Characters ca3 = Characters.create(Server.SHUSIA, "유닛츠", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.4, mA);
            Characters ca4 = Characters.create(Server.SHUSIA, "유니처", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.3, mA);
            em.persist(ca1);
            em.persist(ca2);
            em.persist(ca3);
            em.persist(ca4);

            Characters cb1 = Characters.create(Server.SHUSIA, "블링벨", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 2.0, mB);
            Characters cb2 = Characters.create(Server.SHUSIA, "블링블루", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.9, mB);
            Characters cb3 = Characters.create(Server.SHUSIA, "블링붐", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.8, mB);
            Characters cb4 = Characters.create(Server.SHUSIA, "블링화", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.6, mB);
            em.persist(cb1);
            em.persist(cb2);
            em.persist(cb3);
            em.persist(cb4);

            Characters cc1 = Characters.create(Server.SHUSIA, "아이리크루", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.1, mC);
            Characters cc2 = Characters.create(Server.SHUSIA, "아이리수라", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.7, mC);
            Characters cc3 = Characters.create(Server.SHUSIA, "아이리넨", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 3.2, mC);
            Characters cc4 = Characters.create(Server.SHUSIA, "아이리수라일", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 2.2, mC);
            em.persist(cc1);
            em.persist(cc2);
            em.persist(cc3);
            em.persist(cc4);

            Characters cd1 = Characters.create(Server.SHUSIA, "떼바", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.1, mD);
            Characters cd2 = Characters.create(Server.SHUSIA, "떼바1", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 1.7, mD);
            Characters cd3 = Characters.create(Server.SHUSIA, "떼바2", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 3.2, mD);
            Characters cd4 = Characters.create(Server.SHUSIA, "떼바3", MainClass.FEMALE_GHOST_KNIGHT, SubClass.SWORD_MASTER, 2.2, mD);
            em.persist(cd1);
            em.persist(cd2);
            em.persist(cd3);
            em.persist(cd4);

            Community comA = Community.create(Server.SHUSIA, "void", mA);
            em.persist(comA);


            Guild gA = Guild.create(Server.SHUSIA, "void", 3, mA, comA);
            Guild gB = Guild.create(Server.SHUSIA, "명일방주", 3, mA, comA);
            Guild gC = Guild.create(Server.SHUSIA, "이타적삶", 3, mA, comA);


            em.persist(gA);
            em.persist(gB);
            em.persist(gC);

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

            comA.addJoinedGuild(gA);
            comA.addJoinedGuild(gB);
            comA.addJoinedGuild(gC);


            GuildMember gm1 = GuildMember.createGuildMember(gA, mA);
            GuildMember gm2 = GuildMember.createGuildMember(gC, mA);
            GuildMember gm3 = GuildMember.createGuildMember(gB, mC);
            GuildMember gm4 = GuildMember.createGuildMember(gC, mC);
            GuildMember gm5 = GuildMember.createGuildMember(gA, mD);
            GuildMember gm6 = GuildMember.createGuildMember(gB, mD);
            gm1.setJoinedDate(Instant.now());
            gm2.setJoinedDate(Instant.now());
            gm3.setJoinedDate(Instant.now());
            gm4.setJoinedDate(Instant.now());
            gm5.setJoinedDate(Instant.now());
            gm6.setJoinedDate(Instant.now());

            em.persist(gm1);
            em.persist(gm2);
            em.persist(gm3);
            em.persist(gm4);
            em.persist(gm5);
            em.persist(gm6);

            mA.setJoinedCommunity(comA);
            mB.setJoinedCommunity(comA);
            mC.setJoinedCommunity(comA);
            mD.setJoinedCommunity(comA);

            ca1.joinNewCommunity(comA);
            ca2.joinNewCommunity(comA);
            ca3.joinNewCommunity(comA);
            ca4.joinNewCommunity(comA);

            cb1.joinNewCommunity(comA);
            cb2.joinNewCommunity(comA);
            cb3.joinNewCommunity(comA);
            cb4.joinNewCommunity(comA);

            cc1.joinNewCommunity(comA);
            cc2.joinNewCommunity(comA);
            cc3.joinNewCommunity(comA);
            cc4.joinNewCommunity(comA);

            cd1.joinNewCommunity(comA);
            cd2.joinNewCommunity(comA);
            cd3.joinNewCommunity(comA);
            cd4.joinNewCommunity(comA);


            HardLotus hlp = HardLotus.createHardLotusParty(RecruitType.OPEN, Server.SHUSIA, mA, ca1, LocalDate.now());
            em.persist(hlp);


        }

    }
}
