package indi.mofan.spel.config;

import indi.mofan.spel.pojo.SimpleBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/10/19 19:18
 */
@Component("simpleBean")
public class SimpleBeanFactoryBean implements FactoryBean<SimpleBean> {
    @Override
    public SimpleBean getObject() throws Exception {
        return new SimpleBean();
    }

    @Override
    public Class<?> getObjectType() {
        return SimpleBean.class;
    }
}
