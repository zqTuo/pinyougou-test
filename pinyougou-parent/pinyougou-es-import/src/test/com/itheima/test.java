package com.itheima;

import com.pinyougou.es.service.ItemService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class test {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context  = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        ItemService itemService =context.getBean(ItemService.class);
        itemService.ImportDataToEs();
    }
}
