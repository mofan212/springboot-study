package indi.mofan.controller;


import indi.mofan.service.AddOperator;
import indi.mofan.service.SubOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mofan
 * @date 2025/8/21 19:57
 */
@RestController
@RequestMapping("operator")
public class OperatorController {
    @Autowired
    private AddOperator addOperator;
    @Autowired
    private SubOperator subOperator;

    @GetMapping("add")
    public Object add() {
        return addOperator.add(1, 2);
    }

    @GetMapping("sub")
    public Object sub() {
        return subOperator.sub(2, 1);
    }
}
