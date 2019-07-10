package com.pinyougou.shop.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private SellerService sellerService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        1.从数据库中获取用户的信息（用户的密码）
//        1.1先根据页面传递过来的用户名（seller——id） 查询用户对象
        TbSeller tbSeller = sellerService.findOne(s);
//        1.2判断如果用户不存在，直接返回null
        if(tbSeller==null){
          return null;

        }
//        1.3 判断用户 是否已经被审核了，如果没有被审核 return
        if(!tbSeller.getStatus().equals("1")){
            return null;
        }
//        1.4 如果用户存在，需要陪陪密码，（自动匹配） 就获取用户的密码

        String password=tbSeller.getPassword();

//        2.给用户设置权限
        ArrayList<Object> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("ROLE_USER"));//授权角色
        list.add(new SimpleGrantedAuthority("ROLE_ADMIN"));//授角色

//        2.2 交给springSecurity  自动匹配
    return new User(s, tbSeller.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER") );

    }
}
