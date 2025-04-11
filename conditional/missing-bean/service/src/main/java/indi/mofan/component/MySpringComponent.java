package indi.mofan.component;


import indi.mofan.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2025/4/10 16:04
 */
@Component
public class MySpringComponent {
    @Autowired
    private MyService myService;

    public String getStr() {
        return myService.getStr();
    }
}
