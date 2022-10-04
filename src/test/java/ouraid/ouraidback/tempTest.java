package ouraid.ouraidback;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class tempTest {
    @Test
    public void 날짜테스트() throws Exception {
        //given
        LocalDateTime a = LocalDateTime.now();
        log.info("Day Of Year {}", a.getDayOfYear());
        log.info("Day Of Month {}", a.getDayOfMonth());
        log.info("Day Of Month {} test", a.getDayOfMonth());
        log.info("Day Of week  {}", a.getDayOfWeek().equals(a.getDayOfWeek()));
        log.info("Day Of week  {}", a.getDayOfWeek().getClass().getSimpleName());
        log.info("get month {}", a.getMonth());


        LocalDate b = LocalDate.now();
        log.info("LocalDate.now() : {}", b);
        log.info("LocalDate.getDayOfMonth: {} 일", b.getDayOfMonth()); // 일
        log.info("LocalDate.getMonthValue: {} 월", b.getMonthValue()); // 월
        log.info("LocalDate.getDayOfYear: {} 년", b.getYear()); // 년


        Instant c = Instant.now();
        log.info("Instant.now() : {}", c);
        //log.info("Instant.now().getDayOfMonth() : {}", c.get);
        //log.info("Instant.now().getYear() : {}", c.getYear());
        //log.info("Instant.now().getMonthValue() : {}", c.getMonthValue());

        Instant test = Instant.parse("2022-09-29T19:00:00.00Z");
        System.out.println(test.toString());

        Instant aaa = Instant.parse("2022-09-29T19:30:00.00Z");
        Instant bbb = Instant.parse("2022-09-22T19:00:00.00Z");

        System.out.println(test.compareTo(test)); // test와 같은 시간 0
        System.out.println(test.compareTo(aaa)); // test이후라면 -1
        System.out.println(test.compareTo(bbb)); // test이전이라면 1




        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        int month = localDate.getMonthValue();
        System.out.println("month:"+month);




    }
}
