package com.jaruizes.pocs.springai.jobsmatcher.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.jaruizes.pocs.springai.jobsmatcher.service.model.Position;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobsMatcherServiceImpl implements JobsMatcherService {

    @Value("classpath:/promptTemplates/prompt.st") Resource questionPromptTemplate;

    private final VectorStore vectorStore;
    private final ChatClient chatClient;
    private final String directory = "./curriculums";

    public JobsMatcherServiceImpl(VectorStore chromaVectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = chromaVectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    @PostConstruct
    public void init() {
        Path path = Paths.get(directory);
        if (Files.exists(path)) {
            log.info("---------------------- Cargando ficheros -------------------------");
            try {
                Files.list(path)
                     .filter(file -> file.toString().endsWith(".pdf"))
                     .forEach(this::processCV);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override public void storeCurriculum(byte[] file, String name) {
        log.info("Almacenando CV {} en la base de datos vectorial", name);
        final List<Document> documents = this.extractTextFromPDF(file, name);

        // Añadimos los metadatos
        for (Document document : documents) {
            document.getMetadata().clear();
            document.getMetadata().put("name", name);
        }

        // Añadimos los documentos
        this.vectorStore.add(documents);
        log.info("CV {} almacenado correctamente", name);
    }

    @Override
    public String createPosition(Position position) {
        log.info("Creando posición {}", position.getTitle());




        return "Posición creada correctamente";
    }

    @Override
    public String getCVs(List<String> skills, double threshold) {
        final var skillsWithComma = skills.stream().collect(Collectors.joining(", "));
        SearchRequest searchRequest = SearchRequest.query(skillsWithComma)
                                                   .withTopK(5)
                                                   .withSimilarityThreshold(threshold);
        List<Document> similarDocs = vectorStore.similaritySearch(searchRequest);
        if (!similarDocs.isEmpty()) {
            List<String> results = new ArrayList<>();
            similarDocs.forEach(doc -> {
                String name = doc.getMetadata().get("name").toString();
                String result = evaluatePCVForPosition(skills, doc);

                log.info("CV {} porcentaje: {}", name, result);
                results.add(name + " - " + result);
            });

            return results.stream().collect(Collectors.joining(System.lineSeparator()));
        }

        return "No hay documentos";
    }

    private String evaluatePCVForPosition(List<String> skills, Document cv) {
        final var skillsWithComma = skills.stream().collect(Collectors.joining(", "));
        String answerText = chatClient.prompt()
                                      .system(systemSpec -> systemSpec
                                          .text(questionPromptTemplate)
                                          .param("skills", skillsWithComma)
                                          .param("cv", cv.getContent()))
                                      .user("¿Qué porcentaje de probabilidad de que el currículum sea válido para el puesto de trabajo?")
                                      .call()
                                      .content();

        return answerText;
    }

    private List<Document> extractTextFromPDF(byte[] pdfFile, String name) {
        log.info("Extrayendo texto del fichero PDF del CV {}", name);
        TikaDocumentReader documentReader = new TikaDocumentReader(new ByteArrayResource(pdfFile));
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();

        return tokenTextSplitter.apply(documentReader.read());
    }

    private void processCV(Path cv) {
        try {
            log.info("Procesando CV {}", cv.toString());

            String fileNameWithExtension = cv.getFileName().toString().replaceFirst("[.][^.]+$", "");
            this.storeCurriculum(Files.readAllBytes(cv), fileNameWithExtension);

            log.info("CV {} procesado correctamente", cv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
