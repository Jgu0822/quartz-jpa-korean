package com.example.quartsdemo.test;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @DisallowConcurrentExecution
 * 이 플래그는 이 클래스에 대한 동시 액세스를 허용하지 않음을 의미합니다.
 *
 * @PersistJobDataAfterExecution
 * @DisallowConcurrentExecution과 함께 사용하는 것이 좋습니다. 데이터 일관성 문제를 방지하기 위해 JobDetail의 JobDataMap 데이터를 execute 메서드를 성공적으로 실행한 후에도 업데이트합니다.
 */
public class QuartzTest {

    public static void main(String[] args) throws SchedulerException {
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

        // 트리거 설정
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(
                        SimpleScheduleBuilder
                                .simpleSchedule()
                                .withIntervalInSeconds(2)
                                .repeatForever()
                ).build();

        // 최종적으로 우리의 job을 트리거의 설정에 따라 실행
        scheduler.scheduleJob(job, trigger);
        // scheduler.shutdown();
    }
}
