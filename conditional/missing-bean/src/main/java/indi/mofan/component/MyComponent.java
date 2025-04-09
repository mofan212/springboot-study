package indi.mofan.component;


import indi.mofan.service.MyService;
import lombok.Getter;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2025/3/26 16:50
 */
@Getter
@Component
public class MyComponent {
    @DubboReference
    private MyService customComponent;
}
