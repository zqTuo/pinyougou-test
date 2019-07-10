package com.itheima.sms.listener;

import com.alibaba.fastjson.JSON;
import com.itheima.sms.util.SmsUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Map;

//监听器 用于监听消息 带哦用阿里大鱼的api发送短信
public class SMSMessageListener implements MessageListenerConcurrently {
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        try {
            if (msgs!=null){
                for (MessageExt msg : msgs) {
                    byte[] body = msg.getBody();
                    String ss = new String(body);
    //                获取到相关消息
                    Map<String,String> map = JSON.parseObject(ss, Map.class);//有签名和其他的消息
                    System.out.println(map);
                    SmsUtil.sendSms(map);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
