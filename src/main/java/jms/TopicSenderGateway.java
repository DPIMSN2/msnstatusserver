package jms;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by Kevin.
 */
public class TopicSenderGateway {
    private static TopicSenderGateway instance;
    private static ConnectionFactory connectionFactory;
    private static Connection connection;
    private static Channel channel;
    private static final String EXCHANGE_NAME = "statusUpdate";

    private TopicSenderGateway() {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try {
            connection = connectionFactory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TopicSenderGateway getInstance() {
        if (instance == null) {
            instance = new TopicSenderGateway();
        }
        return instance;
    }

    public static void publishMessage(String message, String routingKey) {
        try {
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
