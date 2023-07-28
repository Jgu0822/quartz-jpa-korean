package com.example.quartsdemo.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.HolidayCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;

/**
 * @DisallowConcurrentExecution
 * 이 플래그는 이 클래스에 대한 동시 액세스를 허용하지 않음을 의미합니다.
 *
 * @PersistJobDataAfterExecution
 * @DisallowConcurrentExecution과 함께 사용하는 것이 좋습니다. 데이터 일관성 문제를 방지하기 위해 JobDetail의 JobDataMap 데이터를 실행 후에도 업데이트합니다.
 */
public class QuartzCalendarTest {

    public static void main(String[] args) throws SchedulerException, ParseException {
        // 스케줄러 생성
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        List<Date> dateList = new ArrayList<>();

        // 우리가 정의한 DumbJob을 사용하여 관련 호출 수행
        JobDetail job = JobBuilder.newJob(DumbJob.class)
                .withIdentity("job1", "group1")
                .usingJobData("myJob","Hello world!")
                .usingJobData("jobSays","Hello world!")
                .usingJobData("myFloatValue",3.14F )
                .build();

        // 휴일 캘린더 생성 및 추가
        HolidayCalendar cal =  new HolidayCalendar();
        cal.addExcludedDate(new Date("2019/5/13"));
        scheduler.addCalendar("myHolidays", cal, false, false);

        Trigger t = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .forJob("job1")
                .withSchedule(
                        dailyAtHourAndMinute(9,30)
                )
                .build();

        Trigger t2 = TriggerBuilder.newTrigger()
                .withIdentity("myTrigger2")
                .forJob("HelloJob2")
                .withSchedule(
                        dailyAtHourAndMinute(11,30)
                )
                .build();

        // 트리거 설정
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(
                        cronSchedule("0 42 10 * * ?")
                )
                .build();

        // Job의 Listener 설정
//        scheduler.getListenerManager().addJobListener(myJobListener, jobKeyEquals(jobKey("myJobName", "myJobGroup")));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date parse = df.parse("2019-05-21 15:59:10");

        // SimpleTrigger
        SimpleTrigger simpleTrigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                .withIdentity("simpleTriger","group1")
                .startAt(parse)
                .forJob("job1","group1")
                .build();

        // 최종적으로 우리의 job을 설정된 트리거에 따라 실행
        scheduler.scheduleJob(job, simpleTrigger);
    }
}
