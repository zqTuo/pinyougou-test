package com.itheima;




import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@ContextConfiguration("classpath:spring/applicationContext-trans.xml")
@RunWith(SpringRunner.class)
public class Test02 {
    @Reference
    private BrandService brandService;
@Test
    public void findAll(){
         System.out.println( brandService.findAll());

    }
    @Test
    public void add(){
        TbBrand tbBrand = new TbBrand();
        tbBrand.setFirstChar("f");
        brandService.insert(tbBrand);
    }
}
