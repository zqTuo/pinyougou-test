package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.impl.CoreServiceImpl;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.mapper.TbTypeTemplateMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class TypeTemplateServiceImpl extends CoreServiceImpl<TbTypeTemplate> implements TypeTemplateService {

	
	private TbTypeTemplateMapper typeTemplateMapper;
	@Autowired
    TbSpecificationOptionMapper optionMapper;
	@Autowired
    RedisTemplate redisTemplate;

	@Autowired
	public TypeTemplateServiceImpl(TbTypeTemplateMapper typeTemplateMapper) {
		super(typeTemplateMapper, TbTypeTemplate.class);
		this.typeTemplateMapper=typeTemplateMapper;
	}

	
	

	
	@Override
    public PageInfo<TbTypeTemplate> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<TbTypeTemplate> all = typeTemplateMapper.selectAll();
        PageInfo<TbTypeTemplate> info = new PageInfo<TbTypeTemplate>(all);

        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbTypeTemplate> pageInfo = JSON.parseObject(s, PageInfo.class);
        return pageInfo;
    }

	
	

	 @Override
    public PageInfo<TbTypeTemplate> findPage(Integer pageNo, Integer pageSize, TbTypeTemplate typeTemplate) {
        PageHelper.startPage(pageNo,pageSize);


        Example example = new Example(TbTypeTemplate.class);
        Example.Criteria criteria = example.createCriteria();

        if(typeTemplate!=null){			
						if(StringUtils.isNotBlank(typeTemplate.getName())){
				criteria.andLike("name","%"+typeTemplate.getName()+"%");
				//criteria.andNameLike("%"+typeTemplate.getName()+"%");
			}
			if(StringUtils.isNotBlank(typeTemplate.getSpecIds())){
				criteria.andLike("specIds","%"+typeTemplate.getSpecIds()+"%");
				//criteria.andSpecIdsLike("%"+typeTemplate.getSpecIds()+"%");
			}
			if(StringUtils.isNotBlank(typeTemplate.getBrandIds())){
				criteria.andLike("brandIds","%"+typeTemplate.getBrandIds()+"%");
				//criteria.andBrandIdsLike("%"+typeTemplate.getBrandIds()+"%");
			}
			if(StringUtils.isNotBlank(typeTemplate.getCustomAttributeItems())){
				criteria.andLike("customAttributeItems","%"+typeTemplate.getCustomAttributeItems()+"%");
				//criteria.andCustomAttributeItemsLike("%"+typeTemplate.getCustomAttributeItems()+"%");
			}
	
		}
        List<TbTypeTemplate> all = typeTemplateMapper.selectByExample(example);
        PageInfo<TbTypeTemplate> info = new PageInfo<TbTypeTemplate>(all);
        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbTypeTemplate> pageInfo = JSON.parseObject(s, PageInfo.class);


//        获取模板数据
         List<TbTypeTemplate> tbTypeTemplateList = this.findAll();
//         循环 模板数据
         for (TbTypeTemplate tbTypeTemplate1 : tbTypeTemplateList) {
//             存储品牌列表
             List<Map>brandList = JSON.parseArray(tbTypeTemplate1.getBrandIds(),Map.class);
             redisTemplate.boundHashOps("brandList").put(tbTypeTemplate1.getId(),brandList );
//             存储规格
             List<Map> specList = findSpecList(tbTypeTemplate1.getId()); //根据模板查询规格列表

             redisTemplate.boundHashOps("specList").put(tbTypeTemplate1.getId(),specList);
         }


         return pageInfo;
    }
//// 显示规格选项列表
    @Override
    public List<Map> findSpecList(Long id) {
//            根据主键 查询模板的对象
        TbTypeTemplate tbTypeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
//            2.获取模板的对象中的规格列表 [{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = tbTypeTemplate.getSpecIds();
//        3.将字符串转成json对象map  同时获取TbTypeTemplate 是一个map集合
        List<Map> maps = JSON.parseArray(specIds, Map.class);

        for (Map map : maps) {
            //{"id":27,"text":"网络"}
//            4.循环bianlijson 数组  根据规格id  获取规格下的所有的选项列表
            Integer  id1 = (Integer) map.get("id");
//            select* from option where spec_id=id
            TbSpecificationOption record  = new TbSpecificationOption();
            record.setSpecId(Long.valueOf(id1));
            // 这是查询到的结果是[{optionName:'移动3G'},{optionName:'移动4G'}]
            List<TbSpecificationOption> optionList = optionMapper.select(record);
//            我们需要拼接成 [{"id":27,"text":"网络",optionsList:[{optionName:'移动3G'},{optionName:'移动4G'}]},{"id":32,"text":"机身内存"}]
            map.put("options", optionList);
        }
        return maps;
    }

}
