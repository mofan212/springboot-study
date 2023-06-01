package indi.mofan.bean.lookup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/6/1 15:51
 */
@Component
public class SingletonBean {
    @Autowired
    private PrototypeBean prototypeBean;

    public PrototypeBean getBean() {
        return this.prototypeBean;
    }

    @Lookup
    public PrototypeBean getPrototypeBean() {
        return this.prototypeBean;
    }

    @Lookup
    public PrototypeBean returnNull() {
        return null;
    }
}
