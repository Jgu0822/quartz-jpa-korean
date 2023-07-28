package com.example.quartsdemo.service.impl;

import com.example.quartsdemo.entity.JobEntity;
import com.example.quartsdemo.job.DynamicJob;
import com.example.quartsdemo.repository.JobEntityRepository;
import com.example.quartsdemo.service.QuartzService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private JobEntityRepository repository;

    @Override
    public JobEntity getById(int id) {
        return repository.getById(id);
    }

    // ID로 JobEntity 조회
    public JobEntity getJobEntityById(Integer id) {
        return repository.getById(id);
    }

    // 데이터베이스에서 모든 JobEntity 조회
    public List<JobEntity> loadJobs() {
        List<JobEntity> list = new ArrayList<>();
        repository.findAll().forEach(list::add);
        return list;
    }

    // Job 의 파라미터 정보를 JobDataMap 으로 가져옴
    public JobDataMap getJobDataMap(JobEntity job) {
        JobDataMap map = new JobDataMap();
        map.put("name", job.getName());
        map.put("group", job.getGroup());
        map.put("cronExpression", job.getCron());
        map.put("parameter", job.getParameter());
        map.put("JobDescription", job.getDescription());
        map.put("vmParam", job.getVmParam());
        map.put("jarPath", job.getJarPath());
        map.put("status", job.getStatus());
        return map;
    }

    // JobDetail 을 가져옴 (JobDetail 은 작업의 정의, Job 은 작업의 실행 로직을 포함하며 JobDetail 은 Job Class 를 참조하여 정의함)
    public JobDetail geJobDetail(JobKey jobKey, String description, JobDataMap map) {
        return JobBuilder.newJob(DynamicJob.class)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(map)
                .storeDurably()
                .build();
    }

    // Trigger 를 가져옴 (Job의 트리거, 실행 규칙을 정의함)
    public Trigger getTrigger(JobEntity job) {
        return TriggerBuilder.newTrigger()
                .withIdentity(job.getName(), job.getGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
                .build();
    }

    // JobKey 를 가져옴 (Name과 Group을 포함함)
    public JobKey getJobKey(JobEntity job) {
        return JobKey.jobKey(job.getName(), job.getGroup());
    }

    /**
     * 아래는 데이터 조회에 대한 작업
     */

}
