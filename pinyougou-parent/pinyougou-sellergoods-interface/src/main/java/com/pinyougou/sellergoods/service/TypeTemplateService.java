package com.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
import com.pinyougou.pojo.TbTypeTemplate;

import java.util.List;
import java.util.Map;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface TypeTemplateService extends CoreService<TbTypeTemplate> {
	
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	 PageInfo<TbTypeTemplate> findPage(Integer pageNo, Integer pageSize);
	
	

	/**
	 * 分页
	 * @param pageNo 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageInfo<TbTypeTemplate> findPage(Integer pageNo, Integer pageSize, TbTypeTemplate TypeTemplate);

// 显示规格选项列表
	public List<Map> findSpecList(Long id);
	
}
