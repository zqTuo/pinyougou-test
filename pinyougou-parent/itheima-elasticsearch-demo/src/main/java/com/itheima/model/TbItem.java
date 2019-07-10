package com.itheima.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Map;

@Document(indexName="pinyougou",type="item")
public class TbItem implements Serializable {

//    商品id  同时是商品的编号

    @Id   //文档唯一的id  表示字段为文档的唯一标识
    @Field(type = FieldType.Long)  //标识  该数据也要最为字段进行展示
    private Long id;
//    商品标题
@Field(analyzer = "ik_smart",searchAnalyzer = "ik_smart",type = FieldType.Text,copyTo = "keyword")
private String title;

    @Field(type = FieldType.Long)
    private Long goodsId;

//    冗余字段，存放三级分类名称  关键字 只能按照确切的词来搜索
@Field(type = FieldType.Keyword,copyTo = "keyword")
    private String category;

//    冗余字段 存放品牌名称
@Field (type = FieldType.Keyword, copyTo = "keyword")
    private String brand;

//    冗余字段  用于存放商家的店铺名称
@Field(type = FieldType.Keyword, copyTo = "keyword")
    private String seller;
//    getter  和 setter

    /*
    * @Document(indexName = "pinyougou"  type item)
    * @Document  表示一个文档
    * indexName 指定索引名
    *
    * type 指定类型
    *
    * @Id 用于设置文档id 可以设置在数据的主键字段上
    * @Field（index = true analyzer‘ik_smart’store=true  searchAnalyzer='ik_smart' type=FieIdType.Text    ）
    *@field  用于表示字段
    *index 是否索引 默认为true
    *analyzer：索引时的分词器
    *searchAnalyzer ：搜索时的分词器
    *store 是否存储 默认是false 但是数据存储在es的——store中了
    * type  指定该字段的类型 比如文本类型  数据long类型 对象类型 （默认不用声明）
    * */

    @Field(index = true,type = FieldType.Object)
    private Map<String,String>specMap;

    public Map<String, String> getSpecMap() {
        return specMap;
    }

    public void setSpecMap(Map<String, String> specMap) {
        this.specMap = specMap;
    }

    public TbItem() {
    }

    public TbItem(Long id, String title, Long goodsId, String category, String brand, String seller) {
        this.id = id;
        this.title = title;
        this.goodsId = goodsId;
        this.category = category;
        this.brand = brand;
        this.seller = seller;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    @Override
    public String toString() {
        return "TbItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", goodsId=" + goodsId +
                ", category='" + category + '\'' +
                ", brand='" + brand + '\'' +
                ", seller='" + seller + '\'' +
                '}';
    }
}
