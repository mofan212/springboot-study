package indi.mofan.component;


import indi.mofan.service.MyService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2025/3/26 16:44
 */
@DubboService
@Component("customComponent")
public class CustomComponent implements MyService {
}
