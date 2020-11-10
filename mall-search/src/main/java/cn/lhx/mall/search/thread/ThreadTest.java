package cn.lhx.mall.search.thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author lee549
 * @date 2020/10/23 21:40
 */
@Data
@AllArgsConstructor
class Student {
    String name;
    int age;

}

public class ThreadTest {
    public static ExecutorService service = Executors.newFixedThreadPool(10);
    /**
     * 初始化线程的4种方式
     * 1.继承Thread
     *          Thread01 thread01 = new Thread01();
     *         thread01.start();
     *         System.out.println("main方法结束");
     * 2.实现runnable接口
     *                Runable01 runable01 = new Runable01();
     *         new Thread(runable01).start();
     *         System.out.println("main方法结束");
     * 3.实现callable+futuretask（可以拿到返回值结果，可以处理异常）
     *          FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
     *         new Thread(futureTask).start();
     *
     *         //阻塞等待获取返回结果
     *         Integer integer = futureTask.get();
     *         System.out.println("main方法结束 "+integer);
     * 4.线程池
     *          ExecutorService service = Executors.newFixedThreadPool(10);
     *                  service.execute(new Runable01());
     */
    /**
     * 异步编排1.runAsync 无返回值
     * CompletableFuture.runAsync(()->{
     * System.out.println("当前线程:"+Thread.currentThread().getId());
     * int i= 10/2;
     * System.out.println("运行结果:"+i);
     * },service);
     * <p>
     * 异步编排2. 有返回值 可获取异常信息等
     * CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
     * System.out.println("当前线程:" + Thread.currentThread().getId());
     * int i = 10 / 2;
     * System.out.println("运行结果:" + i);
     * return i;
     * }, service);
     * System.out.println("main 方法end"+future.get());
     * <p>
     * 2.1 异常
     * <p>
     * CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
     * System.out.println("当前线程:" + Thread.currentThread().getId());
     * int i = 10 / 0;
     * System.out.println("运行结果:" + i);
     * return i;
     * }, service).whenCompleteAsync((res,execption)->{
     * System.out.println("异步任务完成。。结果为："+res+"异常是："+execption);
     * }).exceptionally(throwable -> {
     * //可以感知异常同时返回默认值
     * return 10;
     * });
     * System.out.println("main方法end"+future.get());
     * 2.2
     * handle,方法执行完的后续处理
     * CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
     * System.out.println("当前线程:" + Thread.currentThread().getId());
     * int i = 10 / 2;
     * System.out.println("运行结果:" + i);
     * return i;
     * }, service).handle((res,throwable)->{
     * if (res!=null){
     * return res*2;
     * }
     * if(throwable!=null){
     * return 0;
     * }
     * return 0;
     * });
     * System.out.println("main方法end"+future.get());
     * 3.线程串行化：
     *              1）.不能获取到上一步的结果，无返回值
     *              .thenRunAsync(()->{
     *             System.out.println("任务2启动");
     *         },service);
     *              2)能获取到上一步结果，无返回值
     *              .thenAcceptAsync(res->{
     *             System.out.println("任务2启动"+res);
     *         },service);
     *              3)能获取到上一步结果，有返回值
     *              .thenApplyAsync(res -> {
     *             System.out.println("任务2启动" + res);
     *             return "有返回值" + res;
     *         }, service);
     *         System.out.println("main方法end"+stringCompletableFuture.get());
     *
     *  3.1 组合两任务，都要完成
     *          1）没有返回值
     *  future01.runAfterBothAsync(future02,()->{
     *             System.out.println("任务3开始");
     *         },service);
     *
     *         2）获取前两个任务的返回值
     *         future01.thenAcceptBothAsync(future02,(f1,f2)->{
     *             System.out.println("任务3开始，之前的结果:"+f1+"->"+f2);
     *         },service);
     *
     *         3)获取前两个任务的返回值，并且自身有返回值
     *         CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
     *             System.out.println("任务3开始，之前的结果:" + f1 + "->" + f2);
     *             return f1 + "-》" + f2 + "===";
     *         }, service);
     *         System.out.println("最终结果："+future.get());
     * 3.2 组合两任务 只要一个完成就 执行任务3
     *         1）无法获取任务 1 2 的返回值，自己没有返回值
     *           future01.runAfterEitherAsync(future02,()->{
     *             System.out.println("任务3开始");
     *         },service);
     *
     *         2)感知结果，自己没有返回值
     *         future01.acceptEitherAsync(future02,(res)->{
     *             System.out.println("任务3开始"+res);
     *         },service);
     *         3)感知结果，自己有返回值
     *          CompletableFuture<String> future = future01.applyToEitherAsync(future02, res -> {
     *             System.out.println("之前的结果：" + res);
     *             return res.toString() + "hhhhhhhhhh-";
     *         }, service);
     */

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main 方法开始");

        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务1线程:" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行1结束:");
            return i;
        }, service);

        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务2线程:" + Thread.currentThread().getId());
            System.out.println("运行2结束:");
            return "hello";
        }, service);

        CompletableFuture<String> future = future01.applyToEitherAsync(future02, res -> {
            System.out.println("之前的结果：" + res);
            return res.toString() + "hhhhhhhhhh-";
        }, service);


        System.out.println("main end"+future.get());

    }

    public static class Thread01 extends Thread {
        @Override
        public void run() {
            System.out.println("当前线程:" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果:" + i);
        }
    }


    public static class Runable01 implements Runnable {

        @Override
        public void run() {
            System.out.println("当前线程:" + Thread.currentThread().getId());
            int i = 10 / 2;

            System.out.println("运行结果:" + i);
        }
    }

    public static class Callable01 implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程:" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果:" + i);
            return i;
        }
    }
}

