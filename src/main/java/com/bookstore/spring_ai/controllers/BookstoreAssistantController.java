package com.bookstore.spring_ai.controllers;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/bookstore")
public class BookstoreAssistantController {

    private final OpenAiChatModel chatClient;

    public BookstoreAssistantController(OpenAiChatModel chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/informations")
    public String bookstoreChat(@RequestParam(
            value = "message",
            defaultValue = "Quais são os livros best sellers dos últimos anos?") String message)
    {
        return chatClient.call(message);
    }

    @GetMapping("/informations/prompt")
    public ChatResponse bookstoreChatPrompt(@RequestParam(
            value = "message",
            defaultValue = "Quais são os livros best sellers dos últimos anos?") String message)
    {
        return chatClient.call(new Prompt(message));
    }

    @GetMapping("/informations/stream")
    public Flux<String> bookstoreChatStream(@RequestParam(
            value = "message",
            defaultValue = "Quais são os livros best sellers dos últimos anos?") String message)
    {
        return chatClient.stream(message);
    }

    @GetMapping("/reviews")
    public String bookstoreReview(@RequestParam(value = "book", defaultValue = "Dom Quixote") String book) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                    Por favor, me forneca
                    um breve resumo do livro {book}
                    e também a biografia de seu autor.
                """);
        promptTemplate.add("book", book);
        return chatClient.call(promptTemplate.create())
                .getResult()
                .getOutput()
                .getContent();
    }
}
