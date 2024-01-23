package com.utcn.app.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.utcn.app.dto.CustomerDeviceDTO;
import com.utcn.app.dto.DeviceDTO;
import com.utcn.app.dto.ResponseDTO;
import com.utcn.app.mapping.DeviceMapper;
import com.utcn.app.model.CustomerDevice;
import com.utcn.app.model.Device;
import com.utcn.app.repository.CustomerDeviceRepository;
import com.utcn.app.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final CustomerDeviceRepository customerDeviceRepository;
    private final static String QUEUE_NAME2 = "hello2";
    private final static String QUEUE_NAME3 = "hello3";
    private Integer deviceId = 0;

    @Override
    public void createDevice(DeviceDTO deviceDTO) {
        Device device = DeviceMapper.mapToDevice(deviceDTO);
        deviceRepository.save(device);
        try {
            sendUpdateMessage("Update",deviceId, device.getMaximumHourlyEnergyConsumption());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        deviceId++;
    }

    @Override
    public DeviceDTO getDeviceById(Integer deviceId) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        Device device = optionalDevice.get();

        return DeviceMapper.mapToDeviceDto(device);
    }

    @Override
    public List<DeviceDTO> getAllDevices() {
        List<Device> devices = deviceRepository.findAll();
        return devices.stream().map(DeviceMapper::mapToDeviceDto).collect(Collectors.toList());
    }

    @Override
    public ResponseDTO getAllDevicesByClientId(Integer clientId) {
        ResponseDTO responseDTO = new ResponseDTO();

        Optional<List<CustomerDevice>> optionalCustomerDevice = customerDeviceRepository.findCustomerDevicesByCustomerId(clientId);
        List<CustomerDevice> customerDevices = optionalCustomerDevice.get();

        responseDTO.setDevice(customerDevices.stream()
                .map(customerDevice -> getDeviceById(customerDevice.getDeviceId()))
                .collect(Collectors.toList()));

//        ResponseEntity<CustomerDTO> responseEntity = restTemplate
//                .getForEntity("http://localhost:8081/api/v1/customers/" + clientId,
//                        CustomerDTO.class);
//
//        responseDTO.setCustomer(responseEntity.getBody());

        return responseDTO;
    }


    @Override
    public void deleteDevice(Integer deviceId) {
        boolean exists = deviceRepository.existsById(deviceId);
        if (!exists) {
            throw new IllegalStateException(
                    "device with id " + deviceId + " does not exist!");
        }
        deviceRepository.deleteById(deviceId);

        try {
            sendUpdateMessage("Delete",deviceId, 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //delete mapping
        boolean existsMapping = customerDeviceRepository.existsByDeviceId(deviceId);
        if(existsMapping){
            Optional<CustomerDevice> optionalCustomerDevice = customerDeviceRepository.findCustomerDeviceByDeviceId(deviceId);
            CustomerDevice customerDevice = optionalCustomerDevice.get();
            customerDeviceRepository.delete(customerDevice);
        }
    }

    @Override
    public void updateDevice(DeviceDTO deviceDTO) {
        Device device = deviceRepository.findById(deviceDTO.getId()).get();
        device.setName(deviceDTO.getName());
        device.setAddress(deviceDTO.getAddress());
        device.setMaximumHourlyEnergyConsumption(deviceDTO.getMaximumHourlyEnergyConsumption());
        deviceRepository.save(device);
        try {
            sendUpdateMessage("Update",device.getId(), device.getMaximumHourlyEnergyConsumption());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createMapping(CustomerDeviceDTO mappingDTO){
        CustomerDevice customerDevice = CustomerDevice.builder()
                .deviceId(mappingDTO.getDeviceId())
                .customerId(mappingDTO.getCustomerId())
                .build();

        customerDeviceRepository.save(customerDevice);
    }

    @Override
    public void mapFromDevicesToStrings() {
        List<DeviceDTO> deviceDTOList = getAllDevices();
        for(DeviceDTO device: deviceDTOList){
            String message = device.getId() + "," + device.getMaximumHourlyEnergyConsumption();
            try {
                sendMessage(message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
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
            channel.queueDeclare(QUEUE_NAME3, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME3, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }

    @Override
    public void sendUpdateMessage(String action, Integer deviceId, Integer maxHourlyConsumption) throws Exception{
        //conect to RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("ltqdfidb");
        factory.setPassword("L9-wSESOpbjWOYE85jCR0sTnQHHGC7Hy");
        factory.setPort(5672);
        factory.setUri("amqps://ltqdfidb:L9-wSESOpbjWOYE85jCR0sTnQHHGC7Hy@toad.rmq.cloudamqp.com/ltqdfidb");
        factory.setVirtualHost("ltqdfidb");

        String message = action + "," + deviceId + "," + maxHourlyConsumption;
        try (Connection connection = factory.newConnection();
             //send message to queue
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME2, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME2, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}

