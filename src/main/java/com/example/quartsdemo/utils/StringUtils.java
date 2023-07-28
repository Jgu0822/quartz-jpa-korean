package com.example.quartsdemo.utils;

import java.util.List;
import java.util.Map;

/**
 * 사용자 정의 열거형 싱글톤 객체 StringUtil
 */
public enum StringUtils {
    getStringUtil;
    // 문자열이 비어있는지 확인합니다.
    public boolean isEmpty(String str) {
        return (str == null) || (str.length() == 0) || (str.equals(""));
    }
    // 문자열의 공백을 제거합니다.
    public String trim(String str) {
        return str == null ? null : str.trim();
    }
    // Map 의 값들을 문자열로 반환합니다.
    public String getMapString(Map<String, String> map) {
        String result = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            result += entry.getValue() + " ";
        }
        return result;
    }
    // List 의 값들을 문자열로 반환합니다.
    public String getListString(List<String> list) {
        String result = "";
        for (String s : list) {
            result += s + " ";
        }
        return result;
    }
}
