import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通常時と ShutdownHook する時でスレッドがどの様に違うのかを確認する。
 * 実行例：
 * 元々はあったけどShutdownHookの時には無いスレッド
 * main
 * ShutdownHookの時にできたスレッド
 * DestroyJavaVM
 * ShutdownHook Thread
 */
public class DiffShutdownHook {

    public static void main(String[] args) {

        List<String> threadNames = new ArrayList<>();

        Runnable shutdownHook = ()->{
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            long[] threadIds = threadMXBean.getAllThreadIds();
            List<String> threadNamesInHook = Arrays.stream(threadMXBean.getThreadInfo(threadIds)).map(t -> t.getThreadName()).collect(Collectors.toList());

            System.out.println("元々はあったけどShutdownHookの時には無いスレッド");
            threadNames.stream().filter(n->!threadNamesInHook.contains(n)).forEach(System.out::println);

            System.out.println("ShutdownHookの時にできたスレッド");
            threadNamesInHook.stream().filter(n->!threadNames.contains(n)).forEach(System.out::println);
        };

        Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook, "ShutdownHook Thread"));

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long[] threadIds = threadMXBean.getAllThreadIds();

        threadNames.addAll(Arrays.stream(threadMXBean.getThreadInfo(threadIds)).map(t->t.getThreadName()).collect(Collectors.toList()));
    }
}
