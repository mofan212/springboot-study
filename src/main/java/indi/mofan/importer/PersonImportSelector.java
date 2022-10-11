package indi.mofan.importer;

import indi.mofan.pojo.Person;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/10/11 19:58
 */
@Component
public class PersonImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        // Spring 容器启动时就会调用此方法
        System.out.println("调用 UserImportSelector 的 selectImports 方法获取一批全限定名");
        // 把返回值中的全类名注册到 spring 容器中
        return new String[]{Person.class.getName()};
    }
}
