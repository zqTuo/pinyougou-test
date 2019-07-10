package com.pinyougou.sellergoods.service;

import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
import com.pinyougou.pojo.Goods;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;

import java.util.List;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService extends CoreService<TbGoods> {
	
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	 PageInfo<TbGoods> findPage(Integer pageNo, Integer pageSize);
	
	

	/**
	 * 分页
	 * @param pageNo 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageInfo<TbGoods> findPage(Integer pageNo, Integer pageSize, TbGoods Goods);

	void add(Goods goods);
	public Goods   findOne(Long id);

	public void update(Goods goods);

	//  根据商品spu的数组对象查询所有的该商品的列表数据
	List<TbItem> findTbItemByIds(long[]ids);

	void updateStatus(Long[] ids, String status);

	List<TbItem> findTbItemListByIds(Long[] ids);
}
