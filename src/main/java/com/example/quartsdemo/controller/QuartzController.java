package com.example.quartsdemo.controller;

import com.example.quartsdemo.entity.JobEntity;
import com.example.quartsdemo.service.QuartzService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Quartz 컨트롤러
 * 출처: https://www.cnblogs.com/ealenxie/p/9134602.html
 */
@Controller
@RequestMapping("/")
public class QuartzController {

    private static final Logger logger = LoggerFactory.getLogger(QuartzController.class);
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    @Autowired
    private QuartzService jobService;

    // 초기화 작업을 처리할 새로운 메서드
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent() {
        try {
            reStartAllJobs();
            logger.info("초기화 성공");
        } catch (SchedulerException e) {
            logger.info("초기화 예외: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ID를 기반으로 특정 Job을 재시작합니다.
    @RequestMapping("/refresh/{id}")
    public String refresh(@PathVariable Integer id) throws SchedulerException {
        String result;
        JobEntity entity = jobService.getJobEntityById(id);
        if (entity == null) {
            return "에러: 해당 ID가 존재하지 않습니다.";
        }
        synchronized (logger) {
            JobKey jobKey = jobService.getJobKey(entity);
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.pauseJob(jobKey);
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
            scheduler.deleteJob(jobKey);
            JobDataMap map = jobService.getJobDataMap(entity);
            JobDetail jobDetail = jobService.geJobDetail(jobKey, entity.getDescription(), map);
            if (entity.getStatus().equals("OPEN")) {
                scheduler.scheduleJob(jobDetail, jobService.getTrigger(entity));
                result = "Job 재시작: " + entity.getName() + "\t jarPath: " + entity.getJarPath() + " 성공 !";
            } else {
                result = "Job 재시작: " + entity.getName() + "\t jarPath: " + entity.getJarPath() + " 실패 ! , " +
                        "해당 Job 상태는 " + entity.getStatus() + "입니다.";
            }
        }
        return result;
    }

    /**
     * 모든 job을 다시 시작합니다.
     */
    private void reStartAllJobs() throws SchedulerException {

        synchronized (logger) {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());

            scheduler.pauseJobs(GroupMatcher.anyGroup()); // 모든 JOB 일시 정지

            for (JobKey jobKey : jobKeys) { // 데이터베이스에서 등록된 모든 JOB 삭제
                logger.info("jobKey==>" + jobKey + "\n");
                scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
                scheduler.deleteJob(jobKey);
            }

            // 데이터베이스에서 등록된 모든 JOB
            for (JobEntity job : jobService.loadJobs()) {
                logger.info("Job 등록: 이름 : {} , 그룹 : {} , 크론 : {}", job.getName(), job.getGroup(), job.getCron());

                JobDataMap map = jobService.getJobDataMap(job);
                JobKey jobKey = jobService.getJobKey(job);

                JobDetail jobDetail = jobService.geJobDetail(jobKey, job.getDescription(), map);
                if (job.getStatus().equals("OPEN")) {
                    scheduler.scheduleJob(jobDetail, jobService.getTrigger(job));
                } else {
                    logger.info("Job 실행 건너뜀: 이름 : {} , 해당 {} 상태는 {}입니다.", job.getName(), job.getName(), job.getStatus());
                }
            }
        }
    }

    @RequestMapping("/test/{id}")
    @ResponseBody
    public JobEntity testList(@PathVariable("id") int id) {
        JobEntity jobEntity = jobService.getById(id);

        return jobEntity;

    }

    @RequestMapping("/tablePage")
    public String quartzList() {
        return "table_page";
    }
}
