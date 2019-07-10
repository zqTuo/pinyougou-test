package com.itheima;




import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@ContextConfiguration("classpath:spring/springmvc.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class Test02 {
    @Reference
    private BrandService brandService;
   @Test
    public void findAll(){
     while (true){
         System.out.println( brandService.findAll());

     }



    }
}
