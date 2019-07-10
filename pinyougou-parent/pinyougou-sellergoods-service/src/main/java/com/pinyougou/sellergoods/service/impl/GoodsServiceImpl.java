package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.core.service.impl.CoreServiceImpl;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.GoodsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class GoodsServiceImpl extends CoreServiceImpl<TbGoods> implements GoodsService {


    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbBrandMapper brandMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbSellerMapper sellerMapper;

    @Autowired
    public GoodsServiceImpl(TbGoodsMapper goodsMapper) {
        super(goodsMapper, TbGoods.class);
        this.goodsMapper = goodsMapper;
    }


    @Override
    public PageInfo<TbGoods> findPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<TbGoods> all = goodsMapper.selectAll();
        PageInfo<TbGoods> info = new PageInfo<TbGoods>(all);

        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbGoods> pageInfo = JSON.parseObject(s, PageInfo.class);
        return pageInfo;
    }


    @Override
    public PageInfo<TbGoods> findPage(Integer pageNo, Integer pageSize, TbGoods goods) {
        PageHelper.startPage(pageNo, pageSize);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDelete", false);//只查询没有被删除的

        if (goods != null) {
            if (StringUtils.isNotBlank(goods.getSellerId())) {
                criteria.andEqualTo("sellerId", goods.getSellerId());
                //criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
            }
            if (StringUtils.isNotBlank(goods.getGoodsName())) {
                criteria.andLike("goodsName", "%" + goods.getGoodsName() + "%");
                //criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
            }
            if (StringUtils.isNotBlank(goods.getAuditStatus())) {
                criteria.andLike("auditStatus", "%" + goods.getAuditStatus() + "%");
                //criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
            }
            if (StringUtils.isNotBlank(goods.getIsMarketable())) {
                criteria.andLike("isMarketable", "%" + goods.getIsMarketable() + "%");
                //criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
            }
            if (StringUtils.isNotBlank(goods.getCaption())) {
                criteria.andLike("caption", "%" + goods.getCaption() + "%");
                //criteria.andCaptionLike("%"+goods.getCaption()+"%");
            }
            if (StringUtils.isNotBlank(goods.getSmallPic())) {
                criteria.andLike("smallPic", "%" + goods.getSmallPic() + "%");
                //criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
            }
            if (StringUtils.isNotBlank(goods.getIsEnableSpec())) {
                criteria.andLike("isEnableSpec", "%" + goods.getIsEnableSpec() + "%");
                //criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
            }

        }
        List<TbGoods> all = goodsMapper.selectByExample(example);
        PageInfo<TbGoods> info = new PageInfo<TbGoods>(all);
        //序列化再反序列化
        String s = JSON.toJSONString(info);
        PageInfo<TbGoods> pageInfo = JSON.parseObject(s, PageInfo.class);

        return pageInfo;
    }

    @Override
    public void add(Goods goods) {
//		1.获取goods
        TbGoods tbGoods = goods.getGoods();
        tbGoods.setAuditStatus("0");
        tbGoods.setIsDelete(false);
        goodsMapper.insert(tbGoods);

//		2.获取goodsDesc
        TbGoodsDesc goodsDesc = goods.getGoodsDesc();
        goodsDesc.setGoodsId(tbGoods.getId());
        goodsDescMapper.insert(goodsDesc);

//		3.获取skuList todo
        List<TbItem> itemList = goods.getItemList();


//		4.插入3个表中
        for (TbItem tbItem : itemList) {
//			设置title spu名 + 空格 +规格名称+
            String spec = tbItem.getSpec();   //{"网络":"移动4G","机身内存":"16G"}
            String title = tbGoods.getGoodsName();

            Map<String, String> map = JSON.parseObject(spec, Map.class);

            for (String key : map.keySet()) {
                title += map.get(key);
            }
            tbItem.setTitle(title);

//			设置图片地址
            //[{"color":"白色","url":"http://192.168.25.133/group1/M00/00/00/wKgZhVmOZxyAfH5xAAFa4hmtWek092.jpg"}]
            List<Map> maps = JSON.parseArray(goodsDesc.getItemImages(), Map.class);
            String image = maps.get(0).get("url").toString();
            tbItem.setImage(image);

//			设置分类
            tbItem.setCategoryid(tbGoods.getCategory3Id());
//			tbGoods 对应该表的分类id
            TbItemCat cat = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
            tbItem.setCategory(cat.getName());

//			设置时间
            tbItem.setCreateTime(new Date());
            tbItem.setCreateTime(tbItem.getCreateTime());

//			设置外键
            tbItem.setGoodsId(tbGoods.getId());

//			设置商家
            tbItem.setSellerId(tbGoods.getSellerId());
            TbSeller tbSeller = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId());

            tbItem.setSellerId(tbSeller.getNickName());  //商家店铺名

            TbBrand tbBrand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());
            tbItem.setBrand(tbBrand.getName());

//			添加到数据中
            itemMapper.insert(tbItem);


        }
    }

    @Override
    public Goods findOne(Long id) {
        Goods goods = new Goods();
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);

        TbItem tbItem = new TbItem();
        tbItem.setGoodsId(id);
        List<TbItem> tbItemList = itemMapper.select(tbItem);

        goods.setGoods(tbGoods);
        goods.setGoodsDesc(tbGoodsDesc);
        goods.setItemList(tbItemList);

        return goods;
    }

    /*
     *
     * 修改
     * */

    @Override
    public void update(Goods goods) {
        TbGoods tbGoods = goods.getGoods();
        tbGoods.setAuditStatus("0");
        goodsMapper.updateByPrimaryKey(tbGoods);

//		更新描述
        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());

