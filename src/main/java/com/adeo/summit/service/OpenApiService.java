package com.adeo.summit.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OpenApiService {

    private final ChatClient chatClient;
    private final PDFVectorStore PDFVectorStore;

    public OpenApiService(ChatClient chatClient, PDFVectorStore PDFVectorStore) {
        this.chatClient = chatClient;
        this.PDFVectorStore = PDFVectorStore;
    }

    public String call(String message) {
        return chatClient.call(message);
    }

    public String callWithContext(String message) {
        String inlineDocument = PDFVectorStore.getDocumentsFromVectorStore(message);

        Message context = new SystemPromptTemplate("Based on the following: {documents}").createMessage(Map.of("documents", inlineDocument));
        UserMessage userMessage = new UserMessage(message);


        return chatClient.call(new Prompt(List.of(context, userMessage))).getResult().getOutput().getContent();
    }

}
