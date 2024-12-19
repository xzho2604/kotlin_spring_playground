package com.example.controller

import com.example.APIService
import com.example.model.GithubResponse
import org.springframework.http.HttpMethod
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class GreetingControllerRest(private val apiService: APIService) {
    @GetMapping("/{name}")
    suspend fun retrieveGreeting(
        @PathVariable("name") name: String,
    ): String {
        val response = apiService.getRepository(
            owner = "octocat",
            repo = "Hello-World"
        )

        return "Hello $name you are sexy yeah bala!"
    }
}