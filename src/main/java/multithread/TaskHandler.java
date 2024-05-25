package multithread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serverlogic.ServerProgram;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;

public class TaskHandler {
    private TaskHandler() {}
    private static final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    public static void addTask(Runnable r) {
        ForkJoinTask<?> forkJoinTask = forkJoinPool.submit(r);
    }
    public static void start() {
        LoggerFactory.getLogger(ServerProgram.class.getSimpleName()).info("ForkJoinPool awaitQuiescence: {}", forkJoinPool.awaitQuiescence(100, TimeUnit.SECONDS));
    }
}
