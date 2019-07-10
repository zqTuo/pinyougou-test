package com.pinyougou.search.listener;

import com.alibaba.fastjson.JSON;
import com.pinyougou.common.pojo.MessageInfo;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GoodsMessageListener implements MessageListenerConcurrently {
    @Autowired
    private ItemSearchService dao;



    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        System.out.println(">>>>>>>>>>>>接受数据"+ Thread.currentThread().getName());
        try {
            if (msgs!=null){
                for (MessageExt msg : msgs) {
    //                获取响应体
                    byte[] body = msg.getBody();
    //                转成的字符串
                    String s = new String(body);
//                    转成对象
                    MessageInfo messageInfo = JSON.parseObject(s, MessageInfo.class);
//                    判断对象中的执行类型，（add delete update）

                    switch (messageInfo.getMethods()){
                        case 1:  //新增
                        {

                            String context1 = messageInfo.getContext().toString();
                            List<TbItem> tbItemList = JSON.parseArray(context1, TbItem.class);
                            dao.updateIndex(tbItemList);
                            break;
                        }
                        case 2:  //更新
                        {
                            String context1 = messageInfo.getContext().toString();
                            List<TbItem> tbItemList = JSON.parseArray(context1, TbItem.class);
                            dao.updateIndex(tbItemList);
                            break;
                        }
                        case 3: //删除

                            String sw = messageInfo.getContext().toString();
                            //获取Long数组
                            Long[] longs = JSON.parseObject(sw, Long[].class);
                            dao.deleteByIds(longs);
                            break;
                    }

                }


            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }

}
