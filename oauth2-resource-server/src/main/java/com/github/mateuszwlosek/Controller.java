package com.github.mateuszwlosek;

import com.github.mateuszwlosek.user.User;
import com.github.mateuszwlosek.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = Controller.PATH)
public class Controller {

    public static final String PATH = "resources";

    private final UserService userService;

    @GetMapping("/test")
    public String test() {
        final User currentUser = userService.getCurrentUser();
        return "Hello World from Resource server for user: " + currentUser.getUsername();
    }
}