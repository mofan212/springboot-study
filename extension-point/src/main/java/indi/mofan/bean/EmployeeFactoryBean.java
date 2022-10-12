package indi.mofan.bean;

import indi.mofan.pojo.Employee;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author mofan
 * @date 2022/10/12 20:22
 */
public class EmployeeFactoryBean implements FactoryBean<Employee> {
    @Override
    public Employee getObject() throws Exception {
        Employee employee = new Employee();
        System.out.println("调用 EmployeeFactoryBean 的 getObject 方法生成 Bean:" + employee);
        return employee;
    }

    @Override
    public Class<?> getObjectType() {
        return Employee.class;
    }
}
