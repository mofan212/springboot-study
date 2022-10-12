package indi.mofan.processor;

import indi.mofan.pojo.Student;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/10/12 17:13
 */
@Component
public class StudentBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Student) {
            ((Student) bean).setStudentName("mofan");
        }
        return bean;
    }
}
