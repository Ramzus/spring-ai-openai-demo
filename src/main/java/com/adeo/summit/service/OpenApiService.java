package com.adeo.summit.service;

import com.adeo.summit.config.ChatBotProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@Service
public class OpenApiService {

    private final ChatClient chatClient;
    private final PDFVectorStore PDFVectorStore;
    private final WebhookCall webhookCall;

    public OpenApiService(ChatClient.Builder chatClientBuilder, PDFVectorStore PDFVectorStore, ChatBotProperties chatBotProperties, WebhookCall webhookCall) {
        this.PDFVectorStore = PDFVectorStore;
        this.webhookCall = webhookCall;
        InMemoryChatMemory chatMemory = new InMemoryChatMemory();

        Message systemTemplate = new SystemPromptTemplate(chatBotProperties.getPromptTemplate()).createMessage(Map.of("name", chatBotProperties.getName()));

        this.chatClient = chatClientBuilder
                .defaultSystem(systemTemplate.getText())
                .defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory))
                .build();
    }

    public String call(String message) {
        return chatClient.prompt().user(message).call().chatResponse().getResult().getOutput().getText();
    }

    public String callWithContext(String message, String contextId) {

        ChatResponse
                chatResponse = chatClient.prompt()
                .user(message)
                .advisors(
                        advisor ->
                                advisor.param(CHAT_MEMORY_CONVERSATION_ID_KEY, contextId)
                                        .advisors(new QuestionAnswerAdvisor(PDFVectorStore.getVectorStore())))
                .tools(FunctionToolCallback.builder("createIncident", webhookCall).description("create incident for an application").inputType(WebhookCall.Request.class).build())
                .call()
                .chatResponse();

        return chatResponse.getResult().getOutput().getText();
    }


}
