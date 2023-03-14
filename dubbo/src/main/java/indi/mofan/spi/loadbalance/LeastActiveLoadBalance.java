package indi.mofan.spi.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.context.Lifecycle;
import org.apache.dubbo.common.extension.ExtensionAccessor;
import org.apache.dubbo.common.extension.ExtensionAccessorAware;
import org.apache.dubbo.common.extension.ExtensionPostProcessor;

/**
 * @author mofan
 * @date 2023/3/14 10:55
 */
@Slf4j
public class LeastActiveLoadBalance implements LoadBalance, Lifecycle, ExtensionAccessorAware, ExtensionPostProcessor {
    @Override
    public void initialize() throws IllegalStateException {
        log.info("initialize");
    }

    @Override
    public void start() throws IllegalStateException {
        log.info("start");
    }

    @Override
    public void destroy() throws IllegalStateException {
        log.info("destroy");
    }

    @Override
    public void setExtensionAccessor(ExtensionAccessor extensionAccessor) {
        log.info("setExtensionAccessor");
    }

    @Override
    public Object postProcessBeforeInitialization(Object instance, String name) throws Exception {
        log.info("postProcessBeforeInitialization");
        return instance;
    }

    @Override
    public Object postProcessAfterInitialization(Object instance, String name) throws Exception {
        log.info("postProcessAfterInitialization");
        return instance;
    }
}
