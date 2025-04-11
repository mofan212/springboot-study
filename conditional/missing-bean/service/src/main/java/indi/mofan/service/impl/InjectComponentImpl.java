package indi.mofan.service.impl;


import indi.mofan.service.InjectComponent;
import org.springframework.stereotype.Service;

/**
 * @author mofan
 * @date 2025/4/10 16:02
 */
@Service
public class InjectComponentImpl implements InjectComponent {
    @Override
    public String getStr() {
        return "Inject";
    }
}
