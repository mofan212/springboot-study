package indi.mofan.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mofan
 * @date 2025/6/6 23:17
 */
@RestController
@RequestMapping("accurate")
public class AccurateMatchingController {

    @GetMapping("/get")
    public String accurateMatching() {
        return "accurate matching";
    }
}
