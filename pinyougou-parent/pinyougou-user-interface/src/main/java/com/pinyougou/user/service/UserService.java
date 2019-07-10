package com.pinyougou.user.service;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.CoreService;
import com.pinyougou.pojo.TbUser;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface UserService extends CoreService<TbUser> {
	
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	 PageInfo<TbUser> findPage(Integer pageNo, Integer pageSize);
	
	

	/**
	 * 分页
	 * @param pageNo 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	PageInfo<TbUser> findPage(Integer pageNo, Integer pageSize, TbUser User);

//	生成短信验证码
	public void createSmsCode(String phone);
//	判断短信验证码是否存在
	public boolean checkSmsCode(String phone,String code);
	public void add(TbUser user);
}
