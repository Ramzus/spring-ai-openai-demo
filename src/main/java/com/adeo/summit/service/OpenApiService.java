package com.adeo.summit.service;

import com.adeo.summit.config.ChatBotProperties;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
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
    private final ContextStore contextStore;

    public OpenApiService(ChatClient chatClient, PDFVectorStore PDFVectorStore, ChatBotProperties chatBotProperties, ContextStore contextStore) {
        this.chatClient = chatClient;
        this.PDFVectorStore = PDFVectorStore;
        this.chatBotProperties = chatBotProperties;
        this.contextStore = contextStore;
    }

    public String call(String message) {
        return chatClient.call(message);
    }

    public String callWithContext(String message, String model, String contextId) {
        String inlineDocument = PDFVectorStore.getDocumentsFromVectorStore(message);

        UserMessage userMessage = new UserMessage(message);

        Message systemContext = new SystemPromptTemplate(chatBotProperties.getPromptTemplate()).createMessage(Map.of("name", chatBotProperties.getName(), "documents", inlineDocument));

        Message userContext = new SystemPromptTemplate(chatBotProperties.getUserContext()).createMessage(Map.of("userContext", contextStore.retrieveContext(contextId)));

        ChatResponse chatResponse = chatClient.call(new Prompt(List.of(systemContext, userContext, userMessage), OpenAiChatOptions.builder()
                .withModel(model)
                .build()));

        contextStore.addContext(contextId, List.of(userContext.getContent(), chatResponse.getResult().getOutput().getContent()));

        return  chatResponse.getResult().getOutput().getContent();
    }

}
