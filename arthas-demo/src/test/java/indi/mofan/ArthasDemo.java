package indi.mofan;

import lombok.SneakyThrows;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2023/2/20 21:16
 */
public class ArthasDemo {
    @Mock
    private StringRedisTemplate redisTemplate;

    private static final String A = "A";
    private static final String B = "B";

    @SuppressWarnings("unchecked")
    public void mock() {
        redisTemplate = Mockito.mock(StringRedisTemplate.class);
        HashOperations<String, Object, Object> hash = Mockito.mock(HashOperations.class);
        Mockito.when(redisTemplate.opsForHash()).thenReturn(hash);
        Mockito.when(hash.get(Mockito.anyString(), Mockito.any())).thenReturn(new Object());
        Mockito.when(hash.multiGet(Mockito.anyString(), Mockito.any()))
                .thenReturn(new ArrayList<>(Collections.singletonList(new Object())));
    }

    @SneakyThrows
    public static void main(String[] args) {
        ArthasDemo demo = new ArthasDemo();
        demo.deadLock();
    }

    @SneakyThrows
    private void justRun() {
        while (Instant.now().isBefore(Instant.now().plus(1, ChronoUnit.DAYS))) {
            System.out.println("running");
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @SneakyThrows
    private void seeThread() {
        Thread thread = new Thread(() -> {
            System.out.println("this is in a thread");
            try {
                TimeUnit.HOURS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread.setName("thread-demo");
        thread.start();
    }

    @SneakyThrows
    private void seeProductionCode() {
        GoodHabit goodHabit = new GoodHabit();
        goodHabit.doSomething();
        TimeUnit.HOURS.sleep(1);
    }

    private Car getCar(String carName, BigDecimal carPrice) {
        Car car = new Car();
        car.setName(carName);
        car.setPrice(carPrice);
        return car;
    }

    @SneakyThrows
    private void printCarInfo() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(getCar("catName-" + i, new BigDecimal(i)));
            TimeUnit.SECONDS.sleep(1);
        }
    }

    private void deadLoop() {
        while (true) {
            System.out.println("this is in dead loop");
        }
    }

    private void deadLock() {
        Thread thread = buildDeadLockThreadOne();
        thread.start();

        Thread t2 = buildDeadLockThreadTwo();
        t2.setName("死锁二号");
        t2.start();
    }

    private static Thread buildDeadLockThreadOne() {
        Thread thread = new Thread(() -> {
            synchronized (A) {
                System.out.println("线程一获取到资源A");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // ...
                }
                System.out.println("线程一尝试获取资源B");
                synchronized (B) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        // ...
                    }
                }
            }
        });
        thread.setName("死锁一号");
        return thread;
    }

    private static Thread buildDeadLockThreadTwo() {
        return new Thread(() -> {
            synchronized (B) {
                System.out.println("线程二获取到资源B");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // ...
                }
                System.out.println("线程二尝试获取资源A");
                synchronized (A) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        // ...
                    }
                }
            }
        });
    }
}









