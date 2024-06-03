package com.adeo.summit.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenApiService {

    private final ChatClient chatClient;

    public OpenApiService(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }

    public String call(String message) {
        return chatClient.prompt().user(message).call().chatResponse().getResult().getOutput().getContent();
    }

}
