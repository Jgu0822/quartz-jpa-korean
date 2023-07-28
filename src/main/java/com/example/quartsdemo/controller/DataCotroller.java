package com.example.quartsdemo.controller;

import com.example.quartsdemo.entity.JobEntity;
import com.example.quartsdemo.entity.ResultBody;
import com.example.quartsdemo.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 여기에 관련된 작업 컨트롤러를 추가합니다.
 */
@RestController
@RequestMapping("/data")
@Slf4j
public class DataCotroller {

    @Autowired
    private DataService dataService;

    /**
     * 규정된 반환 형식
     * {
     *   "code": 200,
     *   "msg": "",
     *   "count": 10,
     *   "data": [{}, {}]
     * }
     * 여기에서 요청할 때 기본적으로 ?page=1&limit=10을 포함합니다.
     *
     * @return
     */
    @RequestMapping("/jobList")
    public ResultBody jobList(int page, int limit) {
        log.warn("page->" + page + "limit->" + limit);
        ResultBody all = dataService.getAllByPage(page, limit);
        return all;
    }

    /**
     * 추가
     */
    @RequestMapping("/addJob")
    public ResultBody addJob(JobEntity job) {
        log.info("job==>"+job);
        dataService.save(job);
        ResultBody all = dataService.getAll();
        return all;
    }

    /**
     * 관련된 시작 상태 변경
     */
    @RequestMapping("/updStatus")
    public String updStatus(boolean start, int id) {
        log.info("start==>" + start + "id==>" + id);
        String status = dataService.updStatus(start, id);
        return status;
    }

    /**
     * 업데이트할 데이터 반환
     */
    @RequestMapping("/getUpdData")
    public JobEntity getUpdData(int id) {
        JobEntity jobEntity = dataService.getUpdData(id);
        return jobEntity;
    }

    /**
     * 새로운 내용으로 수정
     */
    @RequestMapping("/updData")
    public ResultBody updData(JobEntity job) {
        dataService.updData(job);
        ResultBody all = dataService.getAll();
        return all;
    }

    /**
     * 데이터 삭제
     */
    @RequestMapping("/delJob")
    public void delJob(int id) {
        dataService.delJob(id);
    }
}
