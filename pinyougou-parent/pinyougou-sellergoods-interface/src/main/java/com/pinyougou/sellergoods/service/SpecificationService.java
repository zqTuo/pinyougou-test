package com.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
import com.pinyougou.pojo.Specification;
import com.pinyougou.pojo.TbSpecification;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SpecificationService extends CoreService<TbSpecification> {
	
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	 PageInfo<TbSpecification> findPage(Integer pageNo, Integer pageSize);
	
	

	/**
	 * 分页
	 * @param pageNo 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageInfo<TbSpecification> findPage(Integer pageNo, Integer pageSize, TbSpecification Specification);
//查询一个
	public Specification findOne(Long id);
//	增加
	public void add(Specification specification);
//	修改
	public void update(Specification specification);
	
}