//		更新sku  先删除原来的spuid对应的sku的列表
        TbItem record = new TbItem();
        record.setGoodsId(tbGoods.getId());
        itemMapper.delete(record);
//		新增就可以了，这里也要判断是否为启用的状态
//		插入到3个表中
        saveItems(goods, tbGoods, goods.getGoodsDesc());
    }


    public List<TbItem> findTbItemByIds(long[] ids) {

        Example example =new Example(TbItem.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("goodsId",Arrays.asList(ids)).andEqualTo("status","1");
        return itemMapper.selectByExample(example);
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        TbGoods record= new TbGoods();
        record.setAuditStatus(status);
        Example example = new Example(TbGoods.class);
        example.createCriteria().andIn("id", Arrays.asList(ids));
        goodsMapper.updateByExampleSelective(record,example);//update set status=1 where id in (12,3)
    }





    @Override
    public List<TbItem> findTbItemListByIds(Long[] ids) {
        Example example = new Example(TbItem.class);
        example.createCriteria().andIn("goodsId",Arrays.asList(ids)).andEqualTo("status","1");
        return itemMapper.selectByExample(example);
    }

    private void saveItems(Goods goods, TbGoods tbGoods, TbGoodsDesc goodsDesc) {
//			要启用规格
        if ("1".equals(goods.getGoods().getIsEnableSpec())) {
            List<TbItem> itemList = goods.getItemList();
            for (TbItem tbItem : itemList) {
                //设置title  SPU名称+ “ ”+ 规格的 选项名
                String title = tbGoods.getGoodsName();
                String spec = tbItem.getSpec();   //{ "网络": "移动3G", "机身内存": "32G" }
                Map<String, String> map = JSON.parseObject(spec, Map.class);
                for (String key : map.keySet()) {
                    title += "" + map.get(key);
                }
                tbItem.setTitle(title);

                //设置图片从goodsDesc中获取
                //[{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/03/wKgZhVq7N-qAEDgSAAJfMemqtP8461.jpg"}]
                String itemImages = goodsDesc.getItemImages();//

                List<Map> maps = JSON.parseArray(itemImages, Map.class);

                String url = maps.get(0).get("url").toString();//图片的地址
                tbItem.setImage(url);

                //设置分类
                TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
                tbItem.setCategoryid(tbItemCat.getId());
                tbItem.setCategory(tbItemCat.getName());

                //时间
                tbItem.setCreateTime(new Date());
                tbItem.setUpdateTime(new Date());

                //设置SPU的ID
                tbItem.setGoodsId(tbGoods.getId());

                //设置商家
                TbSeller tbSeller = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
                System.out.println(tbSeller);
                tbItem.setSellerId(tbSeller.getSellerId());
                tbItem.setSeller(tbSeller.getNickName());//店铺名

                //设置品牌明后
                TbBrand tbBrand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());
                tbItem.setBrand(tbBrand.getName());
                itemMapper.insert(tbItem);
            }
        } else {
            //插入到SKU表 一条记录
            TbItem tbItem = new TbItem();
            tbItem.setTitle(tbGoods.getGoodsName());
            tbItem.setPrice(tbGoods.getPrice());
            tbItem.setNum(999);//默认一个
            tbItem.setStatus("1");//正常启用
            tbItem.setIsDefault("1");//默认的

            tbItem.setSpec("{}");


            //设置图片从goodsDesc中获取
            //[{"color":"黑色","url":"http://192.168.25.133/group1/M00/00/03/wKgZhVq7N-qAEDgSAAJfMemqtP8461.jpg"}]
            String itemImages = goodsDesc.getItemImages();//

            List<Map> maps = JSON.parseArray(itemImages, Map.class);

            String url = maps.get(0).get("url").toString();//图片的地址
            tbItem.setImage(url);

            //设置分类
            TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id());
            tbItem.setCategoryid(tbItemCat.getId());
            tbItem.setCategory(tbItemCat.getName());

            //时间
            tbItem.setCreateTime(new Date());
            tbItem.setUpdateTime(new Date());

            //设置SPU的ID
            tbItem.setGoodsId(tbGoods.getId());

            //设置商家
            TbSeller tbSeller = sellerMapper.selectByPrimaryKey(tbGoods.getSellerId());
            tbItem.setSellerId(tbSeller.getSellerId());
            tbItem.setSeller(tbSeller.getNickName());//店铺名

            //设置品牌明后
            TbBrand tbBrand = brandMapper.selectByPrimaryKey(tbGoods.getBrandId());
            tbItem.setBrand(tbBrand.getName());
            itemMapper.insert(tbItem);

        }
    }

    //    逻辑删除
    public void delete(Object[] ids) {
        Example example = new Example(TbGoods.class);

        Long[] issss = new Long[ids.length];
        for (int i = 0; i < ids.length; i++) {
            issss[i]=(Long)ids[i];
        }
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(issss));
        TbGoods tbGoods = new TbGoods();
        tbGoods.setIsDelete(true);
//        更新的值

        goodsMapper.updateByExampleSelective(tbGoods, example);
    }


}
