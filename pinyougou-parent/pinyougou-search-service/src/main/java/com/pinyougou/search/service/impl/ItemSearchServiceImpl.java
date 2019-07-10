package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.dao.ItemSearchDao;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.DeleteQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private ItemSearchDao dao;
    @Override
    public void updateIndex(List<TbItem> itemList) {
        //先设置map 再一次性插入
        for (TbItem tbItem : itemList) {
            String spec = tbItem.getSpec();
            Map map = JSON.parseObject(spec, Map.class);
            tbItem.setSpecMap(map);
        }
        dao.saveAll(itemList);
    }


    /**
     * 根据SPU的IDs数组 进行删除
     *
     * @param ids
     */
    @Override
    public void deleteByIds(Long[] ids) {

        DeleteQuery query = new DeleteQuery();
        //删除多个goodsid
        query.setQuery(QueryBuilders.termsQuery("goodsId", ids));
        //根据删除条件 索引名 和 类型
        elasticsearchTemplate.delete(query, TbItem.class);
    }

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        HashMap<String, Object> map = new HashMap<>();
//创建查询的构建对象
        NativeSearchQueryBuilder builder  = new NativeSearchQueryBuilder();

//        根据关键字进行搜索
        String keywords = (String) searchMap.get("keywords");
        if(StringUtils.isNotBlank(keywords)){
//            根据关键字来查询
        builder.withQuery(QueryBuilders.multiMatchQuery(keywords,"seller","category","brand","title" ));
//        聚合分组
        }else {
//            匹配所有
           builder.withQuery(QueryBuilders.matchAllQuery());
        }
            builder.addAggregation(AggregationBuilders.terms("category_group").field("category").size(50));



//                设置高亮
        builder
                .withHighlightFields(new HighlightBuilder.Field("title"))
                .withHighlightBuilder(new HighlightBuilder()
                .preTags("<em style=\"color:red\">")
                .postTags("</em>")
                );
//        3.2过滤查询--- 商品分类的过滤查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

//        搜索条件过滤查询
        String category = (String) searchMap.get("category");
        if (StringUtils.isNotBlank(category)){
//            不为空才设置过滤
            boolQueryBuilder.filter(QueryBuilders.termQuery("category",category ));
        }
//        过滤查询，规格的过滤查询，获取到过各的名称和规格的值，执行过滤查询
        String brand = (String) searchMap.get("brand");
        if (StringUtils.isNotBlank(brand)){
        boolQueryBuilder.filter(QueryBuilders.termQuery("brand",brand ));
        }

        //3.4 过滤查询 ----规格的过滤查询 获取到规格的名称 和规格的值  执行过滤查询
        Map<String,String> spec = (Map<String, String>) searchMap.get("spec");//{"网络":"移动4G","机身内存":"16G"}
        if(spec!=null) {
            for (String key : spec.keySet()) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("specMap."+key+".keyword",spec.get(key)));
            }
        }
//        4.过滤查询，价格区间过滤
        String price = (String) searchMap.get("price");
        if (StringUtils.isNotBlank(price)){
            String[] split=price.split("-");
            if ("*".equals(split[1])){
//                价格大于
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            }else {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(split[0]).to(split[1]));
            }

        }


        builder.withFilter(boolQueryBuilder);

//        构建对象
        NativeSearchQuery query = builder.build();

//        参数1  为当前页码 值为0 表示第一页
//        参数2 为每页显示的行

//        6.设置 分页条件
        Integer pageNo = (Integer) searchMap.get("pageNo");
        Integer pageSize = (Integer) searchMap.get("pageSize");
        if (pageNo == null){
            pageNo=1;
        }
        if (pageSize==null){
            pageSize=40;
        }
        query.setPageable(PageRequest.of(pageNo-1,pageSize ));


