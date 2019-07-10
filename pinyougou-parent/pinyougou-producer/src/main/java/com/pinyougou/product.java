package com.pinyougou;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class product {
    public static void main(String[] args) throws Exception {
//1.创建生产者 对象 并指定组名
        DefaultMQProducer producer = new DefaultMQProducer("producer_cluster_group1");

        //2.设置 nameserver的地址
        producer.setNamesrvAddr("127.0.0.1:9876");

        //3.开启连接 并使用
        producer.start();

        //4.发送消息

            //创建消息对象，并指定主题 标签 和消息体
            Message msg = new Message("TopicTest",
                    "TagA",
                    ("Hello RocketMQ" + "你好：" ).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //Call send message to deliver message to one of brokers.
            //发送消息到其中的一个broker中
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);

        producer.shutdown();
    }
}
