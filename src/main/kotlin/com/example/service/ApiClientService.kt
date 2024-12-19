package com.example.service


import com.example.chain.ResponseChainRegistry
import com.example.config.ApiConfigurations
import com.example.model.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class ApiClientService(
    private val webClient: WebClient,
    private val chainRegistry: ResponseChainRegistry,
    private val apiConfigurations: ApiConfigurations
) {
    suspend fun <T> makeRequest(
        apiType: String,
        method: HttpMethod,
        path: String,
        requestBody: Any? = null,
    ): ApiResponse<T> = withContext(Dispatchers.IO) {
        val config = when (apiType) {
            "slack" -> apiConfigurations.slack
            "github" -> apiConfigurations.github
            else -> throw IllegalArgumentException("Unknown API type: $apiType")
        }

        val chain = chainRegistry.getChain<T>(apiType)
            ?: throw IllegalStateException("No chain configured for $apiType")

        try {
            val response = webClient
                .method(method)
                .uri("${config?.baseUrl}$path")
                .apply {
                    requestBody?.let { bodyValue(it) }
                    config?.headers?.forEach { (key, value) ->
                        header(key, value)
                    }
                }
                .retrieve()
                .awaitBody<Any>()  // Using suspend function directly

            chain.handle(
                ApiResponse(
                    data = response,
                    status = "SUCCESS"
                )
            )
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private fun <T> handleError(error: Exception): ApiResponse<T> {
        return ApiResponse(
            status = "ERROR",
            metadata = mapOf("error" to (error.message ?: "Unknown error"))
        )
    }
}