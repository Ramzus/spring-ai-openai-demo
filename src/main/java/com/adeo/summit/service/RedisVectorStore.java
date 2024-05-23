package com.adeo.summit.service;

import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.VectorStore;

public class RedisVectorStore {

    private final VectorStore vectorStore;

    public RedisVectorStore(EmbeddingClient embeddingClient) {
        org.springframework.ai.vectorstore.RedisVectorStore.RedisVectorStoreConfig vectorStoreConfig = org.springframework.ai.vectorstore.RedisVectorStore.RedisVectorStoreConfig.builder()
                .withURI("redis://localhost:6379")
                .build();

        this.vectorStore = new org.springframework.ai.vectorstore.RedisVectorStore(vectorStoreConfig, embeddingClient);
    }

}
