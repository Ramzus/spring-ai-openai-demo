package com.adeo.summit.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.stereotype.Service;

@Service
public class OpenApiService {

    private final ChatClient chatClient;
    private final PDFVectorStore PDFVectorStore;

    public OpenApiService(ChatClient.Builder chatClient, PDFVectorStore pdfVectorStore) {
        this.chatClient = chatClient.build();
        this.PDFVectorStore = pdfVectorStore;
    }

    public String call(String message) {
        return chatClient.prompt().user(message).call().chatResponse().getResult().getOutput().getText();
    }

    public String callWithContext(String message) {

        return chatClient.prompt()
                .user(message)
                .advisors(new QuestionAnswerAdvisor(PDFVectorStore.getVectorStore()))
                .call()
                .chatResponse().getResult().getOutput().getText();
    }


}
