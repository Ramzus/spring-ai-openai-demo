package com.adeo.summit.service;

import com.adeo.summit.config.ChatBotProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenApiService {

    private final ChatClient chatClient;
    private final PDFVectorStore PDFVectorStore;
    private final ChatBotProperties chatBotProperties;

    public OpenApiService(ChatClient.Builder chatClient, PDFVectorStore pdfVectorStore, ChatBotProperties chatBotProperties) {
        this.chatClient = chatClient.build();
        this.PDFVectorStore = pdfVectorStore;
        this.chatBotProperties = chatBotProperties;
    }

    public String call(String message) {
        return chatClient.prompt().user(message).call().chatResponse().getResult().getOutput().getText();
    }

    public String callWithContext(String message) {

        Message systemTemplate = new SystemPromptTemplate(chatBotProperties.getPromptTemplate()).createMessage(Map.of("name", chatBotProperties.getName()));

        return chatClient.prompt()
                .user(message)
                .system(systemTemplate.getText())
                .advisors(new QuestionAnswerAdvisor(PDFVectorStore.getVectorStore()))
                .call()
                .chatResponse().getResult().getOutput().getText();
    }


}
