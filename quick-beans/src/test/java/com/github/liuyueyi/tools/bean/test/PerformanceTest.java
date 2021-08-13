package com.github.liuyueyi.tools.bean.test;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.StopWatch;
import com.github.liuyueyi.tools.bean.test.model.CamelTarget;
import com.github.liuyueyi.tools.bean.test.model.Source;
import com.google.common.util.concurrent.RateLimiter;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.ReflectUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 性能测试
 *
 * @author yihui
 * @date 2021/4/9
 */
public class PerformanceTest extends BasicTest {
    public <K, T> T apacheCopy(K source, Class<T> target) throws Exception {
        T res = (T) ReflectUtils.newInstance(target);
        // 注意，第一个参数为target，第二个参数为source
        // 与其他的正好相反
        BeanUtils.copyProperties(res, source);
        return res;
    }

    public <K, T> T pureCglibCopy(K source, Class<T> target) throws Exception {
        BeanCopier copier = BeanCopier.create(source.getClass(), target, false);
        T res = (T) ReflectUtils.newInstance(target);
        copier.copy(source, res, null);
        return res;
    }

    public <K, T> T huToolCopy(K source, Class<T> target) throws Exception {
        return BeanUtil.toBean(source, target);
    }

    public <K, T> T quickCopy(K source, Class<T> target, boolean camelEnable) throws Exception {
        return com.github.liuyueyi.tools.bean.BeanUtils.copy(source, target, camelEnable);
    }

    @Test
    public void copyTest() throws Exception {
        Source s = genSource();
        Class target = CamelTarget.class;
        Object ta = apacheCopy(s, target);
        Object th = huToolCopy(s, target);
        Object tc = pureCglibCopy(s, target);
        Object tq = quickCopy(s, target, true);
        System.out.println("source:\t" + s + "\napache:\t" + ta + "\nhutool:\t" + th
                + "\ncglib:\t" + tc + "\nquick:\t" + tq);
    }

    @Test
    public void testPerformance() throws Exception {
        Class target = CamelTarget.class;
        autoCheck(target, 10000);
        autoCheck(target, 10000);
        autoCheck(target, 10000_0);
        autoCheck(target, 20000_0);
        autoCheck(target, 50000_0);
        autoCheck(target, 10000_00);
    }

    private <T> void autoCheck(Class<T> target, int size) throws Exception {
        StopWatch stopWatch = new StopWatch();
        runCopier(stopWatch, "apacheCopy", size, (s) -> apacheCopy(s, target));
        runCopier(stopWatch, "huToolCopy", size, (s) -> huToolCopy(s, target));
        runCopier(stopWatch, "pureCglibCopy", size, (s) -> pureCglibCopy(s, target));
        runCopier(stopWatch, "quickCopy", size, (s) -> quickCopy(s, target, true));
        System.out.println((size / 10000) + "w -------- cost: " + stopWatch.prettyPrint());
    }

    private <T> void runCopier(StopWatch stopWatch, String key, int size, CopierFunc func) throws Exception {
        stopWatch.start(key);
        for (int i = 0; i < size; i++) {
            Source s = genSource();
            func.apply(s);
        }
        stopWatch.stop();
    }

    @FunctionalInterface
    public interface CopierFunc<T> {
        T apply(Source s) throws Exception;
    }

