package com.att.training.k8s.greeting;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("greet")
@RequiredArgsConstructor
public class GreetingController {

    private final ReverseClient reverseClient;

    @GetMapping("hello")
    String sayHello(@RequestParam(defaultValue = "k8s") String name,
                    @RequestParam(defaultValue = "false") boolean reversed) {
        var greeting = "Hello " + name;
        return reversed ? reverseClient.reverse(greeting) : greeting;
    }
}
