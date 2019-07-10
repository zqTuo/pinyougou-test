package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.impl.CoreServiceImpl;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.sellergoods.service.SpecificationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;



/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl extends CoreServiceImpl<TbSpecification> implements SpecificationService {

	
	private TbSpecificationMapper specificationMapper;
	@Autowired
    private TbSpecificationOptionMapper optionMapper;

    @Autowired
	public SpecificationServiceImpl(TbSpecificationMapper specificationMapper) {
		super(specificationMapper, TbSpecification.class);
		this.specificationMapper=specificationMapper;
	}

	
	

	
	@Override
    public PageInfo<TbSpecification> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<TbSpecification> all = specificationMapper.selectAll();
        PageInfo<TbSpecification> info = new PageInfo<TbSpecification>(all);

        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbSpecification> pageInfo = JSON.parseObject(s, PageInfo.class);
        return pageInfo;
    }

	
	

	 @Override
    public PageInfo<TbSpecification> findPage(Integer pageNo, Integer pageSize, TbSpecification specification) {
        PageHelper.startPage(pageNo,pageSize);

        Example example = new Example(TbSpecification.class);
        Example.Criteria criteria = example.createCriteria();

        if(specification!=null){			
						if(StringUtils.isNotBlank(specification.getSpecName())){
				criteria.andLike("specName","%"+specification.getSpecName()+"%");
				//criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
        List<TbSpecification> all = specificationMapper.selectByExample(example);
        PageInfo<TbSpecification> info = new PageInfo<TbSpecification>(all);
        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbSpecification> pageInfo = JSON.parseObject(s, PageInfo.class);

        return pageInfo;
    }
//    查找
    @Override
    public Specification findOne(Long id){
//        创建对象  将规格和规格项合并成一个对象来传递
        Specification specification = new Specification();
//	    查找规格标签
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);

//        创建规格项标签对象
        TbSpecificationOption option = new TbSpecificationOption();
//        通过规格标签在查找到的 规格项里面的数据
        option.setSpecId(tbSpecification.getId());

        List<TbSpecificationOption> options =optionMapper.select(option);

//         Specification  将查找出来的数据添加到Specification
        specification.setSpecification(tbSpecification);
        specification.setOptionList(options);
        return specification;
    }
//新增
    @Override
    public void add(Specification specification) {
//        获取规格杜对象
        TbSpecification specification1 = specification.getSpecification();
//        往规格对象里面保存的数据
        specificationMapper.insert(specification1);

//        获取规格项对象
        List<TbSpecificationOption> optionList = specification.getOptionList();
//        遍历循环出每个规格项里面的数据  里面是没有id的 把上面查找到的id数据修改过去
        for (TbSpecificationOption option : optionList) {
            option.setSpecId(specification1.getId());
            optionMapper.insert(option);
        }

    }
//    修改
      @Override
    public void update(Specification specification) {
//        修改规格数据
        specificationMapper.updateByPrimaryKey(specification.getSpecification());
//        修改规格项 要 先把之前的删除 在往里面添加内容
          TbSpecificationOption option = new TbSpecificationOption();
//          通过规格的id删除 规格项的数据
          option.setSpecId(specification.getSpecification().getId());
          int delete = optionMapper.delete(option);

//          遍历循环，往规格项里面添加新数据
          List<TbSpecificationOption> optionList = specification.getOptionList();
          for (TbSpecificationOption tbSpecificationOption : optionList) {
              tbSpecificationOption.setSpecId(specification.getSpecification().getId());
              optionMapper.insert(tbSpecificationOption);
          }
//          批量插入  要求 主键是id并且是自增才可以

      }
}
