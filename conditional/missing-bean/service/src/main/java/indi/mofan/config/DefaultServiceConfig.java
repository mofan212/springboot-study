package indi.mofan.config;


import indi.mofan.service.MyService;
import indi.mofan.service.impl.DefaultServiceImpl;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.rpc.model.ModuleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mofan
 * @date 2025/4/9 16:22
 */
@Configuration
public class DefaultServiceConfig {

    @Autowired
    private ModuleModel moduleModel;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    @ConditionalOnBean(DefaultServiceImpl.class)
    public ServiceConfig<MyService> myServiceExport() {
        ServiceConfig<MyService> service = new ServiceConfig<>();
        service.setScopeModel(moduleModel);

        service.setInterface(MyService.class);
        service.setRef(applicationContext.getBean(MyService.class));

        // 手动注册
        service.export();
        return service;
    }
}
