package com.github.mateuszwlosek.oauth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Controller.PATH)
public class Controller {

    public static final String PATH = "normal";

    @GetMapping("/test")
    public String normalEndpoint() {
        return "Hello World!";
    }
}
