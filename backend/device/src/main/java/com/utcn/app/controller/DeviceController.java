package com.utcn.app.controller;
import com.utcn.app.config.UrlConfig;
import com.utcn.app.dto.CustomerDeviceDTO;
import com.utcn.app.dto.DeviceDTO;
import com.utcn.app.dto.ResponseDTO;
import com.utcn.app.service.DeviceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/devices")
@RequiredArgsConstructor
@CrossOrigin
public class DeviceController {
    private final DeviceServiceImpl deviceService;
    private final UrlConfig urlConfig;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin("http://localhost:3000/")
    public void createDevice(@RequestBody DeviceDTO deviceDTO) {
        log.info("new device registration {}", deviceDTO);
        deviceService.createDevice(deviceDTO);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    public List<DeviceDTO> getAllDevices() {
         return deviceService.getAllDevices();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    public ResponseDTO getAllDevicesByClientId(@PathVariable("id") Integer clientId) {
        return deviceService.getAllDevicesByClientId(clientId);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    public void updateDevice(@PathVariable("id") Integer deviceId,
                             @RequestBody DeviceDTO deviceDTO) {
        deviceDTO.setId(deviceId);
        deviceService.updateDevice(deviceDTO);
    }

    @PutMapping("/mapping")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    public void createMapping(@RequestBody CustomerDeviceDTO customerDeviceDTO) {
        deviceService.createMapping(customerDeviceDTO);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    public void deleteDevice(@PathVariable("id") Integer deviceId) {
        deviceService.deleteDevice(deviceId);
    }

    @GetMapping("/send")
    @CrossOrigin("http://localhost:3000/")
    public void sendDevicesToMicroservice() {
       deviceService.mapFromDevicesToStrings();
    }


}

