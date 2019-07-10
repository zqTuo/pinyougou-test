package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import tk.mybatis.mapper.entity.Example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemPageServiceImpl implements ItemPageService {

    @Autowired
    TbGoodsMapper tbGoodsMapper;

    @Autowired
    TbGoodsDescMapper goodsDescMapper;

    @Autowired
    FreeMarkerConfigurer configurer;
    @Autowired
    TbItemCatMapper tbItemCatMapper;
    @Autowired
    TbItemMapper tbItemMapper;

    @Value("${pageDir}")
    private String pageDir;

    @Override
    public void genItemHtml(Long goodsId) {
//        查询数据库的商品的数据 生成静态页面

//        1.根据spu的id  查询商品的信息（goods goodsDesc）
        TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(goodsId);
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);

//        使用freemarker  创建模板 使用数据集生成静态页面（数据集 和模板）
        genHTML("item.ftl", tbGoods, tbGoodsDesc);

    }

    @Override
    public void deleteById(Long[] goodsId) {
            try {
        for (Long aLong : goodsId) {
                FileUtils.forceDelete(new File(pageDir+aLong+".html"));

            }
            } catch (IOException e) {
                e.printStackTrace();
        }

    }


    private void genHTML(String templateName, TbGoods tbGoods, TbGoodsDesc tbGoodsDesc) {
        FileWriter writer = null;

        try {
//        创建一个configuration对象
//        设置字符编码 和模板加载的目录
            Configuration configuration = configurer.getConfiguration();
//        获取模板对象
            Template template = configuration.getTemplate(templateName);
//            获取数据集
            Map model = new HashMap();
            model.put("tbGoods", tbGoods);
            model.put("tbGoodsDesc", tbGoodsDesc);

//根据分类的ID 查询分类的对象
            TbItemCat tbItemCat1 = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id());
            TbItemCat tbItemCat2 = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id());
            TbItemCat tbItemCat3 = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
            model.put("tbItemCat1", tbItemCat1.getName());
            model.put("tbItemCat2", tbItemCat2.getName());
            model.put("tbItemCat3", tbItemCat3.getName());

            /*查询商品的spu的对应的所有的sku列表数据
             * select / *  from tb_item where goods_id=1 and status=1 order by is_default desc
             * */

            Example example = new Example(TbItem.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("goodsId", tbGoods.getId());
            criteria.andEqualTo("status", "1");
            example.setOrderByClause("is_default desc");//order by  is_default desc
            List<TbItem> tbItemList = tbItemMapper.selectByExample(example);
            model.put("skuList", tbItemList);

//           创建一个写流
            writer = new FileWriter(new File(pageDir + tbGoods.getId() + ".html"));
//            6.调用模板对象的process 方法输出到指定文件中
            template.process(model, writer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            关闭流
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
