package com.jaruizes.pocs.springai.jobsmatcher.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chroma.ChromaApi;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.ChromaVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class Config {

    @Bean public EmbeddingModel embeddingModel() {
        return new OpenAiEmbeddingModel(new OpenAiApi(System.getenv("SPRING_AI_OPENAI_API_KEY")));
    }

/*    @Bean public VectorStore chromaVectorStore(EmbeddingModel embeddingModel, ChromaApi chromaApi) {
        return new ChromaVectorStore(embeddingModel, chromaApi, "chroma", true);
    }*/
}
