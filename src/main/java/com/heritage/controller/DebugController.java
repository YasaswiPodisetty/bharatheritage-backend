package com.heritage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @GetMapping("/")
    public String home() {
        return "Backend is working ";
    }

    @GetMapping("/test-error")
    public String testError() {
        throw new RuntimeException("FORCED ERROR");
    }
}
