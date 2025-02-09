package com.example.transformer.transformers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


// Base interface for all transformers
interface Transformer<in I, out O> {
    fun transform(input: I): O
}

// Enum to identify different types of responses
enum class ResponseType {
    SLACK_MESSAGE,
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

// Example transformers for Slack
class SlackMessagePreprocessor : Transformer<String, SlackMessageDTO> {
    override fun transform(input: String): SlackMessageDTO {
        // Transform raw input to Slack message DTO
        return SlackMessageDTO(input)
    }
}

class SlackMessageFormatter : Transformer<SlackMessageDTO, FormattedSlackMessage> {
    override fun transform(input: SlackMessageDTO): FormattedSlackMessage {
        // Format the Slack message
        return FormattedSlackMessage(input)
    }
}

class SlackMessageValidator : Transformer<FormattedSlackMessage, ValidatedSlackMessage> {
    override fun transform(input: FormattedSlackMessage): ValidatedSlackMessage {
        // Validate the formatted message
        return ValidatedSlackMessage(input)
    }
}

// Factory for creating transformer chains
@Service
class TransformerChainFactory {
    private val chainMap: Map<ResponseType, TransformerChain<*, *>> = mapOf(
        ResponseType.SLACK_MESSAGE to TransformerChain<Any, Any>(
            listOf(
                SlackMessagePreprocessor(),
                SlackMessageFormatter(),
                SlackMessageValidator()
            )
        )
        // Add more chains for other response types
    )

    fun <I, O> getChain(type: ResponseType): TransformerChain<I, O> {
        @Suppress("UNCHECKED_CAST")
        return chainMap[type] as? TransformerChain<I, O>
            ?: throw IllegalArgumentException("No transformer chain found for type: $type")
    }
}

// Usage in a service
@Service
class MessageService(private val transformerChainFactory: TransformerChainFactory) {

    fun processSlackMessage(message: String): ValidatedSlackMessage {
        val chain = transformerChainFactory.getChain<String, ValidatedSlackMessage>(ResponseType.SLACK_MESSAGE)
        return chain.execute(message)
    }
}

// Data classes for the transformation steps
data class SlackMessageDTO(val content: String)
data class FormattedSlackMessage(val dto: SlackMessageDTO)
data class ValidatedSlackMessage(val formatted: FormattedSlackMessage)

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


