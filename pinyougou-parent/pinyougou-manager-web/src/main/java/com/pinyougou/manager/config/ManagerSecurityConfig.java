package com.pinyougou.manager.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class ManagerSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        会自动添加role
        auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");


        /*super.configure(auth);*/
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http.authorizeRequests().
                antMatchers("/css/**","/img/**","/js/**","/plugins/**","/login.html").permitAll()
//                设置所有的其他请求都需要认证通过即可，也就是用户名和密码正确即可不需要
//                其他角色
                .anyRequest().authenticated();
//        设置表单登录
        http.formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/admin/index.html",true)
                .failureUrl("/login?error");
        http.csrf().disable();
//        开启同源iframe  可以访问策略
        http.headers().frameOptions().sameOrigin();
        http.logout().logoutUrl("/logout").invalidateHttpSession(true);
    }
}
