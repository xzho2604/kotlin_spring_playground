package com.example.chain

interface ResponseHandler<T> {
    fun setNext(handler: ResponseHandler<T>): ResponseHandler<T>
    suspend fun handle(response: T): T
}

abstract class AbstractResponseHandler<T> : ResponseHandler<T> {
    private var nextHandler: ResponseHandler<T>? = null

    override fun setNext(handler: ResponseHandler<T>): ResponseHandler<T> {
        nextHandler = handler
        return handler
    }

    override suspend fun handle(response: T): T {
        val processed = processResponse(response)
        return nextHandler?.handle(processed) ?: processed
    }

    protected abstract suspend fun processResponse(response: T): T
}