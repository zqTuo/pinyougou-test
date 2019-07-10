package com.pinyougou.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class MyMessageListener2 implements MessageListenerConcurrently {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            if (msgs!=null){
                for (MessageExt msg : msgs) {
                    System.out.println("主题：2222222222222222"+msg.getTopic());
                    System.out.println(new String(msg.getBody()));
                }
            }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
