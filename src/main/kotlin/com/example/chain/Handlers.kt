package com.example.chain

import com.example.model.ApiResponse

class NullCheckHandler<T> : AbstractResponseHandler<ApiResponse<T>>() {
    override suspend fun processResponse(response: ApiResponse<T>): ApiResponse<T> {
        if (response.data == null) {
            response.metadata = response.metadata + mapOf("warning" to "Null data received")
        }
        return response
    }
}

class MetadataEnrichmentHandler<T> : AbstractResponseHandler<ApiResponse<T>>() {
    override suspend fun processResponse(response: ApiResponse<T>): ApiResponse<T> {
        response.metadata = response.metadata + mapOf(
            "processedAt" to System.currentTimeMillis(),
            "version" to "1.0"
        )
        return response
    }
}

class SlackSpecificHandler<T> : AbstractResponseHandler<ApiResponse<T>>() {
    override suspend fun processResponse(response: ApiResponse<T>): ApiResponse<T> {
        response.metadata = response.metadata + mapOf(
            "platform" to "slack",
            "apiVersion" to "v2"
        )
        return response
    }
}

class GithubSpecificHandler<T> : AbstractResponseHandler<ApiResponse<T>>() {
    override suspend fun processResponse(response: ApiResponse<T>): ApiResponse<T> {
        response.metadata = response.metadata + mapOf(
            "platform" to "github",
            "apiVersion" to "v3"
        )
        return response
    }
}