package com.adeo.summit.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenApiService {

    private final ChatClient chatClient;

    public OpenApiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String call(String message) {
        return chatClient.call(message);
    }

}
