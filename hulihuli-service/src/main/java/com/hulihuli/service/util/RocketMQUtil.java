package com.hulihuli.service.util;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.CountDownLatch2;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.concurrent.TimeUnit;

public class RocketMQUtil {

    public static void syncSendMsg(DefaultMQProducer producer, Message msg) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        SendResult sendResult = producer.send(msg);
        System.out.println(sendResult);
    }

    public static void asyncSendMsg(DefaultMQProducer producer, Message msg) throws MQBrokerException, RemotingException, InterruptedException, MQClientException {
        int messageCount = 2;
        CountDownLatch2 countDownLatch2 = new CountDownLatch2(messageCount);
        for (int i = 0; i < messageCount; i++) {
           producer.send(msg, new SendCallback() {

               @Override
               public void onSuccess(SendResult sendResult) {
                   countDownLatch2.countDown();
                   System.out.println(sendResult.getMsgId());
               }

               @Override
               public void onException(Throwable throwable) {
                    countDownLatch2.countDown();
                   System.out.println("发送消息的时候发生了异常！" + throwable.getMessage());
                   throwable.printStackTrace();

               }
           });
        }
        countDownLatch2.await(5, TimeUnit.SECONDS);
    }

}
