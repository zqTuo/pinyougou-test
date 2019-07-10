package com.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
import com.pinyougou.pojo.TbBrand;

import java.util.List;



public interface BrandService extends CoreService<TbBrand> {
    List<TbBrand> findAll();


    //PageInfo<TbBrand> findPage(Integer page, Integer size);

    PageInfo<TbBrand> findPage(Integer page, Integer size, TbBrand brand);

    //PageInfo<TbBrand> findPage(Integer pageNo, Integer pageSize, TbBrand brand);

}
