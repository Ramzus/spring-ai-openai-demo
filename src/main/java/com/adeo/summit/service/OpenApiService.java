package com.adeo.summit.service;

import com.adeo.summit.config.ChatBotProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import com.adeo.summit.config.ChatBotProperties;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;

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
        return chatClient.prompt().user(message).call().chatResponse().getResult().getOutput().getContent();
    }

    public String callWithContext(String message) {

        SearchRequest searchRequest = SearchRequest.query(message);
        Message systemContext = new SystemPromptTemplate(chatBotProperties.getPromptTemplate()).createMessage(Map.of("name", chatBotProperties.getName(), "documents", inlineDocument));

        return chatClient.prompt()
                .user(message)
                .advisors(new QuestionAnswerAdvisor(PDFVectorStore.getVectorStore(), searchRequest))
                .call()
                .chatResponse().getResult().getOutput().getContent();
    }


}
