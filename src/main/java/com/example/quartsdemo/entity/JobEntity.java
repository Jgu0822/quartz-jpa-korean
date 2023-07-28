package com.example.quartsdemo.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "JOB_ENTITY")
@Data
public class JobEntity implements Serializable {
    private static final long serialVersionUID = -5974699134574143016L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;          // 작업 이름
    @Column(name = "`group`", nullable = true)
    private String group;         // 작업 그룹명
    private String cron;          // 실행할 Cron 표현식
    private String parameter;     // 작업 파라미터
    private String description;   // 작업 설명
    @Column(name = "vm_param")
    private String vmParam;       // VM 파라미터
    @Column(name = "jar_path")
    private String jarPath;       // 작업의 JAR 파일 경로, 여기서는 주기적으로 실행 가능한 JAR 파일을 선택함
    private String status;        // 작업 실행 상태, "OPEN" 또는 "CLOSE"로 설정되며, "OPEN"일 때에만 해당 작업이 실행됨

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "`group`", nullable = true)
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVmParam() {
        return vmParam;
    }

    public void setVmParam(String vmParam) {
        this.vmParam = vmParam;
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
