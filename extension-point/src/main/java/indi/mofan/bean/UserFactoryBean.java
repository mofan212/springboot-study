package indi.mofan.bean;

import indi.mofan.pojo.User;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/10/11 19:47
 */
@Component
public class UserFactoryBean implements FactoryBean<User> {
    @Override
    public User getObject() throws Exception {
        // 获取 Bean 时调用此方法
        User user = new User();
        System.out.println("调用 UserFactoryBean 的 getObject 方法生成 Bean:" + user);
        return user;
    }

    @Override
    public Class<?> getObjectType() {
        // 这个 FactoryBean 返回的 Bean 类型
        return User.class;
    }
}
