package indi.mofan.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author mofan
 * @date 2025/8/12 17:26
 */
@RestController
public class ResponseStatusController {

    @GetMapping("/code/{code}")
    public String get404(@PathVariable(value = "code", required = false) String code) {
        if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(code)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NOT FOUND");
        }
        return code;
    }
}
