package com.example.service


import com.example.model.ApiResponse
import com.example.model.GithubResponse
import com.example.model.SlackMessage
import com.example.model.SlackResponse
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service

@Service
class SlackService(private val apiClientService: ApiClientService) {
    suspend fun sendMessage(message: SlackMessage): ApiResponse<SlackResponse> {
        return apiClientService.makeRequest(
            apiType = "slack",
            method = HttpMethod.POST,
            path = "/chat.postMessage",
            requestBody = message,
        )
    }
}

@Service
class GithubService(private val apiClientService: ApiClientService) {
    suspend fun getRepository(owner: String, repo: String): ApiResponse<GithubResponse> {
        return apiClientService.makeRequest(
            apiType = "github",
            method = HttpMethod.GET,
            path = "/repos/$owner/$repo",
        )
    }
}