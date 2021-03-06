package com.example.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @Value("${gateway.s}")
    private String s;

    @GetMapping("s")
    public String s() {
        if (StringUtils.isEmpty(s)) {
            return "gateway s s s ";
        }
        return s;
    }
}
