package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.impl.CoreServiceImpl;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class  BrandServiceImpl extends CoreServiceImpl<TbBrand> implements BrandService {




    private TbBrandMapper tbBrandMapper;

    public BrandServiceImpl( TbBrandMapper tbBrandMapper) {
        super(tbBrandMapper, TbBrand.class);
        this.tbBrandMapper=tbBrandMapper;
    }



    @Override
    public PageInfo<TbBrand> findPage(Integer page, Integer size, TbBrand brand) {
        PageHelper.startPage(page,size);
        Example example =new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();

        if (brand!=null){
            if (StringUtils.isNotBlank(brand.getName())){
                criteria.andLike("name","%"+brand.getName()+"%" );
            }
            if (StringUtils.isNotBlank(brand.getFirstChar())){
                criteria.andLike("firstChar","%"+brand.getFirstChar()+"%" );
            }
        }
        List<TbBrand> all =tbBrandMapper.selectByExample(example);
        PageInfo<TbBrand> Info = new PageInfo<>(all);
//        序列化  在反序列化
        String s = JSON.toJSONString(Info);
        PageInfo<TbBrand> pageInfo = JSON.parseObject(s, PageInfo.class);

        return pageInfo;
    }


}
