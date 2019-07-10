package com.itheima.es.dao;

import com.itheima.model.TbItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

public interface  ItemDao extends ElasticsearchCrudRepository<TbItem,Long> {
}
