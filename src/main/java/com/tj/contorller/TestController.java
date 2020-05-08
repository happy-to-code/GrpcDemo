package com.tj.contorller;


import com.tj.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class TestController {

    @Resource
    private TestService testService;

    @RequestMapping("getHeight")
    public Map getHeight() {
        return testService.getHeight();
    }
}
