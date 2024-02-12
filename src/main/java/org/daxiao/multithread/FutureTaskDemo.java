package org.daxiao.multithread;

import javafx.concurrent.Task;

import java.util.concurrent.*;

public class FutureTaskDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> integerFutureTask = new FutureTask<>(new Task());
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(integerFutureTask);
        Integer i = integerFutureTask.get();
        System.out.println(i);
    }

    private static class Task implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            TimeUnit.SECONDS.sleep(1);
            return ThreadLocalRandom.current().nextInt(10);
        }
    }
}
