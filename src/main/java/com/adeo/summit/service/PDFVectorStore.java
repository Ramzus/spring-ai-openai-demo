package com.adeo.summit.service;

import com.adeo.summit.config.ResourceProperties;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PDFVectorStore {

    private final VectorStore vectorStore;

    public PDFVectorStore(VectorStore vectorStore, ResourceProperties resourceProperties) {
        this.vectorStore = vectorStore;
        for (Resource resource : resourceProperties.getResources()) {
            PagePdfDocumentReader reader = new PagePdfDocumentReader(resource);
            TokenTextSplitter splitter = new TokenTextSplitter();
            List<Document> documents = splitter.apply(reader.get());
            this.vectorStore.accept(documents);
        }
    }

    public VectorStore getVectorStore() {
        return vectorStore;
    }

}
