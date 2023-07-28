package com.example.quartsdemo.entity;

import lombok.Data;

import java.util.List;

/**
 * layui에 사용되는 반환 형식
 */
@Data
public class ResultBody {

    private int code;                // 상태 코드
    private String msg;              // 메시지
    private long count;              // 데이터 총 갯수
    private List<JobEntity> data;    // 작업 엔티티 리스트

    public ResultBody() {
    }

    public ResultBody(int code, String msg, long count, List<JobEntity> data) {
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }
}
