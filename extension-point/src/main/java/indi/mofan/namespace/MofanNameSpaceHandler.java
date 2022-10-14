package indi.mofan.namespace;

import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.w3c.dom.Element;

/**
 * @author mofan
 * @date 2022/10/14 21:19
 */
public class MofanNameSpaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        //注册解析 bean 标签的解析器
        registerBeanDefinitionParser("bean", new MofanBeanDefinitionParse());
    }

    private static class MofanBeanDefinitionParse extends AbstractSingleBeanDefinitionParser {
        @Override
        protected boolean shouldGenerateId() {
            return true;
        }

        @Override
        protected String getBeanClassName(Element element) {
            // 告诉属性名称
            return element.getAttribute("class");
        }
    }
}
