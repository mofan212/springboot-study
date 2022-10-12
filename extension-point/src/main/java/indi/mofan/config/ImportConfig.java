package indi.mofan.config;

import indi.mofan.importer.PersonImportSelector;
import indi.mofan.importer.TeacherImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author mofan
 * @date 2022/10/11 20:06
 */
@Configuration
@Import(value = {PersonImportSelector.class, TeacherImportBeanDefinitionRegistrar.class})
public class ImportConfig {
}
