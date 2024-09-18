package org.example.entablebe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        // Forward all requests to the Angular index.html
        return "forward:/index.html";
    }
}
