package com.example.microservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @Value("${micro-service.s}")
    private String s;

    @GetMapping("s")
    public String s() {
        if (StringUtils.isEmpty(s)) {
            return "s s s ";
        }
        return s;
    }
}
