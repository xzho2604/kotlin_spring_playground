package com.example.transformer.transformers


// Base interface for all transformers
interface Transformer<in I, out O> {
    fun transform(input: I): O
}

// Enum to identify different types of responses
enum class ActionType {
    SLACK_MESSAGE,
    DISCORD_MESSAGE,
    EMAIL_NOTIFICATION,
    SMS_NOTIFICATION
    // Add more types as needed
}

// TODO: neeed register all the chains of all action types
class TransformerChain<I, O>(private val transformers: List<Transformer<*, *>>) {
    @Suppress("UNCHECKED_CAST")
    fun execute(input: I): O {
        return transformers.fold(input as Any) { acc, transformer ->
            transformer as Transformer<Any, Any>
            transformer.transform(acc)
        } as O
    }
}