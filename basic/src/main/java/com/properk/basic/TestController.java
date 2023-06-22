package com.properk.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("/test") // Get "/test" command and send it to business layer
    public List<Member> getAllMember() {
        return testService.getAllMember();
    }

}
