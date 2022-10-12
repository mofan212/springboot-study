package indi.mofan.importer;

import indi.mofan.pojo.Teacher;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/10/11 20:22
 */
@Component
public class TeacherImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        BeanDefinitionRegistry registry,
                                        BeanNameGenerator importBeanNameGenerator) {
        // 构建一个 BeanDefinition，类型为 Teacher
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(Teacher.class)
                // 需要提供 setter 方法以便赋值使用
                .addPropertyValue("name", "mofan")
                .getBeanDefinition();
        System.out.println("使用 ImportBeanDefinitionRegistrar 往容器中注册 Bean");
        // 注册到容器
        registry.registerBeanDefinition("teacher", beanDefinition);
    }
}
