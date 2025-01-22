package com.example.service

import com.example.model.User
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody


@Service
class UserService(private val webClient: WebClient) {

    suspend fun getUser(id: Long): User {
        return webClient.get()
            .uri("/users/$id")
            .retrieve()
            .awaitBody()
    }

    suspend fun getAllUsers(): List<User> {
        return webClient.get()
            .uri("/users")
            .retrieve()
            .awaitBody()
    }

    suspend fun createUser(user: User): User {
        return webClient.post()
            .uri("/users")
            .bodyValue(user)
            .retrieve()
            .awaitBody()
    }
}
