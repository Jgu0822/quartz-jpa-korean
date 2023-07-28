import org.quartz.*;

// 이것은 Job의 구체적인 내용을 정의하는 클래스입니다.
public class HelloJob implements Job {
    public HelloJob() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        /**
         * JobExecutionContext에는
         * trigger의 참조, JobDetail 객체의 참조 및 기타 정보가 포함됩니다.
         */
        System.out.println("job1이 시작되었습니다.");

        JobKey key = context.getJobDetail().getKey();

        // JobDataMap을 사용하여 Job에서 사용할 데이터를 가져옵니다.
        JobDataMap jobDataMap = context.getMergedJobDataMap();

        String jobSays = jobDataMap.getString("myJob");
        float myFloatValue = jobDataMap.getFloat("myFloatValue");

        // 예를 들어, JobDataMap에 추가 데이터를 넣고 사용할 수도 있습니다.
        // ArrayList state = (ArrayList)jobDataMap.get("myStateData");
        // state.add(new Date());

        System.err.println("HelloJob의 인스턴스 " + key + "이(가) 말합니다: " + jobSays + ", and val is: " + myFloatValue);
    }
}
