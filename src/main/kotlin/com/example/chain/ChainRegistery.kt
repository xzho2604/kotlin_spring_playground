package com.example.chain

import com.example.model.ApiResponse
import org.springframework.stereotype.Component


@Component()
class ResponseChainRegistry {
    private val chains: MutableMap<String, ResponseHandler<*>> = mutableMapOf()

    fun registerChain(name: String, chain: ResponseHandler<*>) {
        chains[name] = chain
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getChain(name: String): ResponseHandler<ApiResponse<T>>? {
        return chains[name] as? ResponseHandler<ApiResponse<T>>
    }
}