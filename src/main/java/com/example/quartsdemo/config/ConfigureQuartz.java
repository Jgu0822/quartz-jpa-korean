package com.example.quartsdemo.config;

import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by   on 2019/5/22 11:02
 * Quartz 핵심 구성 클래스
 */
@Configuration
public class ConfigureQuartz {

    //Batch JobFactory
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * SchedulerFactoryBean 이라는 클래스의 진정한 역할은 org.quartz.Scheduler 는 생성 및 구성되며 Spring 과 동기화되는 수명 주기를 관리합니다.
     * org.quartz.Scheduler: 스케줄러.모든 스케줄은 그것에 의해 제어됩니다.
     * @param dataSource SchedulerFactory 데이터 원본 설정
     * @param jobFactory SchedulerFactory Batch JobFactory
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        // 선택적으로, QuartzScheduler 가 시작할 때 존재하는 Job 을 업데이트합니다.
        // 따라서 targetObject 를 수정할 때마다 qrtz_job_details 테이블을 삭제하지 않아도 됩니다.
        factory.setOverwriteExistingJobs(true);
        factory.setAutoStartup(true); // 오토 스타트 설정
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }

    // Quartz.properties 파일에서 Quartz 설정 속성 읽기
    @Bean
    public Properties quartzProperties() throws IOException {

        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();

        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));

        propertiesFactoryBean.afterPropertiesSet();

        return propertiesFactoryBean.getObject();
    }


    // JobFactory 설정, quartz 작업에 자동 연결 지원 추가
    public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements
            ApplicationContextAware {

        private transient AutowireCapableBeanFactory beanFactory;

        @Override
        public void setApplicationContext(final ApplicationContext context) {
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }
    }


}
