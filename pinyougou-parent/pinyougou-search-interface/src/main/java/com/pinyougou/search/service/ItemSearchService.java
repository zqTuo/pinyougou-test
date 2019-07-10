package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItem;

import java.util.List;
import java.util.Map;

public interface ItemSearchService {
//    根据搜索条件内容展示数据返回

    Map<String, Object> search(Map<String, Object> searchMap);

//    更新数据库到索引库中
     void updateIndex(List<TbItem>items);

//     商品删除 同步到索引数据
    void deleteByIds(Long[] ids);

}
