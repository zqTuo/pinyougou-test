package com.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
import com.pinyougou.pojo.TbSeller;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SellerService extends CoreService<TbSeller> {
	
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	 PageInfo<TbSeller> findPage(Integer pageNo, Integer pageSize);
	
	

	/**
	 * 分页
	 * @param pageNo 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageInfo<TbSeller> findPage(Integer pageNo, Integer pageSize, TbSeller Seller);


//	重写add方法
	public void add(TbSeller seller);


//对服务接口新增的方法重新定义
	public void updateStatus(String id,String status);


}