//        7.设置价格排序
        String sortField =(String)searchMap.get("sortField");
        String sortType = (String) searchMap.get("sortType");

        if(StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(sortType)){
            if (sortType.equals("ASC")){
                Sort sort = new Sort(Sort.Direction.ASC,sortField);
                query.addSort(sort);
            }else if (sortType.equals("DESC")){
                Sort orders = new Sort(Sort.Direction.DESC, sortField);
                query.addSort(orders);
            }else {
                System.out.println("不排序");
            }


        }


        AggregatedPage<TbItem> tbItems = elasticsearchTemplate.queryForPage(query, TbItem.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                SearchHits hits = response.getHits();
                List<T> content = new ArrayList<>();
                //如果没有搜索到记录
                if(hits==null || hits.getHits().length<=0){
                    return new AggregatedPageImpl(content);
                }
                for (SearchHit hit : hits) {
                    String sourceAsString = hit.getSourceAsString();//就是每一个文档对应的json数据
                    TbItem tbItem = JSON.parseObject(sourceAsString, TbItem.class);

                    //获取高亮
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                    //获取高亮的域为title的高亮对象
                    HighlightField highlightField = highlightFields.get("title");

                    if(highlightField!=null) {

                        //获取高亮的碎片
                        Text[] fragments = highlightField.getFragments();
                        StringBuffer sb = new StringBuffer();//高亮的数据
                        if (fragments != null) {
                            for (Text fragment : fragments) {
                                sb.append(fragment.string());//获取到的高亮碎片的值<em styple="colore:red">
                            }
                        }
                        //不为空的时候 存储值
                        if(StringUtils.isNotBlank(sb.toString())){
                            tbItem.setTitle(sb.toString());
                        }
                    }

                    content.add((T)tbItem);
                }

                AggregatedPageImpl aggregatedPage = new AggregatedPageImpl<T>(content,pageable,hits.getTotalHits(),response.getAggregations(),response.getScrollId());

                return aggregatedPage;
            }
        });
//        获取结果集
//        获取分组的结果
        Aggregation category_group = tbItems.getAggregation("category_group");
            StringTerms stringTerms = (StringTerms) category_group;


            List<String> categoryList = new ArrayList<>();
            if (stringTerms != null) {
                List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
                for (StringTerms.Bucket bucket : buckets) {
                    String keyAsString = bucket.getKeyAsString();//获取到的就是分类的名称
                    categoryList.add(keyAsString);
                }
            }
//        搜索之后  默认展示第一个商品分类到的品牌和规格的列表
//        判断商品分类是否为空  如果不为空 根基点击到商品分类查询该分类 下的所有的品牌各规格的列表

            if (StringUtils.isNotBlank(category)) {
                Map map1 = searchBrandAndSpecList(category);
                map.putAll(map1);
            } else {
                Map map1 = searchBrandAndSpecList(categoryList.get(0));
                map.putAll(map1);
            }


        map.put("categoryList", categoryList);

        map.put("total",tbItems.getTotalElements());
        map.put("rows",tbItems.getContent());
        map.put("totalPages",tbItems.getTotalPages());
        return map;
    }


    private Map searchBrandAndSpecList(String category) {
            //1.集成redis
            //2.注入rediTemplate
            //3.获取分类的名称对应的模板的ID
            //hset bigkey field1 value1     hget bigkey field1
            Long typeId = (Long) redisTemplate.boundHashOps("itemCat").get(category);
            //4.根据模板的ID 获取品牌的列表 和规格的列表
            List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);
            List<Map>  specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeId);
            //5.存储到map中返回
            Map<String,Object> map = new HashMap<>();
            map.put("specList",specList);
            map.put("brandList",brandList);
            return map;
        }
}
  /*  //        过滤查询，  商品分类的过滤的查询
    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
    String category = (String) searchMap.get("category");
        if (StringUtils.isNotBlank(category)){
        boolQueryBuilder.filter(QueryBuilders.termQuery("category",category ));
    }*/

