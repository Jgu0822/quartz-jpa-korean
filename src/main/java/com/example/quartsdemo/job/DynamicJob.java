package com.example.quartsdemo.job;

import com.example.quartsdemo.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @DisallowConcurrentExecution: 이 주석은 Job을 구현하는 클래스에 사용되며, 동시 실행을 허용하지 않음을 의미합니다.
 * 주의: org.quartz.threadPool.threadCount 스레드 풀의 스레드 수는 적어도 하나 이상이어야 합니다.
 * 그렇지 않으면 @DisallowConcurrentExecution이 작동하지 않습니다.
 * 예를 들어 Job의 설정된 시간 간격이 3초이고 Job 실행 시간이 5초인 경우,
 * @DisallowConcurrentExecution 설정을 하면 프로그램이 작업이 완료된 후에 다음 작업을 실행하며,
 * 그렇지 않으면 3초 후에 새로운 스레드를 시작하여 실행합니다.
 */
@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution // 예외가 없을 때 데이터를 업데이트합니다. 사용 가능한가요?
@Component

//@Configuration // Spring Batch Test
public class DynamicJob implements Job {
    private Logger logger = LoggerFactory.getLogger(DynamicJob.class);

    /**
     * 쿼츠 Job의 실제 실행 로직인 핵심 메서드입니다.
     * executorContext에는 Quartz가 실행하는 데 필요한 모든 정보가 포함되어 있습니다.
     * @throws JobExecutionException execute() 메서드는 JobExecutionException 예외만 던질 수 있습니다.
     */
    @Override
    public void execute(JobExecutionContext executionContext) throws JobExecutionException {

        JobDataMap map = executionContext.getMergedJobDataMap();

        String jarPath = map.getString("jarPath");
        String parameter = map.getString("parameter");
        String vmParam = map.getString("vmParam");
        logger.info("Running Job name : {} ", map.getString("name"));
        logger.info("Running Job description : " + map.getString("JobDescription"));
        logger.info("Running Job group: {} ", map.getString("group"));
        logger.info("Running Job cron : " + map.getString("cronExpression"));
        logger.info("Running Job jar path : {} ", jarPath);
        logger.info("Running Job parameter : {} ", parameter);
        logger.info("Running Job vmParam : {} ", vmParam);

        long startTime = System.currentTimeMillis();
        if (!StringUtils.getStringUtil.isEmpty(jarPath)) {

            File jar = new File(jarPath);
            if (jar.exists()) {
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(jar.getParentFile());
                /**
                 * 이것은 java 실행 명령입니다.
                 * java -jar
                 */
                List<String> commands = new ArrayList<>();
                commands.add("java");
                if (!StringUtils.getStringUtil.isEmpty(vmParam)) {
                    commands.add(vmParam);
                }
                commands.add("-jar");
                commands.add(jarPath);
                commands.add(" &");
                System.out.println("commands->\n" + commands);

                if (!StringUtils.getStringUtil.isEmpty(parameter)) {

                    commands.add(parameter);
                    processBuilder.command(commands);
                    logger.info("Running Job details as follows >>>>>>>>>>>>>>>>>>>>: ");
                    logger.info("Running Job commands : {}  ", StringUtils.getStringUtil.getListString(commands));
                    try {
                        Process process = processBuilder.start();
                        logProcess(process.getInputStream(), process.getErrorStream());

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                } else {

                    throw new JobExecutionException("Job Jar not found >>  " + jarPath);
                }

                long endTime = System.currentTimeMillis();
                logger.info(">>>>>>>>>>>>> Running Job has been completed , cost time :  " + (endTime - startTime) + "ms\n");
            }
        }
    }

    // Job 실행 내용을 로그로 기록합니다.
    private void logProcess(InputStream inputStream, InputStream errorStream) throws IOException {
        String inputLine;
        String errorLine;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        while ((inputLine = inputReader.readLine()) != null){
            logger.info(inputLine);
        }
        while ((errorLine = errorReader.readLine()) != null) {
            logger.error(errorLine);
        }
    }
}
