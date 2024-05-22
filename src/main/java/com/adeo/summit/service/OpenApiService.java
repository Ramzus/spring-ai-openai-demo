package com.adeo.summit.service;

import com.adeo.summit.config.ChatBotProperties;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OpenApiService {

    private final ChatClient chatClient;
    private final PDFVectorStore PDFVectorStore;
    private final ChatBotProperties chatBotProperties;

    public OpenApiService(ChatClient chatClient, PDFVectorStore PDFVectorStore, ChatBotProperties chatBotProperties) {
        this.chatClient = chatClient;
        this.PDFVectorStore = PDFVectorStore;
        this.chatBotProperties = chatBotProperties;
    }

    public String call(String message) {
        return chatClient.call(message);
    }

    public String callWithContext(String message, String model) {
        String inlineDocument = PDFVectorStore.getDocumentsFromVectorStore(message);

        UserMessage userMessage = new UserMessage(message);

        Message systemContext = new SystemPromptTemplate(chatBotProperties.getPromptTemplate()).createMessage(Map.of("name", chatBotProperties.getName(), "documents", inlineDocument));

        return chatClient.call(new Prompt(List.of(systemContext, userMessage), OpenAiChatOptions.builder()
                .withModel(model)
                .build())).getResult().getOutput().getContent();
    }

}
