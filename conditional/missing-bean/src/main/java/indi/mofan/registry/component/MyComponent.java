package indi.mofan.registry.component;


import indi.mofan.service.MyService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2025/3/26 16:50
 */
@Getter
@Component
public class MyComponent {
    @Autowired
    private MyService myService;
}
