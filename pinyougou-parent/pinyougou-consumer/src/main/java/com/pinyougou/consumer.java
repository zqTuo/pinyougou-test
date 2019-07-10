package com.pinyougou;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

public class consumer {
    public static void main(String[] args) throws MQClientException {
//        1.创建消费者 并创建消费者租
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer_group1");
//        2.设置nameserver地址
        consumer.setNamesrvAddr("127.0.0.1:9876");
//        3.设置消费模式，默认就是集群模式
        consumer.setMessageModel(MessageModel.BROADCASTING);
//        4.设置（订阅）消费主题并执行消费的标签，  * 表示指定所有的标签
        consumer.subscribe("TopicTest", "*");

//        5.设置监听器，同时消费，不需要按住顺序来消费
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                if (msgs != null) {
                    for (MessageExt msg : msgs) {
                        byte[] body = msg.getBody();
                        String message = new String(body);
                        System.out.println(message + "%s %n" + msg.getTopic() + "%s %n" + msg.getTags());
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
//        发布消费
        consumer.start();
//        收回连接 关闭资源，关闭socket通道

    }

}
