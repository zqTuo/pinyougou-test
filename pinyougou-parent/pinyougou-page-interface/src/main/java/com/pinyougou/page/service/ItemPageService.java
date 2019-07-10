package com.pinyougou.page.service;

public interface ItemPageService {
//    生成商品详细页
    public void genItemHtml(Long goodsId);

    void deleteById(Long[] longs);
}
