package org.example.entablebe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectController {

    @RequestMapping(value = "/app/{path:[^\\.]*}")
    public String redirectWithApp() {
        // Forward all requests to the Angular index.html
        return "forward:/index.html";
    }

    @RequestMapping(value = "{path:[home^\\.]*}")
    public String redirectWithHomeApp() {
        // Forward all requests to the Angular index.html
        return "forward:/index.html";
    }

}
