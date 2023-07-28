package com.example.quartsdemo.repository;

import com.example.quartsdemo.entity.JobEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.ui.Model;

/**
 * JPA 를 사용하여 식을 쿼리하는 인터페이스
 */
public interface JobEntityRepository extends CrudRepository<JobEntity,Long> {
     JobEntity getById(Integer id);
     Page<JobEntity> findAll(Specification<Model> sp, Pageable pageable);

//    @Modifying
//    @Query("UPDATE job_entity c\n" +
//            "SET c.name = :job.name,\n" +
//            " c.group = :job.group,\n" +
//            " c.cron = :job.cron,\n" +
//            " c.parameter = :job.parameter,\n" +
//            " c.description = :job.description,\n" +
//            " c.vm_param = :job.vm_param,\n" +
//            " c.jar_path = :job.jar_path,\n" +
//            " c.status = status\n" +
//            "WHERE\n" +
//            "\tc.id = :job.id")
//     void updateJob(JobEntity job);

}
