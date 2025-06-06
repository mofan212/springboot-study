package indi.mofan.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mofan
 * @date 2025/6/6 23:06
 */
@RestController
@RequestMapping("{firstPath}")
public class FuzzyMatchingController {

    @GetMapping("/get")
    public String fuzzyMatching(@PathVariable String firstPath) {
        return firstPath + " matching";
    }
}
