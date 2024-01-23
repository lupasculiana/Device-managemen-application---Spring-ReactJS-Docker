package com.utcn.app.service;


import com.utcn.app.dto.CustomerDeviceDTO;
import com.utcn.app.dto.DeviceDTO;
import com.utcn.app.dto.ResponseDTO;

import java.util.List;

public interface DeviceService {
    void createDevice(DeviceDTO deviceDTO);

    DeviceDTO getDeviceById(Integer deviceId);

    void deleteDevice(Integer deviceId);

    void updateDevice(DeviceDTO deviceDTO);

    ResponseDTO getAllDevicesByClientId(Integer clientId);

    void createMapping(CustomerDeviceDTO mappingDTO);

    List<DeviceDTO> getAllDevices();

    void mapFromDevicesToStrings();

    void sendMessage(String message) throws Exception;

    void sendUpdateMessage(String message, Integer deviceId, Integer maxHourlyConsumption) throws Exception;

}
