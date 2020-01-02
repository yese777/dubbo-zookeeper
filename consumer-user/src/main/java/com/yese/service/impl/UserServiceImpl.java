package com.yese.service.impl;

import com.yese.service.TicketService;
import com.yese.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

@Service //注入到容器中,这里是spring的service
public class UserServiceImpl implements UserService {

    @Reference //远程引用指定的服务，他会按照全类名进行匹配，看谁给注册中心注册了这个全类名
    private TicketService ticketService;

    @Override
    public void bugTicket() {
        String ticket = ticketService.getTicket();
        System.out.println("在注册中心买到" + ticket);
    }
}
