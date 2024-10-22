package com.ucupstore.orderservice.book;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

class BookClientTest {

    private MockWebServer mockWebServer;
    private BookClient bookClient;

    @BeforeEach
    void setUp() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        WebClient web = WebClient.builder()
                .baseUrl(this.mockWebServer.url("/").toString())
                .build();
        this.bookClient = new BookClient(web);
    }

    @AfterEach
    void cleanUp() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void whenBookIsbnExistThenReturnBook() {
        String bookIsbn = "123456890";

        MockResponse mockResponse = new MockResponse().addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody("""
                        {
                        "isbn": %s,
                        "title": "Title",
                        "author": "Author",
                        "price": 9.90,
                        "publisher": "Polarsophia"
                        }
                        """.formatted(bookIsbn));
        mockWebServer.enqueue(mockResponse);

        Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);

        StepVerifier.create(book)
                .expectNextMatches(
                        b -> b.isbn().equals(bookIsbn))
                .verifyComplete();
    }

    @Test
    void whenBookDoesntExistReturnEmptyMono() {
        String bookIsbn = "123456890";

        MockResponse mockResponse = new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        mockWebServer.enqueue(mockResponse);

        Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);

        StepVerifier.create(book)
                .verifyComplete();
    }
}