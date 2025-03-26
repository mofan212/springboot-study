package indi.mofan.component;


import indi.mofan.condition.ControllerCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;

/**
 * @author mofan
 * @date 2024/8/18 17:06
 */
@Controller
@Conditional(ControllerCondition.class)
public class MyController {
}
