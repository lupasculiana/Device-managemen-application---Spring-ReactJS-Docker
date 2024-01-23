import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


import java.nio.charset.StandardCharsets;

public class MessageSender {
    private final static String QUEUE_NAME = "hello";

    public void sendMessage(String message) throws Exception {
        //conect to RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("ltqdfidb");
        factory.setPassword("L9-wSESOpbjWOYE85jCR0sTnQHHGC7Hy");
        factory.setPort(5672);
        factory.setUri("amqps://ltqdfidb:L9-wSESOpbjWOYE85jCR0sTnQHHGC7Hy@toad.rmq.cloudamqp.com/ltqdfidb");
        factory.setVirtualHost("ltqdfidb");

        try (Connection connection = factory.newConnection();
             //send message to queue
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
