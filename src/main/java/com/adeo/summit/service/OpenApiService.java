package com.adeo.summit.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenApiService {

    private final ChatClient chatClient;
    private final PDFVectorStore PDFVectorStore;

    public OpenApiService(ChatClient.Builder chatClient, PDFVectorStore pdfVectorStore) {
        this.chatClient = chatClient.build();
        this.PDFVectorStore = pdfVectorStore;
    }

    public String call(String message) {
        return chatClient.prompt().user(message).call().chatResponse().getResult().getOutput().getContent();
    }

    public String callWithContext(String message) {

        SearchRequest searchRequest = SearchRequest.query(message);

        Message context = new SystemPromptTemplate("Based on the following: {documents}").createMessage(Map.of("documents", inlineDocument));
        UserMessage userMessage = new UserMessage(message);


        return chatClient.prompt()
                .user(message)
                .advisors(new QuestionAnswerAdvisor(PDFVectorStore.getVectorStore(), searchRequest))
                .call()
                .chatResponse().getResult().getOutput().getContent();
    }


}
