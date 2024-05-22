package com.adeo.summit.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PDFVectorStore {

    private final VectorStore vectorStore;


    public PDFVectorStore(EmbeddingClient embeddingClient, ResourceLoader resourceLoader) {
        this.vectorStore = new SimpleVectorStore(embeddingClient);
        Resource kitSFPPDF = resourceLoader.getResource("classpath:KIT_SFP.pdf");
        PagePdfDocumentReader reader = new PagePdfDocumentReader(kitSFPPDF);
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> documents = splitter.apply(reader.get());
        this.vectorStore.accept(documents);
    }

    public String getDocumentsFromVectorStore(String message) {
        List<Document> documents = this.vectorStore.similaritySearch(message);
        return documents.stream().map(Document::getContent).collect(Collectors.joining(System.lineSeparator()));
    }

}
