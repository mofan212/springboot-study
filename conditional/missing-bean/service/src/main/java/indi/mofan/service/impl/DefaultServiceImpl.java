package indi.mofan.service.impl;


import indi.mofan.constant.BeanNameConstant;
import indi.mofan.service.InjectComponent;
import indi.mofan.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mofan
 * @date 2025/3/26 16:43
 */
@Component(BeanNameConstant.DEFAULT_BEAN)
public class DefaultServiceImpl implements MyService {

    public static AtomicInteger integer = new AtomicInteger(0);

    private InjectComponent injectComponent;

    @Autowired(required = false)
    public void setInjectComponent(InjectComponent injectComponent) {
        this.injectComponent = injectComponent;
    }

    public DefaultServiceImpl() {
        integer.incrementAndGet();
    }

    @Override
    public String getStr() {
        return "default " + injectComponent.getStr();
    }

    @Override
    public Integer getInteger() {
        return integer.get();
    }
}
