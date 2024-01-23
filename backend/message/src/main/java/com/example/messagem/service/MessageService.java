package com.example.messagem.service;

import com.example.messagem.dtos.MessageDTO;
import com.example.messagem.model.Message;
import com.example.messagem.repository.DeviceRepository;
import com.example.messagem.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    public final MessageRepository messageRepository;

    public void createNewMessage(Double energyConsumption, String time, Integer deviceId){
            Message messageObj = Message.builder()
                    .energyConsumption(energyConsumption)
                    .timestamp(time)
                    .deviceId(deviceId)
                    .build();
            messageRepository.save(messageObj);
            if(messageObj.getId()>1){
                Optional<Message> messageOptional = messageRepository.findById(messageObj.getId()-1);
                if(messageOptional.isPresent()){
                    Message message = messageOptional.get();
                    messageObj.setEnergyConsumption(energyConsumption-message.getEnergyConsumption());
                    messageRepository.save(messageObj);
                }
            }

    }

    public List<MessageDTO> getAllMessages(){
        List<Message> messages = messageRepository.findAll();
        return messages.stream().map(message -> MessageDTO.builder()
                .id(message.getId())
                .energyConsumption(message.getEnergyConsumption())
                .timestamp(message.getTimestamp())
                .deviceId(message.getDeviceId())
                .build()).collect(Collectors.toList());
    }

}
