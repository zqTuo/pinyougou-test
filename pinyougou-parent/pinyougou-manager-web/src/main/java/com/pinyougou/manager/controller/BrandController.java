package com.pinyougou.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.domain.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {


//    查询所有
    @Reference
    BrandService brandService;
    @RequestMapping("/findAll")
    public List<TbBrand> findAll(){
      return   brandService.findAll();

    }

//    分页查询
    @RequestMapping("/findPage")
    public PageInfo<TbBrand> findPage(
            @RequestParam(value = "pageNo",required = false,defaultValue = "1")Integer page,
            @RequestParam(value = "pageSize",required = false,defaultValue = "5")Integer size,
            @RequestBody TbBrand brand
    ){

        PageInfo<TbBrand> pageInfo = brandService.findPage(page, size, brand);

        return pageInfo;
    }
//    添加
    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand brand){
        try {
            brandService.insert(brand);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
//    修改
    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand brand){
        try {
            brandService.update(brand);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }
//    查询一个
    @RequestMapping("/findOne")
    public TbBrand findOne(Long id){
    TbBrand tbBrand =brandService.findOne(id);
    return tbBrand;
    }

//    删除
    @RequestMapping("/delete")
    public Result delete(@RequestBody Long[] ids){
        try {
            brandService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }

    }

}
