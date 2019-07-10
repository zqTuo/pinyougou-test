package com.itheima;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;

import com.pinyougou.sellergoods.service.impl.BrandServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@ContextConfiguration("classpath:spring/applicationContext-trans.xml")
@RunWith(SpringRunner.class)
public class Test01 {
    @Autowired
    private TbBrandMapper brandMapper;

//    查询
    @Test
        public void findAll() {
            TbBrand tbBrand = brandMapper.selectByPrimaryKey(30L);
            System.out.println(tbBrand);
        }
//        查询所有
        @Test
    public void find(){
            List<TbBrand> all = brandMapper.selectAll();
            for (TbBrand tbBrand : all) {
                System.out.println(tbBrand);
            }
        }
//分页测试，
    @Test
    public void testPage(){
//        page当前页   size 每页显示多少条
        PageHelper.startPage(1,10);
//        查询
        List<TbBrand> list = brandMapper.selectAll();
        System.out.println(list);
        for (TbBrand tbBrand : list) {
            System.out.println(tbBrand);
        }


    }


}
