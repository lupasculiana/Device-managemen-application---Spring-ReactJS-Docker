package com.example.messagem.controller;

import com.example.messagem.dtos.MessageDTO;
import com.example.messagem.rabbitmq.MessageReceiver;
import com.example.messagem.service.DeviceService;
import com.example.messagem.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.DefaultErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/messages")
@RequiredArgsConstructor
@CrossOrigin
public class MessageController {
     private final MessageReceiver messageReceiver;
    private final MessageService messageService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin("http://localhost:3000/")
    public List<MessageDTO> getAllMessages() {
       return messageService.getAllMessages();
    }

     @PostMapping("/receivecsv")
     @CrossOrigin("http://localhost:3000/")
     public void createCsvQueue() {
         try {
             messageReceiver.receiveMessageFromCsv();
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
     }

    @PostMapping("/receivedevice")
    @CrossOrigin("http://localhost:3000/")
    public void createDeviceQueue() {
        try {
            messageReceiver.receiveMessageFromDevice();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/changedevice")
    @CrossOrigin("http://localhost:3000/")
    public void updateDeviceConsumption() {
        try {
            messageReceiver.syncDevices();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
