package com.example.messagem.rabbitmq;

import com.example.messagem.dtos.DeviceDTO;
import com.example.messagem.model.Device;
import com.example.messagem.model.Message;
import com.example.messagem.repository.DeviceRepository;
import com.example.messagem.repository.MessageRepository;
import com.example.messagem.service.DeviceService;
import com.example.messagem.service.MessageService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageReceiver {
    private final static String QUEUE_NAME = "hello";
    private final static String QUEUE_NAME2 = "hello2";
    private final static String QUEUE_NAME3 = "hello3";
    private final DeviceService deviceService;
    private final MessageService messageService;


    public void receiveMessageFromCsv() throws Exception {
       //declare queue
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("ltqdfidb");
        factory.setPassword("L9-wSESOpbjWOYE85jCR0sTnQHHGC7Hy");
        factory.setPort(5672);
        factory.setUri("amqps://ltqdfidb:L9-wSESOpbjWOYE85jCR0sTnQHHGC7Hy@toad.rmq.cloudamqp.com/ltqdfidb");
        factory.setVirtualHost("ltqdfidb");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages from csv reader. To exit press CTRL+C");
        //listen to message
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");

            //save information to db
            String[] messageParts = message.split(",");
            Double energyConsumption = Double.valueOf(messageParts[0]);
            String time = messageParts[1];
            Integer deviceId = Integer.valueOf(messageParts[2]);
            messageService.createNewMessage(energyConsumption, time, deviceId);

        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }

    public void syncDevices() throws Exception {
        //declare queue
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("ltqdfidb");
        factory.setPassword("L9-wSESOpbjWOYE85jCR0sTnQHHGC7Hy");
        factory.setPort(5672);
        factory.setUri("amqps://ltqdfidb:L9-wSESOpbjWOYE85jCR0sTnQHHGC7Hy@toad.rmq.cloudamqp.com/ltqdfidb");
        factory.setVirtualHost("ltqdfidb");
        factory.setHost("toad-01.rmq.cloudamqp.com");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME2, false, false, false, null);
        System.out.println(" [*] Waiting for messages from device. To exit press CTRL+C");
        //listen to message
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");

            //save information to db
            String[] messageParts = message.split(",");
            if(messageParts[0].equals("Delete")){
                Integer deviceId = Integer.valueOf(messageParts[1]);
                deviceService.deleteDeviceConsumption(deviceId);
            }else if(messageParts[0].equals("Update")){
                Integer deviceId = Integer.valueOf(messageParts[1]);
                Integer maxHourlyConsumption = Integer.valueOf(messageParts[2]);
                deviceService.updateDevice(deviceId, maxHourlyConsumption);
            }
        };
        channel.basicConsume(QUEUE_NAME2, true, deliverCallback, consumerTag -> { });
    }

    public void receiveMessageFromDevice() throws Exception {
        //declare queue
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("ltqdfidb");
        factory.setPassword("L9-wSESOpbjWOYE85jCR0sTnQHHGC7Hy");
        factory.setPort(5672);
        factory.setUri("amqps://ltqdfidb:L9-wSESOpbjWOYE85jCR0sTnQHHGC7Hy@toad.rmq.cloudamqp.com/ltqdfidb");
        factory.setVirtualHost("ltqdfidb");
        factory.setHost("toad-01.rmq.cloudamqp.com");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME3, false, false, false, null);
        System.out.println(" [*] Waiting for messages from device. To exit press CTRL+C");
        //listen to message
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");

            //save information to db
            String[] messageParts = message.split(",");
            Integer deviceId = Integer.valueOf(messageParts[0]);
            Integer energyConsumption = Integer.valueOf(messageParts[1]);
            deviceService.saveAllDevices(deviceId, energyConsumption);
        };
        channel.basicConsume(QUEUE_NAME3, true, deliverCallback, consumerTag -> { });
    }

}
