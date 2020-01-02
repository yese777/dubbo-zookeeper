package com.yese.service.impl;

import com.yese.service.TicketService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Service //将服务发布出去,项目一启动就自动注册到注册中心
@Component //使用了dubbo后尽量不要用@Service注解
public class TicketServiceImpl implements TicketService {

    @Override
    public String getTicket() {
        return "《三国演义》";
    }
}
