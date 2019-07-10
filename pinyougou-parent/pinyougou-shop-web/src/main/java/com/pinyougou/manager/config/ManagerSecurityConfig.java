package com.pinyougou.manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class ManagerSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    public UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        会自动添加role_
        //auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");
        //auth.userDetailsService(userDetailsService);
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**","/img/**","/js/**","/shoplogin.html",
                        "/seller/add.shtml"
                        ).permitAll()
//                设置所有的其他请求都需要认证通过即可，也就是用户名和密码正确即可不需要其他的角色
                .anyRequest().authenticated();
//        设置表单登录
        http.formLogin()
                .loginPage("/shoplogin.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/admin/index.html",true)
                .failureUrl("/login?error");
        http.csrf().disable();   //关闭csrf

//        开启同源iframe  可以访问策略
        http.headers().frameOptions().sameOrigin();
    }
}
