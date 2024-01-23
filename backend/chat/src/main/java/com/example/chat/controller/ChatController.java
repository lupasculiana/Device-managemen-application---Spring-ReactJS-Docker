package com.example.chat.controller;

import com.example.chat.model.Message;
import com.example.chat.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message") // app/mesage
    @SendTo("/chatroom/public")
    public Message receivePublicMessage(@Payload Message message){
        message.setSeen(true);
        return message;
    }

    @MessageMapping("/private-message")
    public Message receivePrivateMessage(@Payload Message message){
        message.setSeen(true);
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message); //user/Liana/private
        return message;
    }

    @MessageMapping("/typing")
    public void handleTyping(@Payload Message message) {
        if ("TYPING".equals(message.getStatus().toString())) {
            message.setStatus(Status.TYPING);
        } else if ("STOP_TYPING".equals(message.getStatus().toString())) {
            message.setStatus(Status.STOP_TYPING);
        }

        simpMessagingTemplate.convertAndSend("/chatroom/public", message);
        simpMessagingTemplate.convertAndSend("/user/" + message.getSenderName() + "/private", message);
    }

    @MessageMapping("/seen")
    public void handleSeen(@Payload Message message) {
        if ("SEEN".equals(message.getStatus().toString())) {
            message.setStatus(Status.SEEN);
        }
        message.setSeen(true);
        simpMessagingTemplate.convertAndSend("/chatroom/public", message);
        simpMessagingTemplate.convertAndSend("/user/" + message.getSenderName() + "/private", message);
    }
}