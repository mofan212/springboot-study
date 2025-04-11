package indi.mofan;


import indi.mofan.service.MyService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2025/4/11 10:45
 */
@Component
public class RefDubboService {
    @DubboReference
    private MyService myService;

    public String getStr() {
        return myService.getStr();
    }
}
