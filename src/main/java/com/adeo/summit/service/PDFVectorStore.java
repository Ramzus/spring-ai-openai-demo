package com.adeo.summit.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PDFVectorStore {

    private final VectorStore vectorStore;


    public PDFVectorStore(EmbeddingModel embeddingModel, ResourceLoader resourceLoader) {
        this.vectorStore = SimpleVectorStore.builder(embeddingModel).build();
        Resource PDF = resourceLoader.getResource("classpath:SFP.pdf");
        PagePdfDocumentReader reader = new PagePdfDocumentReader(PDF);
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> documents = splitter.apply(reader.get());
        this.vectorStore.accept(documents);
    }

    public VectorStore getVectorStore() {
        return vectorStore;
    }

}
