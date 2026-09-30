package com.kharcha.processor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Phase 0 smoke test: one prompt to the local Ollama model through Spring AI.
 * Excluded from {@code build}; run with {@code ./gradlew :processor:ollamaSmoke}.
 */
@Tag("ollama")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("local")
class OllamaSmokeTest {

    @Autowired
    ChatClient.Builder chatClientBuilder;

    @Test
    void localModelAnswers() {
        String answer = chatClientBuilder.build()
                .prompt()
                .user("Reply with the single word: pong")
                .call()
                .content();

        assertThat(answer).isNotBlank();
    }
}
