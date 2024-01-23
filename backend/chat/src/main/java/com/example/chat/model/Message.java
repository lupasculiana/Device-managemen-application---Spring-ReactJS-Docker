package com.example.chat.model;

import lombok.*;

@Data
public class Message {
    private String senderName;
    private String receiverName;
    private String message;
    private Status status;
    private boolean seen;
}