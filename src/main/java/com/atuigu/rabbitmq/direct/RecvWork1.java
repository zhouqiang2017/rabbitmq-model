package com.atuigu.rabbitmq.direct;

import com.atuigu.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * @author zhou_qiang
 * @date 2020/3/17
 */
public class RecvWork1 {
    private final static String QUEUE_NAME = "test_work_queue";

    public static void main(String[] argv) throws Exception {
        // 获取到连接
        Connection connection = ConnectionUtil.getConnection();
        // 创建通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicQos(1);
        // 定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // 获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
                                       byte[] body) throws IOException {
                // body 即消息体
                String msg = new String(body);
                System.out.println(" [work消费者1] received : " + msg + "!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 手动ACK需要的操作
                channel.basicAck(envelope.getDeliveryTag(), false);

            }
        };
        //(自动ACK) 监听队列，第二个参数：是否自动进行消息确认 （true 表示自定ACK消息确认）。
        // channel.basicConsume(QUEUE_NAME, true, consumer);
        // 手动ACK
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }
}
