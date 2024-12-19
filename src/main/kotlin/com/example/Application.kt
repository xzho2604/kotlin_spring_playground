package com.example

import com.example.model.ApiResponse
import com.example.model.GithubResponse
import com.example.service.ApiClientService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service


@Service
class APIService(private val apiClientService: ApiClientService) {
    suspend fun getRepository(owner: String, repo: String): ApiResponse<GithubResponse> {
        return apiClientService.makeRequest<GithubResponse>(
            apiType = "github",
            method = HttpMethod.GET,
            path = "/repos/$owner/$repo"
        )
    }
}




@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}