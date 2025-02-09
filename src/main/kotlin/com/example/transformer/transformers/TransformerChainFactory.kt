package com.example.transformer.transformers

import org.springframework.stereotype.Service


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

// Chain of transformers
class TransformerChain<I, O>(private val transformers: List<Transformer<*, *>>) {
    @Suppress("UNCHECKED_CAST")
    fun execute(input: I): O {
        return transformers.fold(input as Any) { acc, transformer ->
            transformer as Transformer<Any, Any>
            transformer.transform(acc)
        } as O
    }
}


// Factory for creating transformer chains


// Usage in a service

//TODO: later change to use builder pattern
//// Builder for transformer chains
//class TransformerChainBuilder<I, O> {
//    private val transformers = mutableListOf<Transformer<*, *>>()
//
//    fun <T> addTransformer(transformer: Transformer<*, T>): TransformerChainBuilder<I, T> {
//        transformers.add(transformer)
//        @Suppress("UNCHECKED_CAST")
//        return this as TransformerChainBuilder<I, T>
//    }
//
//    fun build(): TransformerChain<I, O> {
//        return TransformerChain(transformers)
//    }
//}
//
//// Configuration class for registering transformer chains
//@Configuration
//class TransformerConfig {
//
//    @Bean
//    fun transformerChainFactory(): TransformerChainFactory {
//        val slackChain = TransformerChainBuilder<String, ValidatedSlackMessage>()
//            .addTransformer(SlackMessagePreprocessor())
//            .addTransformer(SlackMessageFormatter())
//            .addTransformer(SlackMessageValidator())
//            .build()
//
//        val chainMap = mapOf(
//            ResponseType.SLACK_MESSAGE to slackChain
//            // Add more chains here
//        )
//
//        return TransformerChainFactory(chainMap)
//    }
//}


