package indi.mofan.registrar;


import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author mofan
 * @date 2025/8/21 19:28
 */
public class OperatorComponentRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
                                        BeanDefinitionRegistry registry) {
        // 1. 获取扫描路径
        Set<String> basePackages = new HashSet<>();
        Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableOperator.class.getName());
        if (MapUtils.getObject(attributes, "value") instanceof String[] value) {
            basePackages.addAll(Arrays.asList(value));
        }
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(metadata.getClassName()));
        }
        // 2. 扫描路径下所有加了 @Operator 注解的接口
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                // 全都要
                return true;
            }
        };
        scanner.setEnvironment(environment);
        scanner.setResourceLoader(resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Operator.class));
        for (String basePackage : basePackages) {
            scanner.findCandidateComponents(basePackage).stream()
                    .filter(i -> i instanceof AnnotatedBeanDefinition)
                    .map(i -> (AnnotatedBeanDefinition) i)
                    .map(AnnotatedBeanDefinition::getMetadata)
                    // 3. 生成代理并注册
                    .forEach(i -> registrarOperatorComponent(i, registry));
        }
    }

    private void registrarOperatorComponent(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(OperatorFactoryBean.class);
        String className = metadata.getClassName();
        builder.addPropertyValue(OperatorFactoryBean.Fields.type, className);
        Map<String, Object> attributes = metadata.getAnnotationAttributes(Operator.class.getName());
        builder.addPropertyValue(OperatorFactoryBean.Fields.expression, MapUtils.getObject(attributes, "value"));
        builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        try {
            beanDefinition.setAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE, Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new BeanInstantiationException(this.getClass(), "Cannot found " + className, e);
        }
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }
}