    private void doSome(ReentrantReadWriteLock.WriteLock writeLock) {
        try {
            writeLock.lock();
            System.out.println("持有锁成功 " + Thread.currentThread().getName());
            Thread.sleep(1000);
            System.out.println("执行完毕! " + Thread.currentThread().getName());
            writeLock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void lock() throws InterruptedException {
        ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

        new Thread(() -> doSome(reentrantReadWriteLock.writeLock())).start();
        new Thread(() -> doSome(reentrantReadWriteLock.writeLock())).start();
        new Thread(() -> doSome(reentrantReadWriteLock.writeLock())).start();

        Thread.sleep(20000);
    }

    AtomicInteger cnt = new AtomicInteger();

    private void consumer(LinkedBlockingQueue<Integer> queue) {
        try {
            // 同步阻塞拿去数据
            int val = queue.take();
            Thread.sleep(2000);
            System.out.println("成功拿到: " + val + " Thread: " + Thread.currentThread());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 添加数据
            System.out.println("结束 " + Thread.currentThread());
            queue.offer(cnt.getAndAdd(1));
        }
    }

    @Test
    public void blockQueue() throws InterruptedException {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(2);
        queue.add(cnt.getAndAdd(1));
        queue.add(cnt.getAndAdd(1));


        new Thread(() -> consumer(queue)).start();
        new Thread(() -> consumer(queue)).start();
        new Thread(() -> consumer(queue)).start();
        new Thread(() -> consumer(queue)).start();

        Thread.sleep(10000);
    }


    private void semConsumer(Semaphore semaphore) {
        try {
            //同步阻塞，尝试获取信号
            semaphore.acquire(1);
            System.out.println("成功拿到信号，执行: " + Thread.currentThread());
            Thread.sleep(2000);
            System.out.println("执行完毕，释放信号: " + Thread.currentThread());
            semaphore.release(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void semaphore() throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);

        new Thread(() -> semConsumer(semaphore)).start();
        new Thread(() -> semConsumer(semaphore)).start();
        new Thread(() -> semConsumer(semaphore)).start();
        new Thread(() -> semConsumer(semaphore)).start();
        new Thread(() -> semConsumer(semaphore)).start();

        Thread.sleep(20_000);
    }

    @Test
    public void countDown() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            try {
                System.out.println("do something in " + Thread.currentThread());
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("do something in t2: " + Thread.currentThread());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }).start();

        countDownLatch.await();
        System.out.printf("结束");
    }


    private void cyclicBarrierLogic(CyclicBarrier barrier, long sleep) {
        // 等待达到条件才放行
        try {
            System.out.println("准备执行: " + Thread.currentThread() + " at: " + LocalDateTime.now());
            Thread.sleep(sleep);
            int index = barrier.await();
            System.out.println("开始执行: " + index + " thread: " + Thread.currentThread() + " at: " + LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCyclicBarrier() throws InterruptedException {
        // 到达两个工作线程才能继续往后面执行
        CyclicBarrier barrier = new CyclicBarrier(2);
        // 三秒之后，下面两个线程的才会输出 开始执行
        new Thread(() -> cyclicBarrierLogic(barrier, 1000)).start();
        new Thread(() -> cyclicBarrierLogic(barrier, 3000)).start();

        Thread.sleep(4000);
        // 重置，可以再次使用
        barrier.reset();
        new Thread(() -> cyclicBarrierLogic(barrier, 1)).start();
        new Thread(() -> cyclicBarrierLogic(barrier, 1)).start();
        Thread.sleep(10000);
    }

    private void guavaProcess(RateLimiter rateLimiter) {
        try {
            // 同步阻塞方式获取
            System.out.println("准备执行: " + Thread.currentThread() + " > " + LocalDateTime.now());
            rateLimiter.acquire();
            System.out.println("执行中: " + Thread.currentThread() + " > " + LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGuavaRate() throws InterruptedException {
        // 1s 中放行两个请求
        RateLimiter rateLimiter = RateLimiter.create(2.0d);
        new Thread(() -> guavaProcess(rateLimiter)).start();
        new Thread(() -> guavaProcess(rateLimiter)).start();
        new Thread(() -> guavaProcess(rateLimiter)).start();
        new Thread(() -> guavaProcess(rateLimiter)).start();
        new Thread(() -> guavaProcess(rateLimiter)).start();
        new Thread(() -> guavaProcess(rateLimiter)).start();
        new Thread(() -> guavaProcess(rateLimiter)).start();

        Thread.sleep(20_000);
    }
}
