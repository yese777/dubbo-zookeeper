package com.yese.controller;

import com.yese.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("buy")
    public String bugTicket() {
        String ticket = userService.bugTicket();
        return ticket;
    }

}
