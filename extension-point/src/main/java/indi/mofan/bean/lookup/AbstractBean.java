package indi.mofan.bean.lookup;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/6/1 15:56
 */
@Component
public abstract class AbstractBean {

    @Lookup
    public void method() {
    }
}
