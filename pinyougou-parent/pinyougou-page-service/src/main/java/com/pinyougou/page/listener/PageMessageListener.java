package com.pinyougou.page.listener;

import com.alibaba.fastjson.JSON;
import com.pinyougou.common.pojo.MessageInfo;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbItem;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

public class PageMessageListener implements MessageListenerConcurrently {

    @Autowired
    private ItemPageService itemPageService;
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        System.out.println(">>>>>>当前线程"+ Thread.currentThread().getName());

        if (msgs!=null) {
            for (MessageExt msg : msgs) {
//                获取到的是map对象  并不能直接序列化回来  需要直接转成字符串
                byte[] body = msg.getBody();
                MessageInfo info = JSON.parseObject(body, MessageInfo.class);

                switch (info.getMethods()){
                    case 1:  //新增
                    {
                        updatePageHtml(info);
                    }
                    case 2: //更新
                    {
                        updatePageHtml(info);
                    }
                    case 3:
                    {
                        String s = info.getContext().toString();
                        //获取Long数组
                        Long[] longs = JSON.parseObject(s, Long[].class);
                        itemPageService.deleteById(longs);
                        break;
                    }
                }
            }
        }


        return null;
    }

    private void updatePageHtml(MessageInfo info) {
        String context1 = info.getContext().toString(); //获取到的是map对象，并不能序列化回来，需要直接转成字符串

        List<TbItem> tbItems = JSON.parseArray(context1, TbItem.class);
        HashSet<Long> set = new HashSet<>();
        for (TbItem tbItem : tbItems) {
//            循环遍历进行生成静态页面页面
            set.add(tbItem.getGoodsId());

        }
//        循环遍历 生成静态页面
        for (Long aLong : set) {
            itemPageService.genItemHtml(aLong);
        }

    }
}
