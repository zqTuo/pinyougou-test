package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.pojo.SysConstants;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.core.service.impl.CoreServiceImpl;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.List;



/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl extends CoreServiceImpl<TbContent> implements ContentService {

	@Autowired
	private RedisTemplate redisTemolate;



	private TbContentMapper contentMapper;

	@Autowired
	public ContentServiceImpl(TbContentMapper contentMapper) {
		super(contentMapper, TbContent.class);
		this.contentMapper=contentMapper;
	}

	
	

	
	@Override
    public PageInfo<TbContent> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<TbContent> all = contentMapper.selectAll();
        PageInfo<TbContent> info = new PageInfo<TbContent>(all);

        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbContent> pageInfo = JSON.parseObject(s, PageInfo.class);
        return pageInfo;
    }

	
	

	 @Override
    public PageInfo<TbContent> findPage(Integer pageNo, Integer pageSize, TbContent content) {
        PageHelper.startPage(pageNo,pageSize);

        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();

        if(content!=null){			
						if(StringUtils.isNotBlank(content.getTitle())){
				criteria.andLike("title","%"+content.getTitle()+"%");
				//criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(StringUtils.isNotBlank(content.getUrl())){
				criteria.andLike("url","%"+content.getUrl()+"%");
				//criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(StringUtils.isNotBlank(content.getPic())){
				criteria.andLike("pic","%"+content.getPic()+"%");
				//criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(StringUtils.isNotBlank(content.getContent())){
				criteria.andLike("content","%"+content.getContent()+"%");
				//criteria.andContentLike("%"+content.getContent()+"%");
			}
			if(StringUtils.isNotBlank(content.getStatus())){
				criteria.andLike("status","%"+content.getStatus()+"%");
				//criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
        List<TbContent> all = contentMapper.selectByExample(example);
        PageInfo<TbContent> info = new PageInfo<TbContent>(all);
        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbContent> pageInfo = JSON.parseObject(s, PageInfo.class);

        return pageInfo;
    }

	@Override
	public List<TbContent> findByCategoryId(Long categoryId) {
//		1.查询redis得数据  如果有 返回
		List<TbContent> content_redis = (List<TbContent>) redisTemolate.boundHashOps("CONTENT_REDIS").get(categoryId);
		if (content_redis!=null && content_redis.size()>0){
			System.out.println("有缓存");
			return content_redis;
		}
//		如果没有 查询数据库的数据

		List<TbContent> contentsFromRedis = (List<TbContent>) redisTemolate.boundHashOps(SysConstants.CONTENT_REDIS_KEY).get(categoryId);


		TbContent record = new TbContent();
		record.setCategoryId(categoryId);
		record.setStatus("1");
		List<TbContent> select = contentMapper.select(record);
//		3.将查询到数据库的数据 写到redis
		System.out.println("meiyouhuancun");

		redisTemolate.boundHashOps("CONTENT_REDIS").put(categoryId, select);
		return select;


	}

}
