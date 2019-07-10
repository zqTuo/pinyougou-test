package com.itheima;

import com.itheima.es.dao.ItemDao;
import com.itheima.model.TbItem;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(locations = "classpath:spring-es.xml")
public class ElasticSearchTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
//    创建索引和映射

    @Autowired
    private ItemDao itemDao;

    @Test
    public void testCreateIndexAndMapping() {
//        创建索引
        elasticsearchTemplate.createIndex(TbItem.class);
//        创建映射
        elasticsearchTemplate.putMapping(TbItem.class);
    }

    @Test
    public void saveData() {

        TbItem tbItem = new TbItem();
        tbItem.setId(20000L);
        tbItem.setTitle("测试商品分类 111:");
        tbItem.setCategory("商品分类:");
        tbItem.setBrand("三星:");
        tbItem.setSeller("三星旗舰店:");
        HashMap<String, String> map = new HashMap<>();
        map.put("网络制式","移动4G"  );
        map.put("机身内存","16G" );
        tbItem.setSpecMap(map);
        itemDao.save(tbItem);

    }

    //    删除文档
    @Test
    public void deleteById() {
        itemDao.deleteById(20000L);
    }

    //    更新文档  修改数据和保存数据一样  当有相同的id时 直接更新
    @Test
    public void update() {
        for (int i = 0; i < 100; i++) {
        TbItem tbItem = new TbItem();
        tbItem.setId(20000L+i);
        tbItem.setTitle("测试商品分类 111:"+i);
        tbItem.setCategory("商品分类:"+i);
        tbItem.setBrand("三星:"+i);
        tbItem.setSeller("三星旗舰店:"+i);
        itemDao.save(tbItem);
        }

    }

    //    查询  查询所有
    @Test
    public void QueryAll() {
        Iterable<TbItem> all = itemDao.findAll();
        for (TbItem tbItem : all) {
            System.out.println(tbItem.getTitle());
        }
    }

    //    分页查询  所有数据
    @Test
    public void queryByPageable() {
        Pageable Pageable = PageRequest.of(0, 10);
        Page<TbItem> all = itemDao.findAll(Pageable);
        for (TbItem tbItem : all) {
            System.out.println(tbItem.getTitle());
        }
    }

    /*
     * 通配符 查询，索引的时候分词了  但是IC哈U型你的时候部分词
     * 表示匹配所有
     * 表示匹配一个字符  会占用一个字符空间
     * */
    @Test
    public void queryByWialdQuery() {
        SearchQuery query = new NativeSearchQuery(QueryBuilders.wildcardQuery("title", "测?"));

        AggregatedPage<TbItem> tbItems = elasticsearchTemplate.queryForPage(query, TbItem.class);

        long totalElements = tbItems.getTotalElements();
        System.out.println("总记录数：" + totalElements);
        List<TbItem> content = tbItems.getContent();

        for (TbItem tbItem : content) {
            System.out.println(tbItem.getTitle());
        }


    }

    /*  分词 匹配 查询 通过boolean 查询是or进行连接
     *
     * 索引的时候分词了 查询的时候先分词，在进行查询匹配 并通过or 进行连接，并集显示所以 所有数据
     *
     * */
    @Test
    public void queryByMatchQuery() {
        SearchQuery query = new NativeSearchQuery(QueryBuilders.matchQuery("title", "商品111"));

        AggregatedPage<TbItem> tbItems = elasticsearchTemplate.queryForPage(query, TbItem.class);

        long totalElements = tbItems.getTotalElements();
        System.out.println("总记录数：" + totalElements);
        List<TbItem> content = tbItems.getContent();
        for (TbItem tbItem : content) {
            System.out.println(tbItem.getTitle());
        }
    }
//    组合域测试
    @Test
    public void queryByCoptTo(){
//          创建搜索对象
        SearchQuery query = new NativeSearchQuery(QueryBuilders.matchQuery("keyword", "三星"));
//        执行搜索
        AggregatedPage<TbItem> tbItems = elasticsearchTemplate.queryForPage(query, TbItem.class);
        long totalElements = tbItems.getTotalElements();
        System.out.println("总记录数"+totalElements);
        List<TbItem> content = tbItems.getContent();
        for (TbItem tbItem : content) {
            System.out.println(tbItem.getTitle());
        }
    }
    @Test
    public void queryByObject(){
//      specMap  网路制式keyword  fileName属性名  keyword
//      specMap  指定的就是要查询的字段名和  pojo的字段一致

//        网络制式  指定的就是网络制式的字段
//        keyword 固定的写法 表示搜索的时候不分词
//        创建搜索对象
        SearchQuery query = new NativeSearchQuery(QueryBuilders.matchQuery("specMap.网络制式.keyword", "移动4G"));
//        执行查询.
        AggregatedPage<TbItem> tbItems = elasticsearchTemplate.queryForPage(query, TbItem.class);
        long totalElements = tbItems.getTotalElements();
        System.out.println("总记录数："+totalElements);
        List<TbItem> content = tbItems.getContent();
        for (TbItem tbItem : content) {
            System.out.println(tbItem.getSpecMap());
        }
    }
    @Test
    public void queryByFilter(){
//        1.创建查询对象的构建对象
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

//        2.设置查询 条件
        queryBuilder.withIndices("pinyougou");  //设置从哪一个索引开始查询
        queryBuilder.withTypes("item");  // 设置从哪一个类型中查询
        queryBuilder.withQuery(QueryBuilders.matchQuery("title","商品" ));  //从title内容中为上平的数据查询
//        3.创建 过滤查询，（规格的过滤查询，多个过滤使用bool查询）
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.filter(QueryBuilders.termQuery("specMap.网络制式.keyword","移动4G" ));
        boolQueryBuilder.filter(QueryBuilders.termQuery("specMap.机身内存.keyword", "16G"));

        queryBuilder.withQuery(boolQueryBuilder);

//       4.构建  查询条件
        NativeSearchQuery searchQuery = queryBuilder.build();
//        执行查询
        AggregatedPage<TbItem> tbItems = elasticsearchTemplate.queryForPage(searchQuery, TbItem.class);

        long totalElements = tbItems.getTotalElements();
        List<TbItem> content = tbItems.getContent();
        for (TbItem tbItem : content) {
            System.out.println(tbItem.getSpecMap());
        }


    }
}
