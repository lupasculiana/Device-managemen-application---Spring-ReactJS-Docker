package com.example.messagem.service;

import com.example.messagem.model.Device;
import com.example.messagem.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceService {
    public final DeviceRepository deviceRepository;
    public final RestTemplate restTemplate;

    public void saveAllDevices(Integer deviceId, Integer maximumHourlyEnergyConsumption){
        Device device = Device.builder()
                .id(deviceId)
                .maximumHourlyEnergyConsumption(maximumHourlyEnergyConsumption)
                .energyConsumption(0.0)
                .build();
        deviceRepository.save(device);
    }

    public void updateDevice(Integer deviceId, Integer maxEnergyConsumption) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        if(optionalDevice.isPresent()){
           Device device = optionalDevice.get();
           device.setMaximumHourlyEnergyConsumption(maxEnergyConsumption);
           device.setId(deviceId);
           deviceRepository.save(device);
        }else {
            saveAllDevices(deviceId, maxEnergyConsumption);
        }

    }

    public void deleteDeviceConsumption(Integer deviceId) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
        Device device = optionalDevice.get();
        deviceRepository.delete(device);
    }

    public void updateDeviceConsumption(Integer deviceId, Double energyConsumption) {
        Optional<Device> optionalDevice = deviceRepository.findById(deviceId);
            Device device = optionalDevice.get();
            if(device.getEnergyConsumption()+energyConsumption<=device.getMaximumHourlyEnergyConsumption()){
                device.setEnergyConsumption(device.getEnergyConsumption()+energyConsumption);
                deviceRepository.save(device);
            }else{
                System.out.printf("Device " + deviceId + " has exceeded its maximum hourly energy consumption");
            }
    }
}
