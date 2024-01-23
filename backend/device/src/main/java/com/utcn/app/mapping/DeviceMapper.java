package com.utcn.app.mapping;

import com.utcn.app.dto.DeviceDTO;
import com.utcn.app.model.Device;

public class DeviceMapper {
    public static DeviceDTO mapToDeviceDto(Device device) {
        return new DeviceDTO(
                device.getId(),
                device.getName(),
                device.getAddress(),
                device.getMaximumHourlyEnergyConsumption()
        );
    }

    public static Device mapToDevice(DeviceDTO deviceDTO) {
        return Device.builder()
                .name(deviceDTO.getName())
                .address(deviceDTO.getAddress())
                .maximumHourlyEnergyConsumption(deviceDTO.getMaximumHourlyEnergyConsumption())
                .build();
    }

}

