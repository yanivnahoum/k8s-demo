package com.att.training.k8s.reverse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reverse")
public class ReverseController {

    @GetMapping("/{word}")
    String reverse(@PathVariable String word) {
        return new StringBuilder(word)
                .reverse()
                .toString();
    }
}
