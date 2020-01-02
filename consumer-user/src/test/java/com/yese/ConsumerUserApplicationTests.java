package com.yese;

import com.yese.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConsumerUserApplicationTests {

    @Autowired
    UserService userService;

    @Test
    public void test01() {
        userService.bugTicket();
    }

    @Test
    void contextLoads() {
    }

}
