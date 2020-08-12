import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;

public class GetAllThreadInfo {
    public static void main(String[] args) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] threadIds = threadMXBean.getAllThreadIds();
        Arrays.stream(threadMXBean.getThreadInfo(threadIds)).map(t->t.getThreadName()).forEach(System.out::println);
    }
}
