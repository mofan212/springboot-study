package indi.mofan.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mofan
 * @date 2025/8/9 21:35
 */
@RestController
public class SseController {

    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    @GetMapping("/sse")
    public SseEmitter sse() {
        // 超时时间设置为 1 分钟
        SseEmitter emitter = new SseEmitter(60_000L);

        char[] str = """
                床前明月光，
                疑是地上霜。
                举头望明月，
                低头思故乡。
                """.toCharArray();
        AtomicInteger integer = new AtomicInteger(0);
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {
            int v = integer.get();
            if (v >= str.length) {
                emitter.complete();
                return;
            }
            try {
                emitter.send(SseEmitter.event().data(String.valueOf(str[integer.getAndIncrement()])));
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }, 0, 150, TimeUnit.MILLISECONDS);

        emitter.onCompletion(() -> future.cancel(true));
        return emitter;
    }
}
