package com.pinyougou.shop.conterller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/getName")
    public String login(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
