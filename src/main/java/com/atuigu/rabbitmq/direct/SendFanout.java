package com.atuigu.rabbitmq.direct;

import com.atuigu.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author zhou_qiang
 * 广播模式的生产者 这个模式需要先启动生产者
 * 在广播模式下，消息发送流程是这样的：
 *
 * - 1）  可以有多个消费者
 * - 2）  每个**消费者有自己的queue**（队列）
 * - 3）  每个**队列都要绑定到Exchange**（交换机）
 * - 4）  **生产者发送的消息，只能发送到交换机**，交换机来决定要发给哪个队列，生产者无法决定。
 * - 5）  交换机把消息发送给绑定过的所有队列
 * - 6）  队列的消费者都能拿到消息。实现一条消息被多个消费者消费
 * 消息会发送给所有绑定交换机的队列
 * @date 2020/3/17
 */
public class SendFanout {
    private final static String EXCHANGE_NAME = "fanout_exchange_test";

    public static void main(String[] argv) throws Exception {
        // 获取到连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取通道
        Channel channel = connection.createChannel();

        // 声明exchange，指定类型为fanout
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 消息内容
        String message = "Hello everyone";
        // 发布消息到Exchange
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println(" [生产者] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
